import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class HuntDisplayTest {
  
  private HuntDisplay huntDisplay;
  
  @Before
  public void setUp() {
    huntDisplay = new HuntDisplay();
  }
  
  @After
  public void tearDown() {
    huntDisplay = null;
  }
  
  @Test
  public void testAddPoint() {
    TrigPoint point = new TrigPoint(45, 90, 0, true);
    huntDisplay.addPoint(point);
    assertEquals(1, huntDisplay.points.size());
    assertEquals(point, huntDisplay.points.elementAt(0));
    // Add more test cases for typical use cases, edge cases, and error scenarios
  }
  
  @Test
  public void testAddLandmark() {
    Landmark landmark = new Landmark(45, 90, new ImageIcon());
    huntDisplay.addLandmark(landmark);
    assertEquals(1, huntDisplay.landmarks.size());
    assertEquals(landmark, huntDisplay.landmarks.elementAt(0));
    // Add more test cases for typical use cases, edge cases, and error scenarios
  }
  
  @Test
  public void testGetPreferredSize() {
    Dimension expected = new Dimension(200, 200);
    assertEquals(expected, huntDisplay.getPreferredSize());
    // Add more test cases for typical use cases, edge cases, and error scenarios
  }
  
  @Test
  public void testGetMinimumSize() {
    Dimension expected = new Dimension(50, 50);
    assertEquals(expected, huntDisplay.getMinimumSize());
    // Add more test cases for typical use cases, edge cases, and error scenarios
  }
  
  @Test
  public void testPaintComponent() {
    // Add test cases to verify the expected behavior of the paintComponent method
  }
}