import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class MP3Test {
    
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }
    
    @Test
    public void testRun() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        MP3 mp3 = new MP3("test.mp3");
        
        // Wait for some time to allow the MP3 to finish playing
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Check if any exceptions were thrown during the execution of run()
        assertNull(outContent.toString());
    }
}