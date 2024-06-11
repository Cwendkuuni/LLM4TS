import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, true);
        
        display.addPoint(point);
        
        // Validate that the point has been added to the points vector
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.elementAt(0));
        
        // TODO: Validate that repaint is called
        
        // TODO: Validate that the point is correctly painted on the component
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon());
        
        display.addLandMark(landmark);
        
        // Validate that the landmark has been added to the landmarks vector
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.elementAt(0));
        
        // TODO: Validate that repaint is called
        
        // TODO: Validate that the landmark is correctly painted on the component
    }
    
    // TODO: Add additional test methods to test all methods in the HuntDisplay class
    
}