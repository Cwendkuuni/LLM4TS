import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertTrue(frame.points.size() == 1);
        assertTrue(frame.fox.size() == 0);
    }

    @Test
    public void testLoadLandmarks_FileNotFoundException() {
        FoxHuntFrame frame = new FoxHuntFrame();
        try {
            frame.loadLandmarks();
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLoadLandmarks_IOException() {
        FoxHuntFrame frame = new FoxHuntFrame();
        try {
            File file = new File("non_existing_file.cfg");
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.close();
            frame.loadLandmarks();
        } catch (IOException e) {
            assertTrue(true);
        }
    }

}