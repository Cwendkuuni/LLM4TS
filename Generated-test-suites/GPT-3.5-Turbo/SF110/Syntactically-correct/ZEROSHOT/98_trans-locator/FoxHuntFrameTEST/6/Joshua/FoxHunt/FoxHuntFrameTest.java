import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test case 1: Add a point with angle 45, LAT 37.5, LON -122.5
        frame.theta.setText("45");
        frame.lat.setText("37.5");
        frame.lon.setText("-122.5");
        frame.addPoint();
        int expectedPointsSize = 1;
        int expectedFoxSize = 0;
        assertEquals(expectedPointsSize, frame.points.size());
        assertEquals(expectedFoxSize, frame.fox.size());

        // Test case 2: Add a point with angle 90, LAT 40.0, LON -123.0
        frame.theta.setText("90");
        frame.lat.setText("40.0");
        frame.lon.setText("-123.0");
        frame.addPoint();
        expectedPointsSize = 2;
        expectedFoxSize = 1;
        assertEquals(expectedPointsSize, frame.points.size());
        assertEquals(expectedFoxSize, frame.fox.size());
    }
}