import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {
  
  // Test getCost() method
  @Test
  public void testGetCost() {
    Node root = new Node();
    root.addChildToFront(new Node(Token.NAME, new Node(Token.STRING))).addChildToBack(new Node(Token.NUMBER));

    int cost = InlineCostEstimator.getCost(root);
  
    assertEquals(8, cost);
  }
  
  // Test getCost() method with costThreshhold
  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node();
    root.addChildToFront(new Node(Token.NAME, new Node(Token.STRING))).addChildToBack(new Node(Token.NUMBER));

    int cost = InlineCostEstimator.getCost(root, 5);
  
    assertEquals(8, cost);
  }
  
  // Test add() method
  @Test
  public void testAdd() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    Node root = new Node();
    Node child1 = new Node(Token.NAME, new Node(Token.STRING));
    Node child2 = new Node(Token.NUMBER);
    root.addChildToFront(child1).addChildToBack(child2);

    estimator.add(root);
  
    assertEquals(8, estimator.getCost());
  }
  
  // Test getLastChar() method
  @Test
  public void testGetLastChar() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    Node root = new Node();
    Node child1 = new Node(Token.NAME, new Node(Token.STRING));
    Node child2 = new Node(Token.NUMBER);
    root.addChildToFront(child1).addChildToBack(child2);

    estimator.add(root);
    char lastChar = estimator.getLastChar();
  
    assertEquals('r', lastChar);
  }
  
  // Test append() method
  @Test
  public void testAppend() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    estimator.append("test");
  
    assertEquals(4, estimator.getCost());
    assertEquals('t', estimator.getLastChar());
  }
  
  // Test addIdentifier() method
  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);

    estimator.addIdentifier("identifier");
  
    assertEquals(2, estimator.getCost());
  }
}