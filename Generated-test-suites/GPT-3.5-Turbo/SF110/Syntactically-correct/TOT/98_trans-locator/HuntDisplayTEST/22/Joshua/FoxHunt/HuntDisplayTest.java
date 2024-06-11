import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
  
  @Test
  public void testAddPoint() {
    // Test typical use case
    TrigPoint point = new TrigPoint(1, 1, 0, true);
    HuntDisplay display = new HuntDisplay();
    display.addPoint(point);
    // Verify that the point is added to the list of points
    assertEquals(1, display.points.size());
    assertEquals(point, display.points.elementAt(0));
    // Verify that repaint method is called
    assertEquals(true, display.repaintCalled);
  }
  
  @Test
  public void testAddLandMark() {
    // Test typical use case
    Landmark landmark = new Landmark(1, 1, new ImageIcon());
    HuntDisplay display = new HuntDisplay();
    display.addLandMark(landmark);
    // Verify that the landmark to the list of landmarks
    assertEquals(1, display.landmarks.size());
    assertEquals(landmark, display.landmarks.elementAt(0));
    // Verify that repaint method is called
    assertEquals(true, display.repaintCalled);
  }
  
  @Test
  public void testPaintComponent() {
    // Test typical use case with one point and one landmark
    TrigPoint point = new TrigPoint(1, 1, 0, true);
    Landmark landmark = new Landmark(1, 1, new ImageIcon());
    HuntDisplay display = new HuntDisplay();
    display.addPoint(point);
    display.addLandMark(landmark);
    // Test that the painting is correctly done
    // TODO: implement test case
    
    // Test edge case with no points and landmarks
    display = new HuntDisplay();
    // Test that the painting is correctly done
    // TODO: implement test case
    
    // Test error scenario with null point
    display = new HuntDisplay();
    try {
      display.addPoint(null);
      // Verify that an exception is thrown
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Verify that the exception message is correct
      assertEquals("Point cannot be null", e.getMessage());
    }
    
    // Test error scenario with null landmark
    display = new HuntDisplay();
    try {
      display.addLandMark(null);
      // Verify that an exception is thrown
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Verify that the exception message is correct
      assertEquals("Landmark cannot be null", e.getMessage());
    }
  }
}