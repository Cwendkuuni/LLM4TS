import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, true);
        display.addPoint(point);
        assertEquals(1, display.points.size());
        assertEquals(point, display.points.elementAt(0));
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
        assertEquals(landmark, display.landmarks.elementAt(0));
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics graphics = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        display.paintComponent(graphics);
        
        // Add asserts to verify the expected behavior of paintComponent method
        // ...
    }
}