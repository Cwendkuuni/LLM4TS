import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.AbstractSimplex;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimizer() {
        // Test with a simple function.
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        AbstractSimplex simplex = new SimpleSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE, new double[]{2.0, 2.0});
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1000, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MINIMIZE, null);
    }

    @Test
    public void testOptimizerWithStartPoint() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }
        };

        AbstractSimplex simplex = new SimpleSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1000, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MAXIMIZE, new double[]{0.0});
    }
}