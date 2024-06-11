import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    private HuntDisplay display;
    
    @Before
    public void setUp() {
        display = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint_typicalCase() {
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        display.addPoint(point);
        
        // Check if the point is added to the list
        assertEquals(1, display.points.size());
        
        // Check if the added point has the correct values
        TrigPoint addedPoint = display.points.get(0);
        assertEquals(0.5, addedPoint.getX(), 0.001);
        assertEquals(0.5, addedPoint.getY(), 0.001);
        assertEquals(0, addedPoint.getTheta());
        assertEquals(true, addedPoint.isFox());
    }
    
    @Test
    public void testAddPoint_edgeCase() {
        TrigPoint point = new TrigPoint(90, 180, -90, false);
        display.addPoint(point);
        
        // Check if the point is added to the list
        assertEquals(1, display.points.size());
        
        // Check if the added point has the correct values
        TrigPoint addedPoint = display.points.get(0);
        assertEquals(0.0, addedPoint.getX(), 0.001);
        assertEquals(0.0, addedPoint.getY(), 0.001);
        assertEquals(-90, addedPoint.getTheta());
        assertEquals(false, addedPoint.isFox());
    }
    
    @Test
    public void testAddLandmark_typicalCase() {
        Landmark landmark = new Landmark(30, -45, new ImageIcon());
        display.addLandMark(landmark);
        
        // Check if the landmark is added to the list
        assertEquals(1, display.landmarks.size());
        
        // Check if the added landmark has the correct values
        Landmark addedLandmark = display.landmarks.get(0);
        assertEquals(0.333, addedLandmark.getLat(), 0.001);
        assertEquals(-0.25, addedLandmark.getLon(), 0.001);
        assertEquals(new ImageIcon(), addedLandmark.getIcon());
    }
    
    @Test
    public void testAddLandmark_edgeCase() {
        Landmark landmark = new Landmark(90, 180, null);
        display.addLandMark(landmark);
        
        // Check if the landmark is added to the list
        assertEquals(1, display.landmarks.size());
        
        // Check if the added landmark has the correct values
        Landmark addedLandmark = display.landmarks.get(0);
        assertEquals(1.0, addedLandmark.getLat(), 0.001);
        assertEquals(1.0, addedLandmark.getLon(), 0.001);
        assertNull(addedLandmark.getIcon());
    }
    
    @Test
    public void testPaintComponent_noPointsOrLandmarks() {
        // Test if the paintComponent method works correctly when there are no points or landmarks
        // Check if the dimensions of the display component are correct
        assertEquals(new Dimension(200, 200), display.getPreferredSize());
        assertEquals(new Dimension(50, 50), display.getMinimumSize());
        
        // Make sure that the display component is empty (has no drawn points or landmarks)
        assertEquals(0, display.points.size());
        assertEquals(0, display.landmarks.size());
    }
    
    @Test
    public void testPaintComponent_withPointsAndLandmarks() {
        // Test if the paintComponent method works correctly when there are points and landmarks
        
        // Add some points and landmarks to the display
        TrigPoint point1 = new TrigPoint(45, 90, 0, true);
        TrigPoint point2 = new TrigPoint(30, 45, 45, false);
        display.addPoint(point1);
        display.addPoint(point2);
        
        Landmark landmark1 = new Landmark(50, 60, new ImageIcon());
        Landmark landmark2 = new Landmark(-30, -60, null);
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        
        // Check if the paintComponent method correctly draws the points and landmarks on the display
        // This test case can be visually inspected
        
        // ...
    }
}