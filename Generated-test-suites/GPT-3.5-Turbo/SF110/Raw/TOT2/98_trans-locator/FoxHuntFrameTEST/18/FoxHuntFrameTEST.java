import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.finalPrep();
        assertNotNull(foxHuntFrame.panel3);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();
        assertEquals(0, foxHuntFrame.fox.size());
    }

    @Test
    public void testError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.error("Test Error Message");
    }

    @Test
    public void testActionPerformed() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(1, foxHuntFrame.points.size());
    }
}