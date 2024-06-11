import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 2);
            assertEquals(0.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown while optimizing with start value.");
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x - 5);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            assertEquals(5.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown while optimizing without start value.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidEps() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(f, GoalType.MAXIMIZE, 0, 10, 5);
        } catch (Exception e) {
            fail("Exception thrown while testing localMin with invalid eps.");
        }
    }

    @Test
    public void testDoOptimize() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.doOptimize();
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Different exception thrown while testing doOptimize.");
        }
    }

}