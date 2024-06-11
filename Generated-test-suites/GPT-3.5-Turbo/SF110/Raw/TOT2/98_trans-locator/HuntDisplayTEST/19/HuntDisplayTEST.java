import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.ImageObserver;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        //Creating a new instance of TrigPoint
        TrigPoint testPoint = new TrigPoint(90.0, 180.0, 45.0, true);
        
        HuntDisplay display = new HuntDisplay();
        display.addPoint(testPoint);
        
        assertEquals(1, display.points.size());
    }

    @Test
    public void testAddLandMark() {
        //Creating a new instance of Landmark
        ImageIcon icon = new ImageIcon("icon.png");
        Landmark testLandmark = new Landmark(90.0, 180.0, icon);
        
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(testLandmark);
        
        assertEquals(1, display.landmarks.size());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = display.getGraphics();
        
        assertNotNull(g);
        
        display.addPoint(new TrigPoint(90.0, 180.0, 45.0, true));
        display.addLandMark(new Landmark(90.0, 180.0, new ImageIcon("icon.png")));
        
        display.paintComponent(g);
        
        // No assertion can be made since this method is void and internally manipulates Graphics object
    }
    
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
}