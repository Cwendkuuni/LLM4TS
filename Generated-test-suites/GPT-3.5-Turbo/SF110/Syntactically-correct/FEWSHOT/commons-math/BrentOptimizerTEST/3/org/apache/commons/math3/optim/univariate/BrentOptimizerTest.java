import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class BrentOptimizerTest {
  
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsNumberIsTooSmallExceptionWhenRelIsLessThanMinRelativeTolerance() {
        new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE - 0.01, 0.1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveExceptionWhenAbsIsZero() {
        new BrentOptimizer(0.5, 0.0);
    }
    
    @Test
    public void testDoOptimizeReturnsUnivariatePointValuePair() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test
    public void testDoOptimizeReturnsCorrectResultForMinimizationGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        optimizer.withGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        double expectedValue = -10.0; // Replace with expected value
        assertEquals(expectedValue, result.getValue(), 0.001);
    }
    
    @Test
    public void testDoOptimizeReturnsCorrectResultForMaximizationGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        optimizer.withGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        double expectedValue = 10.0; // Replace with expected value
        assertEquals(expectedValue, result.getValue(), 0.001);
    }
    
    @Test
    public void testBestReturnsNonNullResultWhenBothArgumentsAreNonNull() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.4);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
    }
    
    @Test
    public void testBestReturnsNonNullResultWhenOneArgumentIsNull() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
    }
    
    @Test
    public void testBestReturnsNullResultWhenBothArgumentsAreNull() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNull(result);
    }
}