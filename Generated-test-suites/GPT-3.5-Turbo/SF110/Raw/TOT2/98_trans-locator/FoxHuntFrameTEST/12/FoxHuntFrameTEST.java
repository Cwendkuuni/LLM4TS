package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        // Extreme edge case with maximum values
        foxHuntFrame.theta.setText("360");
        foxHuntFrame.lat.setText("90.0");
        foxHuntFrame.lon.setText("180.0");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
    }

    @Test
    public void testLoadLandmarks() {
        // Typical case with existing landmarks file
        foxHuntFrame.loadLandmarks();
        assertEquals(1, foxHuntFrame.display.landmarks.size());
    }

    @Test
    public void testError() {
        // Error scenario with file not found
        foxHuntFrame.loadLandmarks();
        assertEquals(0, foxHuntFrame.display.landmarks.size());
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertEquals(2, foxTableModel.getColumnCount());
    }
}