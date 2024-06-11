package Joshua.FoxHunt;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.util.Vector;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(30, 45, 60, true);

        HuntDisplay display = new HuntDisplay();
        display.addPoint(point);

        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(point);

        assertEquals(expectedPoints, display.points);
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(30, 45, new ImageIcon());

        HuntDisplay display = new HuntDisplay();
        display.addLandMark(landmark);

        Vector<Landmark> expectedLandmarks = new Vector<>();
        expectedLandmarks.add(landmark);

        assertEquals(expectedLandmarks, display.landmarks);
    }

    @Test
    public void testPaintComponent() {
        TrigPoint point1 = new TrigPoint(30, 45, 60, true);
        TrigPoint point2 = new TrigPoint(60, 90, 120, false);

        Landmark landmark1 = new Landmark(30, 45, new ImageIcon());
        Landmark landmark2 = new Landmark(60, 90, new ImageIcon());

        HuntDisplay display = new HuntDisplay();

        display.addPoint(point1);
        display.addPoint(point2);
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);

        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();

        display.paintComponent(g);

        assertEquals(Color.BLACK, g.getColor());
        // ... Additional assertions for the paintComponent method
    }
}