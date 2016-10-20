package Pomodoro;
 
// Imports
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 

// Creates pomodoro timer
public class PomodoroTime extends Application {
	
	// Declarations
	private Thread timerThread;
	private Label timerValue;
	private Label messageBox;
	private int minutes = 25;
	private int seconds = 1;
	private Scene timerScene;
	private Scene settingsScene;
	private ImageView growingTomato;
	private int pomoSize = 1;
	private Boolean pomodoroTime = false; 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
        primaryStage.setTitle("Pomodoro!");
        
        // New panes
        BorderPane settingsBorderPane = new BorderPane();
        BorderPane timerBorderPane = new BorderPane();
        StackPane timerStackPane = new StackPane();
        
        // Settings page
        Label pomoTitle = new Label("Pomodoro");
        pomoTitle.setFont(Font.font("Gill Sans Nova Light", 50));
        
        // Make button appear as tomato
        Button pomoButton = new Button();        
        pomoButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("tomato.png"), 100, 100, true, true)));
        pomoButton.setStyle("-fx-background-color: transparent");
        pomoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	// Play noise, change to full screen timer and start
                playMedia("sounds/squish.mp3");
                primaryStage.setScene(timerScene);
            	primaryStage.setFullScreen(true);
                startTimer();
                messageBox.setText("Get to work!");
            }            
        });
     
        // Contain button and title in vbox
        VBox settingsVBox = new VBox();
        settingsVBox.getChildren().addAll(pomoTitle, pomoButton);
        settingsVBox.setAlignment(Pos.CENTER);        
        settingsBorderPane.setCenter(settingsVBox);
        settingsBorderPane.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 10;" +
                "-fx-border-insets: 0;" + 
                "-fx-border-radius: 0;" + 
                "-fx-border-color: black;");
        
        // Timer page
        // Space for timer value and message
        timerValue = new Label();
        timerValue.setFont(Font.font("Gill Sans Nova Light", 100));        
        messageBox = new Label();
        messageBox.setFont(Font.font("Gill Sans Nova Light", 40));
                
        // Vbox to hold timer value and message
        VBox windowVBox = new VBox();
        windowVBox.getChildren().addAll(messageBox, timerValue);
        windowVBox.setAlignment(Pos.CENTER);
        windowVBox.setSpacing(10);
        windowVBox.setPadding(new Insets(15, 15, 15, 15));
        
        // Growing tomato during pomotime
		growingTomato = new ImageView();
		growingTomato.setFitWidth(1);
		growingTomato.setVisible(false);
		growingTomato.setPreserveRatio(true);
		Image tomatoImage = new Image(PomodoroTime.class.getResourceAsStream("tomato.png"));
		growingTomato.setImage(tomatoImage);
		
		// Add tomato to sp
		timerStackPane.getChildren().add(growingTomato);
        
		//Configure panes 
        timerBorderPane.setCenter(windowVBox);
        timerBorderPane.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 25;" +
                "-fx-border-insets: 0;" + 
                "-fx-border-radius: 0;" + 
                "-fx-border-color: black;");
       
        
        timerStackPane.getChildren().add(timerBorderPane);
       
        // Create scenes
        settingsScene = new Scene(settingsBorderPane, 500, 310);
        timerScene = new Scene(timerStackPane, 500, 310);
       
        // Begin with settings 
        primaryStage.setScene(settingsScene);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(500);
        primaryStage.show();
   
    }
    
    public void startTimer()	{
    	// Define new task
		Task<Void> timerTime = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				
				// Loop forever
				while (true) {
										
					//If we're counting down 25 minutes 
					if (!pomodoroTime)	{
					
						// Tomato is small	
						pomoSize = 1;
						
						//Hide tomato image w
						Platform.runLater(new Runnable() {
							@Override
							public void run()	{  
								growingTomato.setVisible(false);
								
							}
						});	
					
						// Decrement minutes when reached zero seconds	
						if (seconds == 0)	{
														
							minutes--;
							seconds = 60;
						}
									
						// Display timer value	
						Platform.runLater(new Runnable() {
							@Override
							public void run()	{
								timerValue.setText(String.valueOf(minutes) + ":" + String.format("%02d", seconds));
								
							}
						});	
							
						// Decrement seconds	
						seconds--;
					
						// If we're done counting down 25 minutes	
						if ((minutes == 0) && (seconds == 0))	{
							pomodoroTime = true;
						playMedia("sounds/notify.mp3");
							
							Platform.runLater(new Runnable() {
								@Override
								public void run() 	{
									messageBox.setText("Pomodoro time!");
									
								}
							});	
						
							// Set timer back to 5 mins	
							minutes = 5; 
							seconds = 1;
						}
					}
					
					// Pomotim (5 minutes)e
					else	{
						
						//Set growing tomato visiblew
						Platform.runLater(new Runnable() {
							@Override
							public void run()  {
								growingTomato.setVisible(true);
								
							}
						});	
					
						// If at end of minute	
						if (seconds == 0)	{
							minutes --;
							seconds = 60;
						}
						
					// Update timer displayw
						Platform.runLater(new Runnable() {
							@Override
							public void run()  {
								timerValue.setText(String.valueOf(minutes) + ":" + String.format("%02d", seconds));
								growingTomato.setFitWidth(pomoSize);
								pomoSize = pomoSize + 8;
								
							}
						});	
						
						// Decrement seconds	
						seconds--;
					
						// If at end of 5 minutes, get back to work	
						if ((minutes == 0) && (seconds == 0))	{
							pomodoroTime = false;
						playMedia("sounds/notify.mp3");
							Platform.runLater(new Runnable() {
								@Override
								public void run()  {
									messageBox.setText("Back to work!");
									
								}
							});	
						
							// Reset to 25 mins	
							minutes = 25; 
							seconds = 1;
						};
					
					}
					
					// Delay the thread by the timer period
					Thread.sleep(50);
		    	}
			}
		};
	
		// Run thread forever 	
		timerThread = new Thread(timerTime);
		timerThread.setDaemon(true);
		timerThread.start();							
		
    }
    
    private void playMedia(String mediaLoc){
       Media soundToPlay = new Media(ClassLoader.getSystemResource(mediaLoc).toExternalForm());
    	 if (soundToPlay != null){
            MediaPlayer mediaPlayer = new MediaPlayer(soundToPlay);
            mediaPlayer.play();
        }
    }
//

    
        // Launch/
	public static void main(String[] args) {

		// Launch the loading screen and then the main application.
		LauncherImpl.launchApplication(PomodoroTime.class, args);
	}
    
}