import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        
        display.addPoint(point);
        
        // Check if the point was added successfully
        assertEquals(1, display.points.size());
    }
    
    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        
        display.addLandmark(landmark);
        
        // Check if the landmark was added successfully
        assertEquals(1, display.landmarks.size());
    }
    
    @Test
    public void testPaintComponentEmptyList() {
        HuntDisplay display = new HuntDisplay();
        
        // TODO: Add test cases for paintComponent() with an empty list of points and landmarks
    }
    
    @Test
    public void testPaintComponentSingleElementList() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addPoint(point);
        display.addLandmark(landmark);
        
        // TODO: Add test cases for paintComponent() with a list of points and landmarks with only one element
    }
    
    @Test
    public void testPaintComponentMultipleElementsList() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(0, 0, 0, false);
        TrigPoint point2 = new TrigPoint(1, 1, 1, true);
        Landmark landmark1 = new Landmark(0, 0, new ImageIcon());
        Landmark landmark2 = new Landmark(1, 1, new ImageIcon());
        display.addPoint(point1);
        display.addPoint(point2);
        display.addLandmark(landmark1);
        display.addLandmark(landmark2);
        
        // TODO: Add test cases for paintComponent() with a list of points and landmarks with multiple elements
    }
    
    @Test
    public void testPaintComponentExtremeCoordinates() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(Double.MAX_VALUE, Double.MIN_VALUE, 0, false);
        Landmark landmark = new Landmark(Double.MAX_VALUE, Double.MIN_VALUE, new ImageIcon());
        display.addPoint(point);
        display.addLandmark(landmark);
        
        // TODO: Add test cases for paintComponent() with extreme coordinate values
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddNullPoint() {
        HuntDisplay display = new HuntDisplay();
        display.addPoint(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddNullLandmark() {
        HuntDisplay display = new HuntDisplay();
        display.addLandmark(null);
    }
}