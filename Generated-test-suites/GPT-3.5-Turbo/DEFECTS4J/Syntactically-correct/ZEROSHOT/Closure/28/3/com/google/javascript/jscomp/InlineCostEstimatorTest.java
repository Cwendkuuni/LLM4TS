import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = createTestNode(); // Create a test Node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(6, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = createTestNode(); // Create a test Node
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(6, cost);
  }

  private Node createTestNode() {
    Node root = new Node();
    // TODO: Add necessary test code to set up the test Node
    return root;
  }
}