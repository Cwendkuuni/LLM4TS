import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 60, 30, true);
        huntDisplay.addPoint(point);

        // Verify that the point is added to the points vector
        Vector points = huntDisplay.points;
        assert(points.contains(point));
        // Verify that the component is repainted after adding the point
        assert(huntDisplay.getGraphics().getUpdateRect().isEmpty());
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(0, 0, icon);
        huntDisplay.addLandMark(landmark);

        // Verify that the landmark is added to the landmarks vector
        Vector landmarks = huntDisplay.landmarks;
        assert(landmarks.contains(landmark));
        // Verify that the component is repainted after adding the landmark
        assert(huntDisplay.getGraphics().getUpdateRect().isEmpty());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics g = huntDisplay.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, huntDisplay.getWidth(), huntDisplay.getHeight());
        huntDisplay.paintComponent(g);

        // Verify that the component is painted with the correct color
        assert(huntDisplay.getBackground() == Color.BLACK);
    }
}