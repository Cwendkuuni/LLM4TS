import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("12.345");
        frame.lon.setText("67.890");
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        
        frame.theta.setText("90");
        frame.lat.setText("98.765");
        frame.lon.setText("43.210");
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(0.0, frame.fox.get(0).getX(), 0.001);
        assertEquals(0.0, frame.fox.get(0).getY(), 0.001);
        assertEquals(12.345, frame.fox.get(1).getX(), 0.001);
        assertEquals(67.890, frame.fox.get(1).getY(), 0.001);
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.zoom);
        assertNotNull(frame.zoomL);
        assertNotNull(frame.panel3);
        assertNotNull(frame.display);
        assertNotNull(frame.panel);
    }

    @Test
    public void testTextPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.theta);
        assertNotNull(frame.lat);
        assertNotNull(frame.lon);
        assertNotNull(frame.add);
        assertNotNull(frame.thetaL);
        assertNotNull(frame.latL);
        assertNotNull(frame.lonL);
        assertNotNull(frame.panel1);
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.posModel);
        assertNotNull(frame.pos);
        assertNotNull(frame.panel2);
    }

    @Test
    public void testDisplayPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.display);
        frame.loadLandmarks();
        assertEquals(0, frame.display.landmarks.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(0, frame.display.landmarks.size());
    }

    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Error message");
        // TODO: Add assertion to check the display of error message
    }
}