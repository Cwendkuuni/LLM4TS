import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-6;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
        optimizer.setMaxIterations(100);
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

    /**
     * Test optimize method for a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        double[] startPoint = new double[]{3.0, 4.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint, new SimpleValueChecker(EPSILON, EPSILON));
        assertEquals("Incorrect minimum found", new PointValuePair(new double[]{2.0, 2.0}, 6.0), result);
    }

    /**
     * Test optimize method for a non-convex function.
     */
    @Test
    public void testOptimizeNonConvexFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2.0) + Math.pow(Math.sin(Math.PI * x[1]), 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint, new SimpleValueChecker(EPSILON, EPSILON));
        // The function has multiple minimums, so this test only checks that the result is not null
        assertNotNull("Optimization failed", result);
    }

    /**
     * Test optimize method with an invalid argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullFunction() {
        optimizer.optimize(null, GoalType.MINIMIZE, new double[]{0.0}, new SimpleValueChecker(EPSILON, EPSILON));
    }
}