package Newzgrabber;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber app = new Newzgrabber();
        assertNotNull(app);
    }

    @Test
    public void testFindDirectory() {
        Newzgrabber app = new Newzgrabber();
        String directory = app.findDirectory("Newz.jar");
        assertNotNull(directory);
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber app = new Newzgrabber();
        app.updateIni("test.ini", "prop", "key", "value");
        // Verify if ini file is updated successfully
    }

    // Add more test cases for other methods here

}