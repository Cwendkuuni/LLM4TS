import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double result = optimizer.optimize(100, GoalType.MINIMIZE, 0, 10);
        assertEquals(0.0, result, 1e-6);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(b, result);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorMinRelativeTolerance() {
        new BrentOptimizer(1e-7, 1e-6);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsThreshold() {
        new BrentOptimizer(1e-6, 0.0);
    }
    
    // Add more test cases here for edge cases and error scenarios
    
}