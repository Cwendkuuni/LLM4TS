import org.junit.Test;
import org.junit.Assert;

public class HuntDisplayTest {

  @Test
  public void testGetPreferredSize() {
    HuntDisplay display = new HuntDisplay();
    Assert.assertEquals(new Dimension(200, 200), display.getPreferredSize());
  }

  @Test
  public void testGetMinimumSize() {
    HuntDisplay display = new HuntDisplay();
    Assert.assertEquals(new Dimension(50, 50), display.getMinimumSize());
  }

  @Test
  public void testAddPoint() {
    HuntDisplay display = new HuntDisplay();
    TrigPoint point = new TrigPoint(0.0, 0.0, 0.0, true);
    display.addPoint(point);
    // Additional assertions can be added here to check if the point is added successfully
  }

  @Test
  public void testAddLandMark() {
    HuntDisplay display = new HuntDisplay();
    Landmark landmark = new Landmark(0.0, 0.0, new ImageIcon());
    display.addLandMark(landmark);
    // Additional assertions can be added here to check if the landmark is added successfully
  }

  // This method cannot be directly tested as it requires Graphics object
  // We can write an integration test to test its behavior when calling paintComponent

  @Test
  public void integrationTestPaintComponent() {
    HuntDisplay display = new HuntDisplay();
    // Additional setup code can be added here
    // Create a Graphics object and call display.paintComponent(g)
    // Perform assertions to verify the rendered image
  }
}