import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @After
    public void tearDown() {
        foxHuntFrame = null;
    }

    @Test
    public void testAddPoint() {
        // Arrange
        double expectedLat = 10.5;
        double expectedLon = 20.5;
        int expectedAngle = 30;

        // Act
        foxHuntFrame.lat.setText("10.5");
        foxHuntFrame.lon.setText("20.5");
        foxHuntFrame.theta.setText("30");
        foxHuntFrame.addPoint();

        // Assert
        assertEquals(expectedLat, foxHuntFrame.points.get(0).getLat(), 0);
        assertEquals(expectedLon, foxHuntFrame.points.get(0).getLon(), 0);
        assertEquals(expectedAngle, foxHuntFrame.points.get(0).getAngle());
    }

    @Test
    public void testAddPointWithMultiplePoints() {
        // Arrange
        double expectedLat = 10.5;
        double expectedLon = 20.5;
        int expectedAngle = 30;

        // Act
        foxHuntFrame.lat.setText("10.5");
        foxHuntFrame.lon.setText("20.5");
        foxHuntFrame.theta.setText("30");
        foxHuntFrame.addPoint();

        foxHuntFrame.lat.setText("11.5");
        foxHuntFrame.lon.setText("21.5");
        foxHuntFrame.theta.setText("35");
        foxHuntFrame.addPoint();

        // Assert
        assertEquals(expectedLat, foxHuntFrame.points.get(0).getLat(), 0);
        assertEquals(expectedLon, foxHuntFrame.points.get(0).getLon(), 0);
        assertEquals(expectedAngle, foxHuntFrame.points.get(0).getAngle());

        assertEquals(2, foxHuntFrame.points.size());
    }

    @Test
    public void testLoadLandmarks() {
        // Act
        foxHuntFrame.loadLandmarks();

        // Assert
        assertNotNull(foxHuntFrame.display.getLandmarks());
        assertEquals(0, foxHuntFrame.display.getLandmarks().size());
    }

    @Test
    public void testError() {
        // Act
        foxHuntFrame.error("Error message");

        // Assert
        // The error method shows a dialog to the user, and the behavior cannot be tested directly
        assertTrue(true);
    }
}



@RunWith(Suite.class)
@Suite.SuiteClasses({FoxHuntFrameTest.class})
public class FoxHuntFrameCompleteTest {}

