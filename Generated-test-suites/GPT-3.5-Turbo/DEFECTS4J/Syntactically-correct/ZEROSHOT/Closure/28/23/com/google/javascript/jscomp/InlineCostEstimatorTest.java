import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {
  
  private Node testRoot;
  
  @Before
  public void setUp() {
    testRoot = new Node("TestRoot");
  }
  
  @Test
  public void testGetCost() {
    int cost = InlineCostEstimator.getCost(testRoot);
    assertEquals(0, cost);
  }
  
  @Test
  public void testGetCostWithCostThreshold() {
    int cost = InlineCostEstimator.getCost(testRoot, 1000);
    assertEquals(0, cost);
  }
  
  @Test
  public void testGetCostWithCostThresholdExceeded() {
    int cost = InlineCostEstimator.getCost(testRoot, 0);
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorAdd() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1000);
    estimator.add(testRoot);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorGetCost() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1000);
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorAppend() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1000);
    estimator.append("test");
    int cost = estimator.getCost();
    assertEquals(4, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1000);
    estimator.addIdentifier("testIdentifier");
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }
}