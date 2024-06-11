import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1, 1, 0, true);
        huntDisplay.addPoint(point);
        Vector<TrigPoint> points = huntDisplay.points;
        assert(points.contains(point));
    }

    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(1, 1, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> landmarks = huntDisplay.landmarks;
        assert(landmarks.contains(landmark));
    }

    @Test
    public void testPaintComponent() {
        // Test that paintComponent executes without any exception
        Graphics g = new JPanel().getGraphics();
        huntDisplay.paintComponent(g);
    }

    @Test
    public void testGetPreferredSize() {
        Dimension prefSize = huntDisplay.getPreferredSize();
        assert(prefSize.getWidth() == 200.0);
        assert(prefSize.getHeight() == 200.0);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension minSize = huntDisplay.getMinimumSize();
        assert(minSize.getWidth() == 50.0);
        assert(minSize.getHeight() == 50.0);
    }

    @Test
    public void testAddPointNull() {
        huntDisplay.addPoint(null);
        Vector<TrigPoint> points = huntDisplay.points;
        assert(!points.contains(null));
    }

    @Test
    public void testAddLandmarkNull() {
        huntDisplay.addLandMark(null);
        Vector<Landmark> landmarks = huntDisplay.landmarks;
        assert(!landmarks.contains(null));
    }
}