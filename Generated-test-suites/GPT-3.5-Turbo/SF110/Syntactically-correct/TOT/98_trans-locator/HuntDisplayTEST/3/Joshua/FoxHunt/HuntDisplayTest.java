import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }
    
    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90, 180, 0, false);
        huntDisplay.addPoint(point);
        
        assertEquals(1, huntDisplay.points.size());
        
        TrigPoint addedPoint = (TrigPoint) huntDisplay.points.firstElement();
        assertEquals(1.0, addedPoint.getX(), 0.001);
        assertEquals(0.5, addedPoint.getY(), 0.001);
        assertEquals(0, addedPoint.getTheta());
        assertFalse(addedPoint.isFox());
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        
        assertEquals(1, huntDisplay.landmarks.size());
        
        Landmark addedLandmark = (Landmark) huntDisplay.landmarks.firstElement();
        assertEquals(1.0, addedLandmark.getLon(), 0.001);
        assertEquals(0.5, addedLandmark.getLat(), 0.001);
        assertNotNull(addedLandmark.getIcon());
    }
    
    @Test
    public void testPaintComponent_noPointsAndLandmarks() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new Graphics();
        
        display.paintComponent(g);
        
        // Check that the display is cleared
        assertClear(g);
    }
    
    @Test
    public void testPaintComponent_withPoints() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new Graphics();
        
        display.addPoint(new TrigPoint(90, 180, 0, false));
        display.paintComponent(g);
        
        // Check that the points are drawn correctly
        assertPoint(g, Color.BLACK, 0.5, 0.5);
    }
    
    @Test
    public void testPaintComponent_withFox() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new Graphics();
        
        display.addPoint(new TrigPoint(90, 180, 0, true));
        display.paintComponent(g);
        
        // Check that the fox is drawn correctly
        assertPoint(g, Color.GREEN, 0.5, 0.5);
    }
    
    @Test
    public void testPaintComponent_withLandmarks() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new Graphics();
        
        ImageIcon icon = new ImageIcon();
        display.addLandMark(new Landmark(90, 180, icon));
        display.paintComponent(g);
        
        // Check that the landmarks are drawn correctly
        assertLandmark(g, icon, 0.5, 0.5);
    }
    
    private void assertClear(Graphics g) {
        // TODO: Implement assertion for cleared display
    }
    
    private void assertPoint(Graphics g, Color color, double x, double y) {
        // TODO: Implement assertion for drawn points
    }
    
    private void assertLandmark(Graphics g, ImageIcon icon, double x, double y) {
        // TODO: Implement assertion for drawn landmarks
    }
}