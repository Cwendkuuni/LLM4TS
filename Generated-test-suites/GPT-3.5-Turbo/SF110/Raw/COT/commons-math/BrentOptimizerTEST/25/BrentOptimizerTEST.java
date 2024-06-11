
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {
  
  @Test
  public void testConstructor() {
    double rel = 0.1;
    double abs = 0.01;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;
    
    try {
      BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
      Assert.assertEquals(rel, optimizer.relativeThreshold, 0);
      Assert.assertEquals(abs, optimizer.absoluteThreshold, 0);
    } catch (NumberIsTooSmallException e) {
      Assert.fail("NumberIsTooSmallException should not have been thrown");
    } catch (NotStrictlyPositiveException e) {
      Assert.fail("NotStrictlyPositiveException should not have been thrown");
    }
  }
  
  @Test
  public void testConstructorWithInvalidRel() {
    double rel = -0.1;
    double abs = 0.01;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;
    
    try {
      BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
      Assert.fail("NumberIsTooSmallException should have been thrown");
    } catch (NumberIsTooSmallException e) {
      Assert.assertEquals(rel, e.getArgument(), 0);
      Assert.assertEquals(BrentOptimizer.MIN_RELATIVE_TOLERANCE, e.getMin(), 0);
      Assert.assertTrue(e.getIncludingMin());
    } catch (NotStrictlyPositiveException e) {
      Assert.fail("NotStrictlyPositiveException should not have been thrown");
    }
  }
  
  @Test
  public void testConstructorWithInvalidAbs() {
    double rel = 0.1;
    double abs = 0.0;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;
    
    try {
      BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
      Assert.fail("NotStrictlyPositiveException should have been thrown");
    } catch (NumberIsTooSmallException e) {
      Assert.fail("NumberIsTooSmallException should not have been thrown");
    } catch (NotStrictlyPositiveException e) {
      Assert.assertEquals(abs, e.getArgument(), 0);
    }
  }
  
  @Test
  public void testDoOptimize() {
    // Test the doOptimize method by using known test cases 
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
    optimizer.setMin(0.0);
    optimizer.setStartValue(1.0);
    optimizer.setMax(2.0);
    optimizer.setGoalType(GoalType.MINIMIZE);
    
    UnivariatePointValuePair result = optimizer.doOptimize();
    
    Assert.assertEquals(0.6180339887498948, result.getPoint(), 0.0001);
    Assert.assertEquals(-0.182, result.getValue(), 0.001);
  }
  
  @Test
  public void testBest() {
    BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
    UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 15.0);
    
    UnivariatePointValuePair result = optimizer.best(a, b, true);
    
    Assert.assertEquals(b, result);
  }

}
