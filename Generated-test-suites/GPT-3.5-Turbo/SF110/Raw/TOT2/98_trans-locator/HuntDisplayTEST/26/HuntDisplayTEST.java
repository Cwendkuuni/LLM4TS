import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;

    @Before
    public void setup() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90, 180, 45, true);
        huntDisplay.addPoint(point);
        assertEquals(1, huntDisplay.points.size());
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
    }

    @Test
    public void testPaintComponent() {
        Graphics g = new Graphics();
        huntDisplay.paintComponent(g);
        // Add assertion here based on expected behavior of the paintComponent method
    }

    // Add more test methods for testing edge cases and exception handling scenarios

}