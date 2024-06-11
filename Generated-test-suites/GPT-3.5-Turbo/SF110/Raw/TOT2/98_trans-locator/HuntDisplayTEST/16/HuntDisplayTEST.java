import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;

public class HuntDisplayTest {
    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expected = new Dimension(200, 200);
        assertEquals(expected, display.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension expected = new Dimension(50, 50);
        assertEquals(expected, display.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        display.addPoint(new TrigPoint(90, 180, 0, true));
        assertEquals(1, display.points.size());
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(new Landmark(90, 180, new ImageIcon()));
        assertEquals(1, display.landmarks.size());
    }

    @Test
    public void testPaintComponent() {
        // This test is a bit complex and could be split further into edge cases
        // It tests the paintComponent method by adding points and landmarks
        
        HuntDisplay display = new HuntDisplay();
        display.addPoint(new TrigPoint(90, 180, 0, true));
        display.addLandMark(new Landmark(90, 180, new ImageIcon()));
        
        // Additional assertions should be added based on expected behavior
        // such as checking if the points and landmarks were properly drawn
    }
}