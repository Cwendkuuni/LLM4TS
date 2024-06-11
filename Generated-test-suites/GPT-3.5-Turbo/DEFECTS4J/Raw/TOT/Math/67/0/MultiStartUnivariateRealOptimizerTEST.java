import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

  @Test
  public void testGetFunctionValue() {
    // Test Case 1: Test getFunctionValue() when optimizer is null
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, new RandomGenerator());
    Assert.assertEquals(0, optimizer.getFunctionValue(), 0);

    // Test Case 2: Test getFunctionValue() when optimizer is not null
    UnivariateRealOptimizer underlyingOptimizer = new UnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, 1, new RandomGenerator());
    underlyingOptimizer.setFunctionValue(10.0);
    Assert.assertEquals(10.0, optimizer.getFunctionValue(), 0);
  }

  @Test
  public void testGetResult() {
    // Test Case 1: Test getResult() when optimizer is null
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, new RandomGenerator());
    Assert.assertEquals(0.0, optimizer.getResult(), 0);

    // Test Case 2: Test getResult() when optimizer is not null
    UnivariateRealOptimizer underlyingOptimizer = new UnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, 1, new RandomGenerator());
    underlyingOptimizer.setResult(5.0);
    Assert.assertEquals(5.0, optimizer.getResult(), 0);
  }

  // Other test methods for the remaining methods in the class

}