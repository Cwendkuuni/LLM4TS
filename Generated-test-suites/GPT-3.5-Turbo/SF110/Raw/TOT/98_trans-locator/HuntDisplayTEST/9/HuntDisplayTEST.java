import static org.junit.Assert.*;
import org.junit.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint_typicalCase() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp = new TrigPoint(45, 60, 0.5, true);
        hd.addPoint(tp);
        // Assertion
        assertEquals(hd.points.elementAt(0), tp);
    }
    
    @Test
    public void testAddPoint_edgeCase() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp1 = new TrigPoint(0, 0, 0, true);
        hd.addPoint(tp1);
        TrigPoint tp2 = new TrigPoint(90, 180, 1, false);
        hd.addPoint(tp2);
        // Assertion
        assertEquals(hd.points.elementAt(0), tp1);
        assertEquals(hd.points.elementAt(1), tp2);
    }
    
    @Test
    public void testAddPoint_errorScenario() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp = null;
        hd.addPoint(tp);
        // Assertion
        assertEquals(hd.points.size(), 0);
    }
    
    @Test
    public void testAddLandmark_typicalCase() {
        HuntDisplay hd = new HuntDisplay();
        Landmark lm = new Landmark(30, -45, new ImageIcon("icon.png"));
        hd.addLandmark(lm);
        // Assertion
        assertEquals(hd.landmarks.elementAt(0), lm);
    }
    
    @Test
    public void testAddLandmark_edgeCase() {
        HuntDisplay hd = new HuntDisplay();
        Landmark lm1 = new Landmark(0, 90, new ImageIcon("icon1.png"));
        hd.addLandmark(lm1);
        Landmark lm2 = new Landmark(45, -180, new ImageIcon("icon2.png"));
        hd.addLandmark(lm2);
        // Assertion
        assertEquals(hd.landmarks.elementAt(0), lm1);
        assertEquals(hd.landmarks.elementAt(1), lm2);
    }
    
    @Test
    public void testAddLandmark_errorScenario() {
        HuntDisplay hd = new HuntDisplay();
        Landmark lm = null;
        hd.addLandmark(lm);
        // Assertion
        assertEquals(hd.landmarks.size(), 0);
    }
    
    @Test
    public void testPaintComponent_typicalCase() {
        HuntDisplay hd = new HuntDisplay();
        Graphics g = new Graphics();
        // Assertion
        assertDoesNotThrow(() -> hd.paintComponent(g));
    }
}