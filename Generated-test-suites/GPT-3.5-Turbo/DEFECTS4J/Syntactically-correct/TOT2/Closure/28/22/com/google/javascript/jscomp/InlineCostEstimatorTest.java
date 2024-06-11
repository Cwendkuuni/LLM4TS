import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node();
    root.addChildToFront(new Node(Token.FUNCTION));
    
    int cost = InlineCostEstimator.getCost(root);
    
    assertEquals(18, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node();
    root.addChildToFront(new Node(Token.STRING, new Node(Token.NAME, "test")));
    
    int cost = InlineCostEstimator.getCost(root, 10);
    
    assertEquals(4, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(20);
    Node root = new Node();
    root.addChildToFront(new Node(Token.ADD));
    
    estimator.add(root);
    
    assertEquals(3, estimator.getCost());
  }

  @Test
  public void testCompiledSizeEstimatorAppend() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(20);
    
    estimator.append("var test = 5;");
    
    assertEquals(12, estimator.getCost());
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(20);
    
    estimator.addIdentifier("test");
    
    assertEquals(4, estimator.getCost());
  }
}