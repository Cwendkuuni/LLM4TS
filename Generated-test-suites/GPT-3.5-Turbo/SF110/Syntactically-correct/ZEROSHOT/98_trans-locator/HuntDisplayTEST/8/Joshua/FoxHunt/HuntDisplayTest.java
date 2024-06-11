
package Joshua.FoxHunt;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.util.Vector;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        Vector points = huntDisplay.points;
        
        assertNotNull(points);
        assertEquals(1, points.size());
        
        TrigPoint addedPoint = (TrigPoint) points.elementAt(0);
        assertEquals(point.getY() / 90.0, addedPoint.getY(), 0.001);
        assertEquals(point.getX() / 180.0, addedPoint.getX(), 0.001);
        assertEquals(point.getTheta(), addedPoint.getTheta());
        assertEquals(point.isFox(), addedPoint.isFox());
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector landmarks = huntDisplay.landmarks;
        
        assertNotNull(landmarks);
        assertEquals(1, landmarks.size());
        
        Landmark addedLandmark = (Landmark) landmarks.elementAt(0);
        assertEquals(landmark.getLat() / 90.0, addedLandmark.getLat(), 0.001);
        assertEquals(landmark.getLon() / 180.0, addedLandmark.getLon(), 0.001);
        assertEquals(landmark.getIcon(), addedLandmark.getIcon());
    }
}
