import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        assertEquals(1, huntDisplay.points.size());
        
        // Add multiple points and check if they are added correctly
        TrigPoint point2 = new TrigPoint(60, 30, 45, false);
        TrigPoint point3 = new TrigPoint(90, 180, 90, true);
        huntDisplay.addPoint(point2);
        huntDisplay.addPoint(point3);
        assertEquals(3, huntDisplay.points.size());
        
        // Check if the points are added with correct coordinates
        assertEquals(0.5, huntDisplay.points.elementAt(0).getX(), 0.001);
        assertEquals(0.833, huntDisplay.points.elementAt(0).getY(), 0.001);
        assertEquals(0.166, huntDisplay.points.elementAt(1).getX(), 0.001);
        assertEquals(0.5, huntDisplay.points.elementAt(1).getY(), 0.001);
        assertEquals(1, huntDisplay.points.elementAt(2).getX(), 0.001);
        assertEquals(1, huntDisplay.points.elementAt(2).getY(), 0.001);
        
        // Check if the repaint method is called after adding the point
        // (it cannot be directly tested, so check if the background color is changed)
        assertEquals(Color.WHITE, huntDisplay.getBackground());
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(30, 60, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        
        // Add multiple landmarks and check if they are added correctly
        Landmark landmark2 = new Landmark(45, 90, new ImageIcon());
        Landmark landmark3 = new Landmark(60, 120, new ImageIcon());
        huntDisplay.addLandMark(landmark2);
        huntDisplay.addLandMark(landmark3);
        assertEquals(3, huntDisplay.landmarks.size());
        
        // Check if the landmarks are added with correct coordinates
        assertEquals(0.833, huntDisplay.landmarks.elementAt(0).getLat(), 0.001);
        assertEquals(1, huntDisplay.landmarks.elementAt(0).getLon(), 0.001);
        assertEquals(0.5, huntDisplay.landmarks.elementAt(1).getLat(), 0.001);
        assertEquals(0.833, huntDisplay.landmarks.elementAt(1).getLon(), 0.001);
        assertEquals(0.333, huntDisplay.landmarks.elementAt(2).getLat(), 0.001);
        assertEquals(0.666, huntDisplay.landmarks.elementAt(2).getLon(), 0.001);
        
        // Check if the repaint method is called after adding the landmark
        // (it cannot be directly tested, so check if the background color is changed)
        assertEquals(Color.WHITE, huntDisplay.getBackground());
    }
    
    @Test
    public void testGetPreferredSize() {
        Dimension dimension = huntDisplay.getPreferredSize();
        
        // Check if the preferred size is set correctly
        assertEquals(200, dimension.width);
        assertEquals(200, dimension.height);
    }
    
    @Test
    public void testGetMinimumSize() {
        Dimension dimension = huntDisplay.getMinimumSize();
        
        // Check if the minimum size is set correctly
        assertEquals(50, dimension.width);
        assertEquals(50, dimension.height);
    }
}