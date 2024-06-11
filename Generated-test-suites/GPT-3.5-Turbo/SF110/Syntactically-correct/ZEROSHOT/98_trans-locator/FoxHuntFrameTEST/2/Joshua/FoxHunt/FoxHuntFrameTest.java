import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        int initialSize = foxHuntFrame.points.size();

        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("40.123456");
        foxHuntFrame.lon.setText("-80.987654");

        foxHuntFrame.addPoint();

        int newSize = foxHuntFrame.points.size();
        assertFalse(initialSize == newSize);
    }

    @Test
    public void testAddPointWithInvalidInput() {
        int initialSize = foxHuntFrame.points.size();

        foxHuntFrame.theta.setText("invalid");
        foxHuntFrame.lat.setText("invalid");
        foxHuntFrame.lon.setText("invalid");

        foxHuntFrame.addPoint();

        int newSize = foxHuntFrame.points.size();
        assertEquals(initialSize, newSize); // Point should not be added with invalid input
    }

    @Test
    public void testAddPointUpdatesFoxVector() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("40.123456");
        foxHuntFrame.lon.setText("-80.987654");

        foxHuntFrame.addPoint();

        int foxSize = foxHuntFrame.fox.size();
        assertEquals(1, foxSize);
    }

    @Test
    public void testAddPointUpdatesPointModel() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("40.123456");
        foxHuntFrame.lon.setText("-80.987654");

        foxHuntFrame.addPoint();

        int rowCount = foxHuntFrame.posModel.getRowCount();
        assertEquals(1, rowCount);
    }

    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();

        int landmarkCount = foxHuntFrame.display.landmarks.size();
        assertTrue(landmarkCount > 0);
    }
}