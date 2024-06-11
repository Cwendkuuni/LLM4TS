import org.junit.Assert;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root);
    Assert.assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithCostThreshold() {
    Node root = new Node();
    int costThreshold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshold);
    Assert.assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorConstructor() {
    int costThreshold = 10;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshold);
    Assert.assertEquals(costThreshold, estimator.maxCost);
    Assert.assertEquals(0, estimator.cost);
    Assert.assertEquals('\0', estimator.last);
    Assert.assertTrue(estimator.continueProcessing);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    Node root = new Node();
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    Assert.assertEquals(0, estimator.cost);
  }

  @Test
  public void testCompiledSizeEstimatorGetCost() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    int cost = estimator.getCost();
    Assert.assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorContinueProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    boolean continueProcessing = estimator.continueProcessing();
    Assert.assertTrue(continueProcessing);
  }

  @Test
  public void testCompiledSizeEstimatorGetLastChar() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    char lastChar = estimator.getLastChar();
    Assert.assertEquals('\0', lastChar);
  }

  @Test
  public void testCompiledSizeEstimatorAppend() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.append("test");
    Assert.assertEquals(4, estimator.cost);
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.addIdentifier("identifier");
    Assert.assertEquals(2, estimator.cost);
  }
}