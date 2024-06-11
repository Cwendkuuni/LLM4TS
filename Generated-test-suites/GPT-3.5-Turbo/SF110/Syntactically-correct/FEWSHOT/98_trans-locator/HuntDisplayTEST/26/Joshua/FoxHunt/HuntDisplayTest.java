
package Joshua.FoxHunt;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, true);
        display.addPoint(point);

        Vector<TrigPoint> points = display.points;
        assertEquals(1, points.size());
        assertEquals(point, points.elementAt(0));
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandMark(landmark);

        Vector<Landmark> landmarks = display.landmarks;
        assertEquals(1, landmarks.size());
        assertEquals(landmark, landmarks.elementAt(0));
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        display.points.add(new TrigPoint(0, 0, 0, true));
        display.landmarks.add(new Landmark(0, 0, new ImageIcon()));

        Graphics g = new JPanel().getGraphics();
        display.paintComponent(g);

        // Test assertions for painting
    }
}
