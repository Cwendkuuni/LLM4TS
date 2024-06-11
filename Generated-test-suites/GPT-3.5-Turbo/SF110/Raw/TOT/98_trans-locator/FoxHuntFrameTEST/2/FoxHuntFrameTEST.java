import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();

        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.numPoints());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());

        frame.theta.setText("45");
        frame.lat.setText("47.6097");
        frame.lon.setText("-122.3331");
        frame.addPoint();

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.numPoints());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointWithInvalidAngle() {
        frame.theta.setText("invalid");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();

        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.numPoints());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointWithInvalidLatitude() {
        frame.theta.setText("90");
        frame.lat.setText("invalid");
        frame.lon.setText("-122.4194");
        frame.addPoint();

        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.numPoints());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointWithInvalidLongitude() {
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("invalid");
        frame.addPoint();

        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.numPoints());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testFinalPrep() {
        frame.finalPrep();

        assertNotNull(frame.zoom);
        assertNotNull(frame.panel3);
        assertNotNull(frame.panel);

        assertEquals("1", frame.zoom.getText());
    }

    @Test
    public void testTextPrep() {
        frame.textPrep();

        assertNotNull(frame.theta);
        assertNotNull(frame.lat);
        assertNotNull(frame.lon);
        assertNotNull(frame.add);
        assertNotNull(frame.panel1);
    }

    @Test
    public void testTablePrep() {
        frame.tablePrep();

        assertNotNull(frame.posModel);
        assertNotNull(frame.pos);

        assertEquals(0, frame.pos.getRowCount());
    }

    @Test
    public void testDisplayPrep() {
        frame.displayPrep();

        assertNotNull(frame.display);
    }

    @Test
    public void testLoadLandmarks() {
        frame.loadLandmarks();

        assertEquals(0, frame.display.numLandmarks());
    }

    @Test
    public void testError() {
        frame.error("Error message");

        // N/A
        // User interaction test
    }

}