import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        display.addPoint(new TrigPoint(90.0, 180.0, 45, true));
        // Add assertion here to check functionality
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(new Landmark(90.0, 180.0, new ImageIcon("icon")));
        // Add assertion here to check functionality
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        // Add test cases for paintComponent method
    }

    // Add more test cases for the other methods in HuntDisplay class

}