import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node("test");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(2, cost);
  }

  @Test
  public void testGetCostWithCostThreshold() {
    Node root = new Node("test");
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(2, cost);
  }

}