import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriterion;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimize;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximize;
import org.apache.commons.math3.optim.simple.AbstractSimplex;
import org.apache.commons.math3.optim.simple.SimplexConvergenceChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test file for the SimplexOptimizer class.
 */
public class SimplexOptimizerTest {
    private static final double EPSILON = 1e-5;
    private AbstractSimplex simplex;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        simplex = new Simplex(3);
        optimizer = new SimplexOptimizer(new SimpleValueChecker(EPSILON, EPSILON));
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testOptimizationSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
            }
        };
        GoalType goalType = new GoalTypeMinimize();
        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        PointValuePair result;

        result = optimizer.optimize(50, f, goalType, new OptimizationData[]{simplex});
        Assert.assertEquals(0.0, result.getValue(), EPSILON);
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), EPSILON);
    }

    /**
     * Test optimization with a discontinuous function.
     */
    @Test
    public void testOptimizationDiscontinuousFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + (x[1] > 0 ? x[1] * x[1] : 0) + x[2] * x[2];
            }
        };
        GoalType goalType = new GoalTypeMinimize();
        double[] startPoint = new double[]{1.0, -1.0, 1.0};
        PointValuePair result;

        result = optimizer.optimize(50, f, goalType, new OptimizationData[]{simplex});
        Assert.assertEquals(0.0, result.getValue(), EPSILON);
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), EPSILON);
    }

    /**
     * Test optimization with a convergence checker.
     */
    @Test
    public void testOptimizationConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.E, -Math.pow(x[0], 2) - Math.pow(x[1], 2) - Math.pow(x[2], 2));
            }
        };
        GoalType goalType = new GoalTypeMinimize();
        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        PointValuePair result;

        simplex.setMaxIterations(50);
        SimplexConvergenceChecker convergenceChecker = new SimplexConvergenceChecker(1e-5, 1e-5);
        optimizer = new SimplexOptimizer(convergenceChecker);

        result = optimizer.optimize(100, f, goalType, new OptimizationData[]{simplex});
        Assert.assertEquals(Math.E, result.getValue(), 1e-3);
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), EPSILON);
    }

    /**
     * Test optimization with a maximum number of evaluations.
     */
    @Test(expected = MaxEvalException.class)
    public void testOptimizationMaxEval() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
            }
        };
        GoalType goalType = new GoalTypeMinimize();
        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        optimizer.optimize(0, f, goalType, new OptimizationData[]{simplex});
    }

    /**
     * Test optimization with a null optimization function.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationNullFunction() {
        optimizer.optimize(50, null, new GoalTypeMinimize(), new OptimizationData[]{simplex});
    }
}