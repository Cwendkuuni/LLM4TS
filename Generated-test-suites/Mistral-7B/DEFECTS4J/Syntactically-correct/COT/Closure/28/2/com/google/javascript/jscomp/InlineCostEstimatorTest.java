import static org.junit.Assert.*;
import static com.google.javascript.rhino.Node.*;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    InlineCostEstimator.getCost(null);
    Node root = newObject();
    InlineCostEstimator.getCost(root);
  }

  @Test
  public void testGetCostWithThreshold() {
    int costThreshhold = 10;
    InlineCostEstimator.getCost(newObject(), costThreshhold);
  }

  @Test(expected = NullPointerException.class)
  public void testGetCostWithNullRootAndThreshold() {
    InlineCostEstimator.getCost(null, 10);
  }

  @Test(expected = NullPointerException.class)
  public void testGetCostWithNullRoot() {
    InlineCostEstimator.getCost(null);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(newObject());
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier");
  }

  @Test
  public void testCompiledSizeEstimatorMaxCostReached() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(1);
    for (int i = 0; i < 100; i++) {
      estimator.add(newObject());
    }
    assertFalse(estimator.continueProcessing());
  }
}