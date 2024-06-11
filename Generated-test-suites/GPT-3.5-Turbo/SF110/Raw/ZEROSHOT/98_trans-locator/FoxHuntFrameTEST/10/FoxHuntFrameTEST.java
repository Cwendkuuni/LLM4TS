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
        // Test adding a point with angle, latitude, and longitude
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();

        // Verify the point was added to the points vector
        assertEquals(1, foxHuntFrame.points.size());

        // Verify the point was added to the display
        assertEquals(1, foxHuntFrame.display.getPoints().size());
    }

    @Test
    public void testAddPointWithNoPoints() {
        // Test adding a point with no points in the vector
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();

        // Verify the point was added to the points vector
        assertEquals(1, foxHuntFrame.points.size());
        
        // Verify the point was added to the display
        assertEquals(1, foxHuntFrame.display.getPoints().size());
    }

    @Test
    public void testAddPointWithMultiplePoints() {
        // Add two points to the vector
        foxHuntFrame.points.add(new TrigPoint(37.7749, -122.4194, 45));
        foxHuntFrame.points.add(new TrigPoint(34.0522, -118.2437, 30));

        // Test adding a point with angle, latitude, and longitude
        foxHuntFrame.theta.setText("60");
        foxHuntFrame.lat.setText("38.9072");
        foxHuntFrame.lon.setText("-77.0369");
        foxHuntFrame.addPoint();

        // Verify the point was added to the points vector
        assertEquals(3, foxHuntFrame.points.size());

        // Verify the point was added to the display
        assertEquals(3, foxHuntFrame.display.getPoints().size());
    }

    @Test
    public void testAddPointAndCalculateFox() {
        // Add two points to the vector and fox vector
        foxHuntFrame.points.add(new TrigPoint(37.7749, -122.4194, 45));
        foxHuntFrame.fox.add(new TrigPoint(38.9072, -77.0369));

        // Test adding a point with angle, latitude, and longitude
        foxHuntFrame.theta.setText("60");
        foxHuntFrame.lat.setText("34.0522");
        foxHuntFrame.lon.setText("-118.2437");
        foxHuntFrame.addPoint();

        // Verify the point was added to the points vector
        assertEquals(2, foxHuntFrame.points.size());

        // Verify the point was added to the display
        assertEquals(2, foxHuntFrame.display.getPoints().size());

        // Verify the fox vector was updated
        assertEquals(2, foxHuntFrame.fox.size());
    }

    @Test
    public void testAddPointWithInvalidAngle() {
        // Test adding a point with invalid angle
        foxHuntFrame.theta.setText("abc");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();

        // Verify the point was not added to the points vector
        assertEquals(0, foxHuntFrame.points.size());

        // Verify the point was not added to the display
        assertEquals(0, foxHuntFrame.display.getPoints().size());
    }

    @Test
    public void testAddPointWithInvalidLatitude() {
        // Test adding a point with invalid latitude
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("abc");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();

        // Verify the point was not added to the points vector
        assertEquals(0, foxHuntFrame.points.size());

        // Verify the point was not added to the display
        assertEquals(0, foxHuntFrame.display.getPoints().size());
    }

    @Test
    public void testAddPointWithInvalidLongitude() {
        // Test adding a point with invalid longitude
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("abc");
        foxHuntFrame.addPoint();

        // Verify the point was not added to the points vector
        assertEquals(0, foxHuntFrame.points.size());

        // Verify the point was not added to the display
        assertEquals(0, foxHuntFrame.display.getPoints().size());
    }
}