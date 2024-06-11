import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node("root");
    int expectedCost = 0; // Assuming an empty node has a cost of 0
    assertEquals(expectedCost, InlineCostEstimator.getCost(root));
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node("root");
    int costThreshold = 10;
    int expectedCost = 0; // Assuming an empty node has a cost of 0
    assertEquals(expectedCost, InlineCostEstimator.getCost(root, costThreshold));
  }

  @Test
  public void testCompiledSizeEstimator() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(new Node("test"));
    int expectedCost = 4; // Assuming "test" has a size of 4 characters
    assertEquals(expectedCost, estimator.getCost());
  }
}