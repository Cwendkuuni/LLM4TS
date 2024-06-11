
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {
  
  private Node root;
  
  @Before
  public void setUp() {
    // Create a sample node for testing
    root = new Node(Token.BLOCK);
    Node child1 = new Node(Token.STRING);
    child1.setString("Hello");
    Node child2 = new Node(Token.NAME);
    child2.setLineno(1);
    Node child3 = new Node(Token.NUMBER);
    child3.setDouble(3.14);
    root.addChildToFront(child1);
    root.addChildToBack(child2);
    root.addChildToBack(child3);
  }
  
  @Test
  public void testGetCost() {
    int result = InlineCostEstimator.getCost(root);
    assertEquals(11, result);
  }
  
  @Test
  public void testGetCostWithThreshold() {
    int result = InlineCostEstimator.getCost(root, 10);
    assertEquals(11, result);
  }
  
  @Test
  public void testGetCostWithInvalidRoot() {
    int result = InlineCostEstimator.getCost(null);
    assertEquals(0, result);
  }
  
  @Test
  public void testGetCostWithThresholdExceeded() {
    int result = InlineCostEstimator.getCost(root, 5);
    assertEquals(11, result);
  }
  
  @Test
  public void testGetCostWithEmptyRoot() {
    Node emptyRoot = new Node(Token.BLOCK);
    int result = InlineCostEstimator.getCost(emptyRoot);
    assertEquals(0, result);
  }
  
}
