import Joshua.FoxHunt.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(0, 0, 0, true);
        huntDisplay.addPoint(point);

        Vector points = huntDisplay.points;
        assertEquals(1, points.size());

        TrigPoint addedPoint = (TrigPoint) points.get(0);
        assertEquals(point.getY() / 90.0, addedPoint.getY());
        assertEquals(point.getX() / 180.0, addedPoint.getX());
        assertEquals(point.getTheta(), addedPoint.getTheta());
        assertEquals(point.isFox(), addedPoint.isFox());
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        huntDisplay.addLandMark(landmark);

        Vector landmarks = huntDisplay.landmarks;
        assertEquals(1, landmarks.size());

        Landmark addedLandmark = (Landmark) landmarks.get(0);
        assertEquals(landmark.getLat() / 90.0, addedLandmark.getLat());
        assertEquals(landmark.getLon() / 180.0, addedLandmark.getLon());
        assertEquals(landmark.getIcon(), addedLandmark.getIcon());
    }

    @Test
    public void testPaintComponent() {
        // Test if the paintComponent method executes without any exceptions
        Graphics g = new JPanel().getGraphics();
        huntDisplay.paintComponent(g);

        // No assertion is made as this is a graphical method and can't be compared directly
    }
}