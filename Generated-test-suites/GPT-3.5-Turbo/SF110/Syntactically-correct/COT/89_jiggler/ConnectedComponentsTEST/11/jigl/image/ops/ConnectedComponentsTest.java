
import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

  @Test
  public void testApply() {
    BinaryImage image = new BinaryImage(4, 4);
    image.set(0, 0, 1);
    image.set(0, 1, 1);
    image.set(1, 0, 1);
    image.set(1, 1, 0);
    image.set(2, 2, 1);
    image.set(3, 3, 1);

    ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);

    GrayImage result = cc.apply(image);

    assertEquals(1, result.get(0, 0));
    assertEquals(1, result.get(0, 1));
    assertEquals(1, result.get(1, 0));
    assertEquals(0, result.get(1, 1));
    assertEquals(2, result.get(2, 2));
    assertEquals(3, result.get(3, 3));
  }

  @Test
  public void testConstructorWithValidInputs() {
    try {
      ConnectedComponents cc1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
      assertEquals(ConnectedComponents.FOUR_CONNECTED, cc1.getNeighbors());
      assertEquals(1, cc1.getBackground());
    } catch (Exception e) {
      fail("Exception thrown: " + e);
    }

    try {
      ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
      assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc2.getNeighbors());
      assertEquals(0, cc2.getBackground());
    } catch (Exception e) {
      fail("Exception thrown: " + e);
    }
  }

  @Test(expected = InvalidArgumentException.class)
  public void testConstructorWithInvalidInputs() {
    ConnectedComponents cc = new ConnectedComponents(2, 1);
  }
}
