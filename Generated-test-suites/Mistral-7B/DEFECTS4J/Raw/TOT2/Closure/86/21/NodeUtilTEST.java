import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = // create a Node instance that represents a FOR-IN loop
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = // create a Node instance that does not represent a FOR-IN loop
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  // Repeat the above test case structure for other methods

  // ...

}