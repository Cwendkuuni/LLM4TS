import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node("root");
    int result = InlineCostEstimator.getCost(root);
    assertEquals(0, result);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node("root");
    int result = InlineCostEstimator.getCost(root, 10);
    assertEquals(0, result);
  }

  @Test
  public void testCompiledSizeEstimator() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(new Node("root"));
    int result = estimator.getCost();
    assertEquals(0, result);
  }

}