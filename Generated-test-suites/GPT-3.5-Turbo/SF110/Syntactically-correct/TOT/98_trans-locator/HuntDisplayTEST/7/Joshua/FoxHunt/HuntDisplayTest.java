import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, 0, false);
        HuntDisplay display = new HuntDisplay();
        display.addPoint(point);
        
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.get(0));
    }
    
    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        HuntDisplay display = new HuntDisplay();
        display.addLandmark(landmark);
        
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.get(0));
    }
    
    @Test
    public void testPaintComponentNoPointsOrLandmarks() {
        // TODO: Write test case to test paintComponent method when there are no points or landmarks
    }
    
    @Test
    public void testPaintComponentWithPoints() {
        // TODO: Write test case to test paintComponent method when there are points
    }
    
    @Test
    public void testPaintComponentWithLandmarks() {
        // TODO: Write test case to test paintComponent method when there are landmarks
    }
    
    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        // TODO: Write test case to test paintComponent method when there are both points and landmarks
    }
}