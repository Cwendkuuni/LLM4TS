import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.test.AbstractTestCase;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

/**
 * Test file for SimplexOptimizer class.
 */
public class SimplexOptimizerTest extends AbstractTestCase {

    /**
     * Test for Simplex Optimizer with simple function.
     */
    public void testSimpleFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        MultivariateFunction mf = new MultivariateFunction() {
            public double value(double[] x) {
                return f.value(x[0]);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        PointValuePair result = optimizer.optimize(1000, mf, GoalType.MINIMIZE,
                new double[]{1.5});
        assertEquals("Optimization failed", 1.0, result.getValue(), 1e-5);
    }

    /**
     * Test for Simplex Optimizer with non-simple function.
     */
    public void testNonSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return 1.0 / (sum + 1e-8);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(5);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        PointValuePair result = optimizer.optimize(1000, f, GoalType.MAXIMIZE,
                new double[]{-1.0, -1.0, -1.0, -1.0, -1.0});
        assertEquals("Optimization failed", 1.0, result.getValue(), 1e-5);
    }

    /**
     * Test for Simplex Optimizer with no simplex set.
     */
    public void testNoSimplexSet() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2);
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        try {
            optimizer.optimize(1000, f, GoalType.MINIMIZE, null);
            fail("Exception expected");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test for Simplex Optimizer with invalid goal type.
     */
    public void testInvalidGoalType() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2);
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        try {
            optimizer.optimize(1000, f, GoalType.INVALID, null);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test for Simplex Optimizer with null convergence checker.
     */
    public void testNullConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2);
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(null);
        try {
            optimizer.optimize(1000, f, GoalType.MINIMIZE, null);
            fail("Exception expected");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }
}