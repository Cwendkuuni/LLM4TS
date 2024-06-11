import static org.junit.Assert.*;
import static com.google.javascript.rhino.Node.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = newObject(); // a simple object
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = newObject(); // a simple object
    int costThreshhold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshhold);
    assertEquals(0, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    Node root = newObject(); // a simple object
    int costThreshhold = -1;
    InlineCostEstimator.getCost(root, costThreshhold);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(newNumber(42));
    estimator.add(STRING_LITERAL("test"));
    int actualCost = estimator.getCost();
    int expectedCost = 8;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("abc");
    int actualCost = estimator.getCost();
    int expectedCost = 3;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testCompiledSizeEstimatorMaxCostReached() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(3);
    estimator.add(STRING_LITERAL("test"));
    estimator.addIdentifier("def");
    estimator.addIdentifier("ghi");
    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testCompiledSizeEstimatorContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(STRING_LITERAL("test"));
    estimator.addIdentifier("def");
    estimator.addIdentifier("ghi");
    assertTrue(estimator.continueProcessing());
  }
}