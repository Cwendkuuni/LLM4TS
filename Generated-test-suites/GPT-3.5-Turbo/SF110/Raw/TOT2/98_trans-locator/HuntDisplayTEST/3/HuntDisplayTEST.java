import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        HuntDisplay display = new HuntDisplay();
        display.addPoint(point);
        assertEquals(1, display.points.size());
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(landmark);
        assertEquals(1, display.landmarks.size());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(30, 60, 0, true);
        TrigPoint point2 = new TrigPoint(45, 90, Math.PI/2, false);
        Landmark landmark1 = new Landmark(30, 60, new ImageIcon());
        Landmark landmark2 = new Landmark(45, 90, new ImageIcon());
        display.addPoint(point1);
        display.addPoint(point2);
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        display.paintComponent(g);
        // Not assertable due to graphical output
    }
}