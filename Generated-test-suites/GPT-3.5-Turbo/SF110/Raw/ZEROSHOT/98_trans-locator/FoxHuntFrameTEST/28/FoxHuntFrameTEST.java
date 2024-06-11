import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        // create an instance of FoxHuntFrame
        FoxHuntFrame frame = new FoxHuntFrame();

        // set input values for theta, lat, and lon
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");

        // call the addPoint method
        frame.addPoint();

        // check if the points vector contains the added point
        assertEquals(1, frame.points.size());

        // check if the display has added the point
        assertEquals(1, frame.display.getPoints().size());

        // check if the fox vector has the correct calculated TrigPoint
        assertEquals(1, frame.fox.size());

        // check if the position table has been updated
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals(37.7749, frame.posModel.getValueAt(0, 0));
        assertEquals(-122.4194, frame.posModel.getValueAt(0, 1));
    }

    // Add more test methods for other methods in the FoxHuntFrame class

}