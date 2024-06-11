import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {

    /**
     * Test function for optimization.
     */
    private static final UnivariateRealFunction testFunction = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + 0.1 * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x) + 0.1;
        }
    };

    /**
     * Test optimization with given bounds and initial value.
     */
    @Test
    public void testOptimization() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double minimum = optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
            Assert.assertEquals(-1.4142135625, minimum, 1E-8);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test optimization without bounds.
     */
    @Test
    public void testOptimizationWithoutBounds() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double minimum = optimizer.optimize(testFunction, GoalType.MINIMIZE, null, null, 0.0);
            Assert.assertEquals(-1.4142135625, minimum, 1E-8);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test optimization with function that always returns the same value.
     */
    @Test
    public void testOptimizationWithConstantFunction() {
        UnivariateRealFunction constantFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 5.0;
            }

            @Override
            public double derivative(double x) {
                return 0.0;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double minimum = optimizer.optimize(constantFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
            Assert.assertEquals(5.0, minimum, 1E-8);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test optimization with function that throws an exception.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizationWithNegativeAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.01);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    /**
     * Test optimization with function that throws an exception.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizationWithNegativeStepSize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(-1);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }
}