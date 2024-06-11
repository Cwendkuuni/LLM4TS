import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(0.5, 0.5, 0.0, true);
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(point);
        assertEquals(1, huntDisplay.points.size());
        assertEquals(point, huntDisplay.points.elementAt(0));
    }

    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon());
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        assertEquals(landmark, huntDisplay.landmarks.elementAt(0));
    }

    @Test
    public void testPaintComponentWithPoints() {
        TrigPoint point1 = new TrigPoint(0.2, 0.3, 0.0, false);
        TrigPoint point2 = new TrigPoint(0.6, 0.7, 0.0, true);
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        Graphics g = huntDisplay.getGraphics();
        
        // TODO: Validate the painting of the points on the graphics object
        
        // Example assertion:
        // assertEquals(Color.BLACK, someVariableContainingPaintedPointsColor);
        // assertEquals(Color.GREEN, someVariableContainingPaintedFoxColor);
    }

    @Test
    public void testPaintComponentWithLandmarks() {
        Landmark landmark1 = new Landmark(0.2, 0.3, new ImageIcon());
        Landmark landmark2 = new Landmark(0.6, 0.7, new ImageIcon());
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        Graphics g = huntDisplay.getGraphics();

        // TODO: Validate the painting of the landmarks on the graphics object
        
        // Example assertion:
        // assertEquals(2, someVariableContainingNumberOfPaintedLandmarks);
    }
}