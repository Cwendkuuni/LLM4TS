import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        huntDisplay.addPoint(point);
        // Verify that the point was added successfully
        Vector points = huntDisplay.points;
        assert(points.contains(point));
        // Verify that repaint was called
        // ...
    }
    
    @Test
    public void testAddLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        huntDisplay.addLandmark(landmark);
        // Verify that the landmark was added successfully
        Vector landmarks = huntDisplay.landmarks;
        assert(landmarks.contains(landmark));
        // Verify that repaint was called
        // ...
    }
    
    @Test
    public void testPaintComponent() {
        // Create a mock graphics object
        // ...
        HuntDisplay huntDisplay = new HuntDisplay();
        // Set up some test data
        TrigPoint point1 = new TrigPoint(0, 0, 0, false);
        TrigPoint point2 = new TrigPoint(0.5, 0.5, 0, true);
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        // Call the paintComponent method
        // ...
        // Verify that the graphics object was used correctly to draw the points and landmarks
        // ...
    }

}