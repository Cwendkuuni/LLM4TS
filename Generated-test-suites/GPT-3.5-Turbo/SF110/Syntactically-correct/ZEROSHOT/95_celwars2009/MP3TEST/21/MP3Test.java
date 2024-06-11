import org.junit.Test;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        // Add assertions here to test the constructor
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        // Add assertions here to test the run method
    }

    // Add more test methods to test other functionality of the MP3 class

}