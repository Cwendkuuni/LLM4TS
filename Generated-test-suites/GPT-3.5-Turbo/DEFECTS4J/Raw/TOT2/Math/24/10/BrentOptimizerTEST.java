import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        // Test constructor with convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutChecker() {
        // Test constructor without convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        // Test constructor with invalid relative threshold
        new BrentOptimizer(1e-10, 1e-6, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        // Test constructor with invalid absolute threshold
        new BrentOptimizer(1e-6, -1e-6, null);
    }

    @Test
    public void testDoOptimize() {
        // Test doOptimize method

        // Create a mock univariate optimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null) {
            @Override
            protected UnivariatePointValuePair doOptimize() {
                // Mock implementation
                return new UnivariatePointValuePair(0.0, 0.0);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(100, GoalType.MINIMIZE, 0.0, 1.0);

        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testBest() {
        // Test best method

        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 5.0);

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        UnivariatePointValuePair bestPoint = optimizer.best(pointA, pointB, true);

        assertNotNull(bestPoint);
        assertEquals(2.0, bestPoint.getPoint(), 0.0);
        assertEquals(5.0, bestPoint.getValue(), 0.0);
    }

}