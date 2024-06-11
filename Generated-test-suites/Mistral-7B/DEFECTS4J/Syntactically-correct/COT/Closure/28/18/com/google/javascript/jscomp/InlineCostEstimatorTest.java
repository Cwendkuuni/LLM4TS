import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node(); // create a dummy Node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node(); // create a dummy Node
    int costThreshhold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    assertEquals(0, cost); // The cost should be less than the threshold
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    Node root = new Node(); // create a dummy Node
    int costThreshhold = -10;
    InlineCostEstimator.getCost(root, costThreshhold);
  }

  @Test
  public void testCompiledSizeEstimatorAppendsString() {
    Node root = new Node(); // create a dummy Node
    String str = "testString";
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    estimator.append(str);
    assertEquals(str.length() + 1, estimator.getCost()); // +1 for the last character
  }

  @Test
  public void testCompiledSizeEstimatorAddsIdentifier() {
    Node root = new Node(); // create a dummy Node
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(root);
    estimator.addIdentifier("testIdentifier");
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2, estimator.getCost()); // 2 characters for the identifier
  }
}