
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost() {
    Node root = new Node(Token.OBJECTLIT);
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(2, cost);
  }
  
  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node(Token.OBJECTLIT);
    int cost = InlineCostEstimator.getCost(root, 10);
    assertEquals(2, cost);
  }
  
  @Test
  public void testAddWithValidNode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = new Node(Token.OBJECTLIT);
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }
  
  @Test
  public void testContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    boolean continueProcessing = estimator.continueProcessing();
    assertTrue(continueProcessing);
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
    estimator.append("Hello");
    int cost = estimator.getCost();
    assertEquals(5, cost);
  }
  
  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }
  
}
