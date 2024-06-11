import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, true);
        display.addPoint(point);
        // Assert that point is added to the display Vector
        assertTrue(display.points.contains(point));
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandMark(landmark);
        // Assert that landmark is added to the display Vector
        assertTrue(display.landmarks.contains(landmark));
    }

    // Test paintComponent method
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        // Create test points
        TrigPoint point1 = new TrigPoint(0, 0, 0, false);
        TrigPoint point2 = new TrigPoint(0.5, 0.5, 0, true);
        // Add points to display
        display.addPoint(point1);
        display.addPoint(point2);
        // Create test landmarks
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        // Add landmark to display
        display.addLandMark(landmark);
        // Test that the points and landmarks are painted correctly
        // TODO: Implement this test case based on visual comparison of the actual display component output
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension prefSize = display.getPreferredSize();
        // Assert that preferred size is set correctly
        assertEquals(200, (int)prefSize.getWidth());
        assertEquals(200, (int)prefSize.getHeight());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension minSize = display.getMinimumSize();
        // Assert that minimum size is set correctly
        assertEquals(50, (int)minSize.getWidth());
        assertEquals(50, (int)minSize.getHeight());
    }
}