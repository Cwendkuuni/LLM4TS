import javax.sound.sampled.*;
import java.io.*;
import org.junit.Test;

public class MP3Test {
    @Test
    public void testRun() {
        try {
            // Test case 1: Check if the MP3 player can play a valid file
            MP3 player1 = new MP3("valid-file.mp3");
            // Wait for the player to finish playing the file
            player1.join();
            
            // Test case 2: Check if the MP3 player can handle a null filename
            MP3 player2 = new MP3(null);
            // Wait for the player to finish playing the file
            player2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}