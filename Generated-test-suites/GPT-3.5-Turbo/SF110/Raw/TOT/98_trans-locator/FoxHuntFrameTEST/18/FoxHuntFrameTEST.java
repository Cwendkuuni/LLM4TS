import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint_ValidInput_ShouldAddPointToListAndDisplay() {
        // Arrange
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("32.123");
        foxHuntFrame.lon.setText("-75.456");

        // Act
        foxHuntFrame.addPoint();

        // Assert
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.getPointCount());
    }

    @Test
    public void testAddPoint_ValidInput_CheckFoxCalculationAndTableModelUpdate() {
        // Arrange
        foxHuntFrame.points.add(new TrigPoint(30.123, -70.456, 30));

        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("32.123");
        foxHuntFrame.lon.setText("-75.456");

        // Act
        foxHuntFrame.addPoint();

        // Assert
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.getPointCount());

        TrigPoint B = foxHuntFrame.fox.get(0);
        assertEquals(32.343, B.getY(), 0.001);
        assertEquals(-71.276, B.getX(), 0.001);

        assertEquals(2, foxHuntFrame.posModel.getRowCount());
        assertEquals(32.343, foxHuntFrame.posModel.getValueAt(0, 0));
        assertEquals(-71.276, foxHuntFrame.posModel.getValueAt(0, 1));
    }

    @Test
    public void testAddPoint_InvalidAngle_ShouldNotAddPointToListAndDisplay() {
        // Arrange
        foxHuntFrame.theta.setText("abc");
        foxHuntFrame.lat.setText("32.123");
        foxHuntFrame.lon.setText("-75.456");

        // Act
        foxHuntFrame.addPoint();

        // Assert
        assertEquals(0, foxHuntFrame.points.size());
        assertEquals(0, foxHuntFrame.display.getPointCount());
    }

    @Test
    public void testAddPoint_InvalidLatitude_ShouldNotAddPointToListAndDisplay() {
        // Arrange
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("abc");
        foxHuntFrame.lon.setText("-75.456");

        // Act
        foxHuntFrame.addPoint();

        // Assert
        assertEquals(0, foxHuntFrame.points.size());
        assertEquals(0, foxHuntFrame.display.getPointCount());
    }

    @Test
    public void testAddPoint_InvalidLongitude_ShouldNotAddPointToListAndDisplay() {
        // Arrange
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("32.123");
        foxHuntFrame.lon.setText("abc");

        // Act
        foxHuntFrame.addPoint();

        // Assert
        assertEquals(0, foxHuntFrame.points.size());
        assertEquals(0, foxHuntFrame.display.getPointCount());
    }

    @Test
    public void testAddPoint_MultipleValidInputs_ShouldAddMultiplePoints() {
        // Arrange
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("32.123");
        foxHuntFrame.lon.setText("-75.456");

        // Act
        foxHuntFrame.addPoint();

        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("28.123");
        foxHuntFrame.lon.setText("-71.456");

        foxHuntFrame.addPoint();

        // Assert
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.getPointCount());
    }

    @Test
    public void testLoadLandmarks_ValidFile_ShouldAddLandmarksToDisplay() {
        // Act
        foxHuntFrame.loadLandmarks();

        // Assert
        assertEquals(3, foxHuntFrame.display.getLandmarkCount());
    }

    @Test
    public void testLoadLandmarks_InvalidFile_ShouldShowErrorDialogAndExit() {
        // Arrange
        foxHuntFrame.error("Can't Find the file");

        // Assert
        assertEquals(0, foxHuntFrame.display.getLandmarkCount());
    }

    @Test
    public void testLoadLandmarks_ErrorReadingFile_ShouldShowErrorDialogAndExit() {
        // Arrange
        foxHuntFrame.error("Error communicating with the file");

        // Assert
        assertEquals(0, foxHuntFrame.display.getLandmarkCount());
    }

}