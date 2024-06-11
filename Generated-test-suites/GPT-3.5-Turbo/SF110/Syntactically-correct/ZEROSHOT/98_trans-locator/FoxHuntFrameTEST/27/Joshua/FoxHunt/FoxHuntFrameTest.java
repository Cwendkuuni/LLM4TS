import org.junit.Test;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("122.4194");
        frame.add.doClick();

        // assert statements

    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();

        // assert statements

    }

}