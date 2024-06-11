import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");

        frame.addPoint();

        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.pointList.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        assertEquals(new TrigPoint(37.7749, -122.4194, 45), frame.points.get(0));
        assertEquals(new TrigPoint(37.7749, -122.4194, 45), frame.display.pointList.get(0));
    }

    @Test
    public void testAddPoint_multiplePoints() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");

        frame.addPoint();

        frame.theta.setText("60");
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");

        frame.addPoint();

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.pointList.size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals(new TrigPoint(34.0522, -118.2437, 60), frame.points.get(1));
        assertEquals(new TrigPoint(34.0522, -118.2437, 60), frame.display.pointList.get(1));
        assertEquals(new TrigPoint(34.0522, -118.2437, 60), frame.fox.get(0));
        assertEquals(34.0522, frame.posModel.getValueAt(0, 0));
        assertEquals(-118.2437, frame.posModel.getValueAt(0, 1));
    }

    @Test
    public void testAddPoint_invalidInput() {
        frame.theta.setText("45");
        frame.lat.setText("Invalid latitude");
        frame.lon.setText("-122.4194");

        frame.addPoint();

        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.pointList.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testFinalPrep() {
        assertNotNull(frame.panel3);
        assertNotNull(frame.zoom);
        assertNotNull(frame.display);
        assertNotNull(frame.panel);
    }

    @Test
    public void testTextPrep() {
        assertNotNull(frame.theta);
        assertNotNull(frame.lat);
        assertNotNull(frame.lon);
        assertNotNull(frame.add);
        assertNotNull(frame.panel1);
    }

    @Test
    public void testTablePrep() {
        assertNotNull(frame.posModel);
        assertNotNull(frame.pos);
        assertNotNull(frame.panel2);
    }

    @Test
    public void testDisplayPrep() {
        assertNotNull(frame.display);
        assertEquals(0, frame.display.landmarkList.size());
    }

    @Test
    public void testLoadLandmarks() {
        // Test data
        frame.display.addLandMark(new Landmark(37.7749, -122.4194, new ImageIcon("icon1.gif")));
        frame.display.addLandMark(new Landmark(34.0522, -118.2437, new ImageIcon("icon2.gif")));

        File file = new File("./landmarks.cfg");
        frame.loadLandmarks();

        assertEquals(2, frame.display.landmarkList.size());
        assertEquals(new Landmark(37.7749, -122.4194, new ImageIcon("icon1.gif")), frame.display.landmarkList.get(0));
        assertEquals(new Landmark(34.0522, -118.2437, new ImageIcon("icon2.gif")), frame.display.landmarkList.get(1));
    }

    @Test
    public void testLoadLandmarks_fileNotFound() {
        File file = new File("./invalid_file.cfg");
        frame.loadLandmarks();

        assertEquals(0, frame.display.landmarkList.size());
    }

    @Test
    public void testLoadLandmarks_ioException() {
        File file = new File("./test_invalid_file.cfg");
        frame.loadLandmarks();

        assertEquals(0, frame.display.landmarkList.size());
    }
}