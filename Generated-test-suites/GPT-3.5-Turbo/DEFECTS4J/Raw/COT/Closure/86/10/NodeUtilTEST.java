import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    NodeUtil util = new NodeUtil();
    
    // Test case 1: valid FOR-IN loop
    Node forInLoop = createForInLoop();
    assertTrue(util.isForIn(forInLoop));
    
    // Test case 2: invalid FOR-IN loop
    Node notForInLoop = createNotForInLoop();
    assertFalse(util.isForIn(notForInLoop));
  }
  
  private Node createForInLoop() {
    // create and return a valid FOR-IN loop node
  }
  
  private Node createNotForInLoop() {
    // create and return a node that is not a FOR-IN loop
  }
}