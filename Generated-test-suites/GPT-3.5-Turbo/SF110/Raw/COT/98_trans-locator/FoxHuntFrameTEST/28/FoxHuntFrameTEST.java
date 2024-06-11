import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

    private static final double DELTA = 0.0001;
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

        Assert.assertEquals(1, frame.points.size());
        Assert.assertEquals(1, frame.display.points.size());
        Assert.assertEquals(0, frame.fox.size());
        Assert.assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointWithMultiplePoints() {
        frame.theta.setText("0");
        frame.lat.setText("35.6895");
        frame.lon.setText("139.6917");
        frame.addPoint();

        frame.theta.setText("90");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        frame.addPoint();

        Assert.assertEquals(2, frame.points.size());
        Assert.assertEquals(2, frame.display.points.size());
        Assert.assertEquals(1, frame.fox.size());
        Assert.assertEquals(1, frame.posModel.getRowCount());
    }

    @Test
    public void testLoadLandmarks() {
        frame.loadLandmarks();

        Assert.assertEquals(3, frame.display.landmarks.size());
    }

    @Test
    public void testTableModel() {
        Assert.assertEquals(2, frame.posModel.getColumnCount());
        Assert.assertEquals(0, frame.posModel.getRowCount());

        frame.fox.add(new TrigPoint(37.7749, -122.4194, 45));

        Assert.assertEquals("Lattitude", frame.posModel.getColumnName(0));
        Assert.assertEquals("Longitude", frame.posModel.getColumnName(1));
        Assert.assertEquals(Double.class, frame.posModel.getColumnClass(0));
        Assert.assertEquals(Double.class, frame.posModel.getColumnClass(1));
        Assert.assertEquals("37.7749", frame.posModel.getValueAt(0, 0));
        Assert.assertEquals("-122.4194", frame.posModel.getValueAt(0, 1));
    }
}