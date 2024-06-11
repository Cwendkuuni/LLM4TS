import org.junit.Test;
import java.awt.event.ActionEvent;

public class FoxHuntFrameTest {

    @Test
    public void testActionPerformed() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Add Point");
        foxHuntFrame.actionPerformed(actionEvent);
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("39.123");
        foxHuntFrame.lon.setText("-90.456");
        foxHuntFrame.addPoint();
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.finalPrep();
    }

    @Test
    public void testTextPrep() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.textPrep();
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.tablePrep();
    }

    @Test
    public void testDisplayPrep() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.displayPrep();
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();
    }

    @Test
    public void testError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.error("Test Error");
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame.FoxTableModel foxTableModel = new FoxHuntFrame().new FoxTableModel();
    }
}