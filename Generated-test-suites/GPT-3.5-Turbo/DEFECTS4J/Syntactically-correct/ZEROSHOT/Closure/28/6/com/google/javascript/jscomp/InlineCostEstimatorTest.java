import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node(1);
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node(1);
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    Node root = new Node(1);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    char lastChar = estimator.getLastChar();
    assertEquals('\0', lastChar);
  }

  @Test
  public void testAppend() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.append("test");
    char lastChar = estimator.getLastChar();
    int cost = estimator.getCost();
    assertEquals('t', lastChar);
    assertEquals(4, cost);
  }

  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("test");
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }
}