import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }
  
  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root, 100);
    assertEquals(0, cost);
  }
  
  @Test
  public void testAdd() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = new Node();
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }
  
  @Test
  public void testGetCostMethod() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
    assertEquals(0, cost);
  }
  
  @Test
  public void testAddIdentifier() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    int expectedCost = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST;
    assertEquals(expectedCost, cost);
  }
  
  @Test
  public void testContinueProcessing() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    boolean continueProcessing = estimator.continueProcessing();
    assertEquals(true, continueProcessing);
  }
  
  @Test
  public void testGetLastChar() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    char lastChar = estimator.getLastChar();
    assertEquals('\0', lastChar);
  }
  
  @Test
  public void testAppend() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.append("test");
    int cost = estimator.getCost();
    assertEquals(4, cost);
  }
  
}