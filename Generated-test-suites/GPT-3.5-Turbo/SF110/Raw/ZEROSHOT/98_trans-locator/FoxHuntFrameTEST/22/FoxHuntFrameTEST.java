import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
       
    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        
        TrigPoint point = frame.points.elementAt(0);
        assertNotNull(point);
        assertEquals(37.7749, point.getLat(), 0);
        assertEquals(-122.4194, point.getLon(), 0);
        assertEquals(45, point.getAngle());
        
        TrigPoint foxPoint = frame.fox.elementAt(0);
        assertNotNull(foxPoint);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        int NUM = 0;

        File file = new File("./landmarks.cfg");
        assertTrue(file.exists());

        try {
            FileInputStream inFile = new FileInputStream(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
            String input = "";

            if ((input = in.readLine()) != null) {
                final String num = input.substring(4);
                NUM = Integer.parseInt(num);
            }

            assertEquals(NUM, frame.display.landmarks.size());
            assertEquals(NUM, frame.display.panel.getComponentCount());

            in.close();
            inFile.close();
        } catch (FileNotFoundException e) {
            fail("Can't Find the file");
        } catch (IOException e2) {
            fail("Error communicating with the file");
        }
    }
}