import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost() {
    // Typical use case
    Node root = new Node(Node.Type.ROOT);
    root.addChildToBack(new Node(Node.Type.VAR));
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(6, cost);
    
    // Edge case - empty root node
    Node emptyRoot = new Node(Node.Type.ROOT);
    int emptyCost = InlineCostEstimator.getCost(emptyRoot);
    assertEquals(0, emptyCost);
  }
  
  @Test
  public void testGetCostWithThreshold() {
    // Typical use case
    Node root = new Node(Node.Type.ROOT);
    root.addChildToBack(new Node(Node.Type.VAR));
    int cost = InlineCostEstimator.getCost(root, 10);
    assertTrue(cost <= 10);
    
    // Edge case - threshold equal to size of root
    Node smallRoot = new Node(Node.Type.ROOT);
    smallRoot.addChildToBack(new Node(Node.Type.VAR));
    int smallCost = InlineCostEstimator.getCost(smallRoot, 6);
    assertTrue(smallCost <= 6);
  }
  
  @Test
  public void testCompiledSizeEstimator() {
    // Typical use case
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    Node root = new Node(Node.Type.ROOT);
    estimator.add(root);
    assertEquals(6, estimator.getCost());
    
    // Edge case - max cost reached
    CompiledSizeEstimator maxEstimator = new CompiledSizeEstimator(5);
    Node rootNode = new Node(Node.Type.ROOT);
    maxEstimator.add(rootNode);
    assertEquals(6, maxEstimator.getCost());
  }
  
  // TODO: Add more test cases for addIdentifier() method
  
  // TODO: Add test cases for continueProcessing() method
  
  // TODO: Add test cases for getLastChar() method
  
  // TODO: Add test cases for append() method
}