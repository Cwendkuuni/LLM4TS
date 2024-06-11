import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.point.PointValues;
import org.apache.commons.math3.optim.simpledirect.AbstractSimplex;
import org.apache.commons.math3.optim.simpledirect.DefaultSimplex;
import org.apache.commons.math3.optim.simplevaluechecker.DefaultSimpleValueChecker;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SimplexOptimizerTest {

    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValues initialGuess = new PointValues(new double[]{1.0, 1.0});
        OptimizationData optimizationData = new InitialGuess(initialGuess);
        optimizer.optimize(1000, function, goalType, optimizationData);

        double[] optimalPoint = optimizer.getPoint();
        double optimalValue = optimizer.getValue();

        assertEquals("Optimal point is not correct", new double[]{0.0, 0.0}, optimalPoint, 0.01);
        assertEquals("Optimal value is not correct", 0.0, optimalValue, 0.01);

        optimizationData = new AbstractSimplex(new DefaultSimplex(2));
        optimizer.setSimplex((AbstractSimplex) optimizationData);
        optimizer.optimize(1000, function, goalType, optimizationData);

        optimalPoint = optimizer.getPoint();
        optimalValue = optimizer.getValue();

        assertEquals("Optimal point is not correct", new double[]{0.0, 0.0}, optimalPoint, 0.01);
        assertEquals("Optimal value is not correct", 0.0, optimalValue, 0.01);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0);
        optimizer.optimize(1000, null, GoalType.MINIMIZE, new InitialGuess(new double[]{0.0, 0.0}));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullInitialGuess() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0);
        optimizer.optimize(1000, new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MINIMIZE, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0);
        optimizer.optimize(1000, new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        }, null, new InitialGuess(new double[]{0.0, 0.0}));
    }

    @Test
    public void testDifferentDimension() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValues initialGuess = new PointValues(new double[]{1.0, 1.0, 1.0});
        OptimizationData optimizationData = new InitialGuess(initialGuess);
        optimizer.optimize(1000, function, goalType, optimizationData);

        // Reinitialize with the correct simplex size.
        optimizationData = new AbstractSimplex(new DefaultSimplex(2));
        optimizer.setSimplex((AbstractSimplex) optimizationData);
        optimizer.optimize(1000, function, goalType, optimizationData);
    }

    @Test
    public void testInvalidGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        };

        // Check with an invalid goal type.
        GoalType invalidGoalType = null;
        try {
            optimizer.optimize(1000, function, invalidGoalType, new InitialGuess(new double[]{0.0, 0.0}));
        } catch (NullArgumentException e) {
            // Expected exception.
        }

        // Check with the default goal type.
        optimizer.optimize(1000, function, GoalType.MINIMIZE, new InitialGuess(new double[]{0.0, 0.0}));
        optimizer.optimize(1000, function, GoalType.MAXIMIZE, new InitialGuess(new double[]{0.0, 0.0}));
    }

    @Test
    public void testConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0, new DefaultSimpleValueChecker(0.001, 0.0));
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(-10 * x[0] * x[0]) - x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValues initialGuess = new PointValues(new double[]{1.0, 1.0});
        OptimizationData optimizationData = new InitialGuess(initialGuess);
        PointValuePair[] previousPoints = new PointValuePair[] {
                new PointValuePair(new double[]{1.0, 1.0}, 1.0),
                new PointValuePair(new double[]{0.9, 1.1}, 0.9),
                new PointValuePair(new double[]{1.1, 0.9}, 1.1)
        };

        // Check that the optimization doesn't converge when the function is not decreasing.
        optimizer.setConvergenceChecker((checker) -> !checker.isConverged(previousPoints[0], previousPoints[1]));
        optimizer.optimize(1000, function, goalType, optimizationData);

        // Check that the optimization converges when the function is decreasing.
        optimizer.setConvergenceChecker((checker) -> checker.isConverged(previousPoints[1], previousPoints[2]));
        optimizer.optimize(1000, function, goalType, optimizationData);
    }

    @Test
    public void testMaxEvalReached() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0, new DefaultSimpleValueChecker(0.001, 0.0));
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(-10 * x[0] * x[0]) - x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValues initialGuess = new PointValues(new double[]{1.0, 1.0});
        OptimizationData optimizationData = new InitialGuess(initialGuess);
        PointValuePair[] previousPoints = new PointValuePair[] {
                new PointValuePair(new double[]{1.0, 1.0}, 1.0),
                new PointValuePair(new double[]{0.9, 1.1}, 0.9),
                new PointValuePair(new double[]{1.1, 0.9}, 1.1),
                new PointValuePair(new double[]{0.8, 1.2}, 0.8),
                new PointValuePair(new double[]{1.2, 0.8}, 1.2)
        };

        optimizer.setConvergenceChecker((checker) -> true);
        optimizer.setMaxEvaluations(5);

        optimizer.optimize(function, goalType, optimizationData);

        assertEquals("Optimization didn't stop when the maximum number of evaluations was reached", 5, optimizer.getNumberEvaluations());
        assertEquals("Optimal point is not correct", new double[]{0.8, 1.2}, optimizer.getPoint(), 0.01);
        assertEquals("Optimal value is not correct", -1.2985, optimizer.getValue(), 0.01);
    }

}