import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Dimension;

public class HuntDisplayTest {
  
  private HuntDisplay huntDisplay;
  
  @BeforeEach
  public void setUp() {
    huntDisplay = new HuntDisplay();
  }
  
  @Test
  public void testAddPoint() {
    TrigPoint trigPoint = new TrigPoint(0, 0, 0, true);
    huntDisplay.addPoint(trigPoint);
    Vector<TrigPoint> expectedPoints = new Vector<>();
    expectedPoints.add(new TrigPoint(0, 0, 0, true));
    assertEquals(expectedPoints, huntDisplay.points);
  }
  
  @Test
  public void testAddLandMark() {
    Landmark landmark = new Landmark(0, 0, new ImageIcon());
    huntDisplay.addLandMark(landmark);
    Vector<Landmark> expectedLandmarks = new Vector<>();
    expectedLandmarks.add(new Landmark(0, 0, new ImageIcon()));
    assertEquals(expectedLandmarks, huntDisplay.landmarks);
  }
  
  @Test
  public void testGetPreferredSize() {
    Dimension expectedSize = new Dimension(200, 200);
    assertEquals(expectedSize, huntDisplay.getPreferredSize());
  }
  
  @Test
  public void testGetMinimumSize() {
    Dimension expectedSize = new Dimension(50, 50);
    assertEquals(expectedSize, huntDisplay.getMinimumSize());
  }
  
  @Test
  public void testPaintComponentWithNoPointsAndNoLandmarks() {
    Graphics g = null; // Test with null Graphics object
    assertDoesNotThrow(() -> huntDisplay.paintComponent(g));
  }
  
  @Test
  public void testPaintComponentWithPointsOnly() {
    Graphics g = null; // Test with null Graphics object
    huntDisplay.addPoint(new TrigPoint(0, 0, 0, false));
    assertDoesNotThrow(() -> huntDisplay.paintComponent(g));
  }
  
  @Test
  public void testPaintComponentWithLandmarksOnly() {
    Graphics g = null; // Test with null Graphics object
    huntDisplay.addLandMark(new Landmark(0, 0, new ImageIcon()));
    assertDoesNotThrow(() -> huntDisplay.paintComponent(g));
  }
  
  @Test
  public void testPaintComponentWithPointsAndLandmarks() {
    Graphics g = null; // Test with null Graphics object
    huntDisplay.addPoint(new TrigPoint(0, 0, 0, false));
    huntDisplay.addLandMark(new Landmark(0, 0, new ImageIcon()));
    assertDoesNotThrow(() -> huntDisplay.paintComponent(g));
  }
}