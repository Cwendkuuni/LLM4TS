import org.junit.Test;
import org.junit.Assert;

public class BrentOptimizerTest {

  @Test
  public void testOptimizerInitialization() {
    ConvergenceChecker<UnivariatePointValuePair> checker = null;
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001, checker);
    
    Assert.assertEquals(0.0001, optimizer.getRelativeThreshold(), 0.0001);
    Assert.assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.001);
  }

  @Test
  public void testOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
    
    double result = optimizer.optimize();
    Assert.assertEquals(expectedResult, result, 0.0001);
  }

  @Test
  public void testEmptyInterval() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
    optimizer.setMin(2.0);
    optimizer.setMax(2.0);
    
    double result = optimizer.optimize();
    Assert.assertEquals(2.0, result, 0.0001);
  }

  @Test
  public void testLargeInterval() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
    optimizer.setMin(-1000.0);
    optimizer.setMax(1000.0);
    
    double result = optimizer.optimize();
    Assert.assertEquals(expectedResult, result, 0.0001);
  }

}