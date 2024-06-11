import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();

        TrigPoint point1 = new TrigPoint(45.0, 90.0, 30.0, true);
        display.addPoint(point1);
        assertEquals(1, display.points.size());

        TrigPoint point2 = new TrigPoint(60.0, 120.0, 45.0, false);
        display.addPoint(point2);
        assertEquals(2, display.points.size());
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();

        Landmark landmark1 = new Landmark(30.0, 60.0, new ImageIcon("landmark1.png"));
        display.addLandmark(landmark1);
        assertEquals(1, display.landmarks.size());

        Landmark landmark2 = new Landmark(45.0, 90.0, new ImageIcon("landmark2.png"));
        display.addLandmark(landmark2);
        assertEquals(2, display.landmarks.size());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        display.points.add(new TrigPoint(45.0, 90.0, 30.0, true));
        display.points.add(new TrigPoint(60.0, 120.0, 45.0, false));
        display.landmarks.add(new Landmark(30.0, 60.0, new ImageIcon("landmark1.png")));
        display.landmarks.add(new Landmark(45.0, 90.0, new ImageIcon("landmark2.png")));

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(200, 200));

        Graphics g = panel.getGraphics();
        display.paintComponent(g);

        // Add assertions for the expected behavior of paintComponent method
    }
}