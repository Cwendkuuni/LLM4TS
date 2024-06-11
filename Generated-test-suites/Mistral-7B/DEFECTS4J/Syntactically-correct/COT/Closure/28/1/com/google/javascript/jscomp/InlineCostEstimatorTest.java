import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.newObject(); // an empty Node object
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = Node.newObject();
    int costThreshhold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    // The actual cost is not known as it depends on the Node content
    // But we can check that the threshold is respected
    assertTrue(cost <= costThreshhold);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    Node root = Node.newObject();
    int costThreshhold = -1;
    InlineCostEstimator.getCost(root, costThreshhold);
  }

  @Test
  public void testCompiledSizeEstimator() {
    String longString = "This is a long string that will be used to test the CompiledSizeEstimator.";
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(Node.stringLiteral(longString));
    int cost = estimator.getCost();
    assertTrue(cost > longString.length()); // The actual cost is greater than the length of the string
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier1");
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST * 2, cost); // two characters for the identifier and two for the estimator
  }
}