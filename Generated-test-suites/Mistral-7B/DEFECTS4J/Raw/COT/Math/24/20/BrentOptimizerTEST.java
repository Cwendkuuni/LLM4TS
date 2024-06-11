import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test methods for BrentOptimizer constructor

    @Test
    public void testBrentOptimizerConstructorNullChecker() {
        try {
            new BrentOptimizer(Double.NaN, Double.NaN, null);
            fail("Expected NumberIsTooSmallException or NotStrictlyPositiveException.");
        } catch (Exception e) {
            // Expected
        }

        try {
            new BrentOptimizer(-1.0, Double.NaN, null);
            fail("Expected NotStrictlyPositiveException.");
        } catch (Exception e) {
            // Expected
        }

        try {
            new BrentOptimizer(1.0, -1.0, null);
            fail("Expected NotStrictlyPositiveException.");
        } catch (Exception e) {
            // Expected
        }

        try {
            new BrentOptimizer(Double.NaN, 0.0, null);
            fail("Expected NumberIsTooSmallException.");
        } catch (Exception e) {
            // Expected
        }

        try {
            new BrentOptimizer(1.0, 0.0, null);
            fail("Expected NumberIsTooSmallException.");
        } catch (Exception e) {
            // Expected
        }

        try {
            new BrentOptimizer(1.0, 1.0, null);
            fail("Expected null argument exception.");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerConstructor() {
        UnivariateFunction function = new SimpleUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimpleBoundingMethod(0.0, 1.0), new InitialGuess(0.5));
        assertEquals(0.25, result.getPoint(), 1e-6);
    }

    // Test methods for BrentOptimizer optimization

    @Test
    public void testBrentOptimizerMinimum() {
        UnivariateFunction function = new SimpleUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x / 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimpleBoundingMethod(-10, 10), new InitialGuess(0.0));
        assertEquals(-1.275948824933561, result.getPoint(), 1e-5);
    }

    @Test
    public void testBrentOptimizerMaximum() {
        UnivariateFunction function = new SimpleUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x / 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function.negate(), new SimpleBoundingMethod(-10, 10), new InitialGuess(0.0));
        assertEquals(1.275948824933561, result.getPoint(), 1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerBoundingMethodNull() {
        UnivariateFunction function = new SimpleUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, null, new InitialGuess(0.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInitialGuessNull() {
        UnivariateFunction function = new SimpleUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, new SimpleBoundingMethod(0.0, 1.0), null);
    }

    // Test methods for BrentOptimizer optimization with convergence checker

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        UnivariateFunction function = new SimpleUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 0.001;
            }
        };

        PointValuePair result = optimizer.optimize(function, new SimpleBoundingMethod(0.0, 1.0), convergenceChecker, new InitialGuess(0.5));
        assertEquals(0.25, result.getPoint(), 1e-5);
    }
}