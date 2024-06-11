
import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(30, 60, 45, true);

        huntDisplay.addPoint(point);
        
        // Assert the point is added correctly
        assertEquals(1, huntDisplay.points.size());
        assertEquals(30/90.0, huntDisplay.points.get(0).getY(), 0.001);
        assertEquals(60/180.0, huntDisplay.points.get(0).getX(), 0.001);
        assertEquals(45, huntDisplay.points.get(0).getTheta());
        assertTrue(huntDisplay.points.get(0).isFox());
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(40, 70, new ImageIcon());

        huntDisplay.addLandMark(landmark);
        
        // Assert the landmark is added correctly
        assertEquals(1, huntDisplay.landmarks.size());
        assertEquals(40/90.0, huntDisplay.landmarks.get(0).getLat(), 0.001);
        assertEquals(70/180.0, huntDisplay.landmarks.get(0).getLon(), 0.001);
        assertEquals(new ImageIcon(), huntDisplay.landmarks.get(0).getIcon());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.points.add(new TrigPoint(0.3, 0.6, 45, true));
        huntDisplay.landmarks.add(new Landmark(0.4, 0.7, new ImageIcon()));
        huntDisplay.setPreferredSize(new Dimension(500, 500));
        huntDisplay.setMinimumSize(new Dimension(100, 100));

        Graphics g = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB).getGraphics();
        huntDisplay.paintComponent(g);

        // Assert that the paint is being correctly executed
        // This is a basic test case, additional test cases for paintComponent could be added
        // by checking the expected result of the image.
    }
}
