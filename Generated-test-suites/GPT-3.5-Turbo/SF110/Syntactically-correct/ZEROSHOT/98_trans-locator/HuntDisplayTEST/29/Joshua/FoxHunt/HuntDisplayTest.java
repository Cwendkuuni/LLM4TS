import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import java.util.Vector;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        this.huntDisplay = new HuntDisplay();
    }

    @Test
    public void testGetPreferredSize() {
        Dimension expectedSize = new Dimension(200, 200);
        Dimension actualSize = this.huntDisplay.getPreferredSize();
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension expectedSize = new Dimension(50, 50);
        Dimension actualSize = this.huntDisplay.getMinimumSize();
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90.0, 180.0, 45.0, true);
        this.huntDisplay.addPoint(point);
        Vector<TrigPoint> points = this.huntDisplay.points;
        Assert.assertEquals(1, points.size());
        TrigPoint actualPoint = points.get(0);
        Assert.assertEquals(point.getY() / 90.0, actualPoint.getY(), 0.0001);
        Assert.assertEquals(point.getX() / 180.0, actualPoint.getX(), 0.0001);
        Assert.assertEquals(point.getTheta(), actualPoint.getTheta(), 0.0001);
        Assert.assertEquals(point.isFox(), actualPoint.isFox());
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(90.0, 180.0, new ImageIcon());
        this.huntDisplay.addLandMark(landmark);
        Vector<Landmark> landmarks = this.huntDisplay.landmarks;
        Assert.assertEquals(1, landmarks.size());
        Landmark actualLandmark = landmarks.get(0);
        Assert.assertEquals(landmark.getLat() / 90.0, actualLandmark.getLat(), 0.0001);
        Assert.assertEquals(landmark.getLon() / 180.0, actualLandmark.getLon(), 0.0001);
        Assert.assertEquals(landmark.getIcon(), actualLandmark.getIcon());
    }

    @Test
    public void testPaintComponent() {
        Graphics g = null; // Create mock Graphics object
        this.huntDisplay.paintComponent(g);
        // TODO: Add assertions to test the behavior of the paintComponent method
    }
}