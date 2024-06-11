import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
  
  // Test for the addPoint method
  @Test
  public void testAddPoint() {
    HuntDisplay display = new HuntDisplay();
    TrigPoint point = new TrigPoint(45, 90, 0, false);
    display.addPoint(point);
    
    TrigPoint[] expectedPoints = { new TrigPoint(0.5, 0.5, 0, false), new TrigPoint(0.5, 0.5, 0, false) };
    assertArrayEquals(expectedPoints, display.points.toArray());
  }

  // Test for the addLandMark method
  @Test
  public void testAddLandMark() {
    HuntDisplay display = new HuntDisplay();
    Landmark landmark = new Landmark(45, 90, new ImageIcon());
    display.addLandMark(landmark);
    
    Landmark[] expectedLandmarks = { new Landmark(0.5, 0.5, new ImageIcon()), new Landmark(0.5, 0.5, new ImageIcon()) };
    assertArrayEquals(expectedLandmarks, display.landmarks.toArray());
  }

  // Test for the paintComponent method
  @Test
  public void testPaintComponent() {
    HuntDisplay display = new HuntDisplay();
    // Assuming the TrigPoint and Landmark lists are populated properly
    // Create a mock Graphics object for testing purposes
    Graphics g = new Graphics();
    
    display.paintComponent(g);
    
    // Assert the expected behavior of the paintComponent method
    // based on the provided code
    // ...
  }

  // Test for the getPreferredSize method
  @Test
  public void testGetPreferredSize() {
    HuntDisplay display = new HuntDisplay();
    Dimension expectedSize = new Dimension(200, 200);
    assertEquals(expectedSize, display.getPreferredSize());
  }
  
  // Test for the getMinimumSize method
  @Test
  public void testGetMinimumSize() {
    HuntDisplay display = new HuntDisplay();
    Dimension expectedSize = new Dimension(50, 50);
    assertEquals(expectedSize, display.getMinimumSize());
  }
}

