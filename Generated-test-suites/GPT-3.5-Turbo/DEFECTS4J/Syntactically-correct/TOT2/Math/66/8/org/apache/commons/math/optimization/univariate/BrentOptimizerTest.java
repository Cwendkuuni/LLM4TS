import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.MathUserException;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 1.0, 3.0, 1.5);
            assertEquals(2.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = optimizer.optimize(function, GoalType.MAXIMIZE, -2.0, 0.0);
            assertEquals(-2.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setEpsilon(-1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setTolerance(-1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        optimizer.setMaximalIterationCount(2);
        optimizer.optimize(function, GoalType.MAXIMIZE, -2.0, 0.0);
    }

    // Add more test cases for edge cases and error scenarios

}