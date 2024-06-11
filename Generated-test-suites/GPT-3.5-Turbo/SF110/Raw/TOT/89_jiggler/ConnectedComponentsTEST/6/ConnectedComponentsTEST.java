import static org.junit.Assert.*;
import org.junit.*;
import jigl.image.*;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
  
  @Test
  public void testConnectedComponents_fourConnected_typicalCase() {
    BinaryImage image = new BinaryImage(3, 3);
    image.set(0, 0, 1);
    image.set(0, 1, 1);
    image.set(1, 1, 1);
    
    ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    GrayImage result = cc.apply(image);
    
    assertEquals(3, result.get(0, 0));
    assertEquals(3, result.get(0, 1));
    assertEquals(3, result.get(1, 1));
  }
  
  @Test
  public void testConnectedComponents_eightConnected_typicalCase() {
    BinaryImage image = new BinaryImage(3, 3);
    image.set(0, 0, 1);
    image.set(0, 1, 1);
    image.set(1, 1, 1);
    
    ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
    GrayImage result = cc.apply(image);
    
    assertEquals(3, result.get(0, 0));
    assertEquals(3, result.get(0, 1));
    assertEquals(3, result.get(1, 1));
  }
  
  @Test(expected = InvalidArgumentException.class)
  public void testConnectedComponents_invalidNumberNeighbors() {
    ConnectedComponents cc = new ConnectedComponents(2, 0);
  }
  
  @Test(expected = InvalidArgumentException.class)
  public void testConnectedComponents_invalidBackgroundColor() {
    ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
  }
  
  // Additional test cases can be added here
  
}