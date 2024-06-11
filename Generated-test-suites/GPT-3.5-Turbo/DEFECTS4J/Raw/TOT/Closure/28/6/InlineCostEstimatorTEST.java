import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost_WithNullRoot_ReturnZeroCost() {
    Node root = null;
    int expectedCost = 0;
    
    int actualCost = InlineCostEstimator.getCost(root);
    
    assertEquals(expectedCost, actualCost);
  }
  
  @Test
  public void testGetCost_WithSingleNode_ReturnSingleNodeCost() {
    Node root = new Node("test");
    int expectedCost = 4; // Assuming "test" has length 4
    
    int actualCost = InlineCostEstimator.getCost(root);
    
    assertEquals(expectedCost, actualCost);
  }
  
  @Test
  public void testGetCost_WithLargeTree_ReturnTotalCost() {
    Node root = createLargeTree();
    int expectedCost = getTotalCost(root);
    
    int actualCost = InlineCostEstimator.getCost(root);
    
    assertEquals(expectedCost, actualCost);
  }
  
  @Test
  public void testGetCost_WithLargeTreeAndCostThreshold_ReturnPartialCost() {
    Node root = createLargeTree();
    int costThreshold = 100;
    int expectedCost = costThreshold;
    
    int actualCost = InlineCostEstimator.getCost(root, costThreshold);
    
    assertEquals(expectedCost, actualCost);
  }
  
  private Node createLargeTree() {
    // Create a large tree with several nodes
    Node root = new Node("root");
    Node child1 = new Node("child1");
    Node child2 = new Node("child2");
    root.addChildToBack(child1);
    root.addChildToBack(child2);
    // Add more nodes...
    
    return root;
  }
  
  private int getTotalCost(Node node) {
    int cost = node.getString().length();
    for (Node child : node.children()) {
      cost += getTotalCost(child);
    }
    return cost;
  }
}