
package org.apache.commons.math3.optimization.univariate;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorThrowsNotStrictlyPositiveExceptionWhenAbsIsNegative() {
        new BrentOptimizer(0.01, -0.1);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorThrowsNumberIsTooSmallExceptionWhenRelIsTooSmall() {
        new BrentOptimizer(0.0001, 1E-20);
    }
    
    @Test
    public void testBrentOptimizerConstructorDoesNotThrowWhenRelAndAbsAreValid() {
        new BrentOptimizer(0.0001, 0.1);
    }
    
    @Test
    public void testDoOptimizeReturnsTheBestPointWhenConvergenceCheckerIsNull() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test
    public void testBestReturnsNullWhenBothPointsAreNull() {
        assertNull(BrentOptimizer.best(null, null, true));
    }
    
    @Test
    public void testBestReturnsTheBestPointWhenBothPointsAreNotNull() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.3);
        UnivariatePointValuePair result = BrentOptimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(b, result);
    }
}
