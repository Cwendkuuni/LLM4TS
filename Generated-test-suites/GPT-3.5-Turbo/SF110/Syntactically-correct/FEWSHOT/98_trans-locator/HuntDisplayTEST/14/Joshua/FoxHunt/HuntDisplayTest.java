package Joshua.FoxHunt;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HuntDisplayTest {

    @Test
    public void testConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(Color.WHITE, huntDisplay.getBackground());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(45.0, 90.0, 0.0, true);
        huntDisplay.addPoint(point);

        Vector<TrigPoint> points = huntDisplay.getPoints();
        assertEquals(1, points.size());
        TrigPoint addedPoint = points.elementAt(0);
        assertEquals(point.getY() / 90.0, addedPoint.getY(), 0.0001);
        assertEquals(point.getX() / 180.0, addedPoint.getX(), 0.0001);
        assertEquals(point.getTheta(), addedPoint.getTheta(), 0.0001);
        assertTrue(addedPoint.isFox());
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(45.0, 90.0, new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)));
        huntDisplay.addLandMark(landmark);

        Vector<Landmark> landmarks = huntDisplay.getLandmarks();
        assertEquals(1, landmarks.size());
        Landmark addedLandmark = landmarks.elementAt(0);
        assertEquals(landmark.getLat() / 90.0, addedLandmark.getLat(), 0.0001);
        assertEquals(landmark.getLon() / 180.0, addedLandmark.getLon(), 0.0001);
        assertEquals(landmark.getIcon(), addedLandmark.getIcon());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();

        Graphics graphics = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        huntDisplay.paintComponent(graphics);
        //TODO: Add assertions for the paintComponent method
    }
}