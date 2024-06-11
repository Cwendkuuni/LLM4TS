import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(200, 200), display.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(50, 50), display.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(90, 180, 0, false);
        display.addPoint(point);
        // Check if the point was added successfully
        assertTrue(display.points.size() > 0);
        // Add more test cases for boundary and exception scenarios if needed
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        display.addLandMark(landmark);
        // Check if the landmark was added successfully
        assertTrue(display.landmarks.size() > 0);
        // Add more test cases for boundary and exception scenarios if needed
    }

    // Add more test cases for other methods as needed

}