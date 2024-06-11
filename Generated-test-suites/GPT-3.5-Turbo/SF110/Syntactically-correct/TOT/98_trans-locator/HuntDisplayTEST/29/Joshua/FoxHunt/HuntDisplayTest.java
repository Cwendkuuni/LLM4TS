import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        // Typical use case: Adding a point with valid coordinates
        TrigPoint point = new TrigPoint(45, 90, 0, false);
        HuntDisplay display = new HuntDisplay();
        display.addPoint(point);
        assertEquals(1, display.points.size());
        
        // Edge case: Adding a point with minimum coordinates
        TrigPoint point2 = new TrigPoint(-90, -180, 0, true);
        display.addPoint(point2);
        assertEquals(2, display.points.size());
        
        // Error scenario: Adding a point with invalid coordinates
        TrigPoint point3 = new TrigPoint(100, -200, 0, false);
        display.addPoint(point3);
        assertEquals(2, display.points.size());
    }
    
    @Test
    public void testAddLandmark() {
        // Typical use case: Adding a landmark with valid coordinates and icon
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(30, 60, icon);
        HuntDisplay display = new HuntDisplay();
        display.addLandmark(landmark);
        assertEquals(1, display.landmarks.size());
        
        // Edge case: Adding a landmark with maximum coordinates and null icon
        Landmark landmark2 = new Landmark(90, 180, null);
        display.addLandmark(landmark2);
        assertEquals(2, display.landmarks.size());
        
        // Error scenario: Adding a landmark with invalid coordinates
        ImageIcon icon2 = new ImageIcon("landmark2.png");
        Landmark landmark3 = new Landmark(-100, 200, icon2);
        display.addLandmark(landmark3);
        assertEquals(2, display.landmarks.size());
    }
    
    @Test
    public void testPaintComponent() {
        // Edge case: Testing paintComponent with empty points and landmarks
        HuntDisplay display = new HuntDisplay();
        display.paintComponent(null);
        
        // Typical use case: Testing paintComponent with multiple points and landmarks
        TrigPoint point1 = new TrigPoint(45, 90, 0, false);
        TrigPoint point2 = new TrigPoint(-60, 120, 0, true);
        TrigPoint point3 = new TrigPoint(0, 0, 0, false);
        display.addPoint(point1);
        display.addPoint(point2);
        display.addPoint(point3);
        ImageIcon icon1 = new ImageIcon("landmark1.png");
        ImageIcon icon2 = new ImageIcon("landmark2.png");
        Landmark landmark1 = new Landmark(30, 60, icon1);
        Landmark landmark2 = new Landmark(-45, -90, icon2);
        display.addLandmark(landmark1);
        display.addLandmark(landmark2);
        Graphics g = null; // Since we can't create Graphics object directly
        display.paintComponent(g);
    }
}