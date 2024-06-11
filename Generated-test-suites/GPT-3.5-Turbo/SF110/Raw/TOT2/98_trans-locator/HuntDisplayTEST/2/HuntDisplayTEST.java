import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    // Test case for addPoint method
    @Test
    public void testAddPoint() {
        TrigPoint testPoint = new TrigPoint(1.0, 2.0, 90, true);
        HuntDisplay display = new HuntDisplay();
        display.addPoint(testPoint);
        assertEquals(1, display.points.size());
    }

    // Test case for addLandMark method
    @Test
    public void testAddLandMark() {
        Landmark testLandmark = new Landmark(0.5, 0.5, new ImageIcon("icon.png"));
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(testLandmark);
        assertEquals(1, display.landmarks.size());
    }

    // Test case for paintComponent method
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        // To be implemented
    }
}