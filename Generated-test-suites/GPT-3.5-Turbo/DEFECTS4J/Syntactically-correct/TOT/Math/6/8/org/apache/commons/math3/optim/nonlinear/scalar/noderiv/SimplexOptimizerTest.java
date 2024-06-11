import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-10;

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(100, null, GoalType.MINIMIZE, null);
    }

    @Test
    public void testOptimizeMinimize() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 2 * point[0] + point[1];
            }
        };

        double[] startPoint = {1, 1};
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(EPSILON, EPSILON);

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(-1, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeMaximize() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 2 * point[0] + point[1];
            }
        };

        double[] startPoint = {1, 1};
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(EPSILON, EPSILON);

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        PointValuePair result = optimizer.optimize(100, function, GoalType.MAXIMIZE, startPoint);

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(1, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeConvergence() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 2 * point[0] + point[1];
            }
        };

        double[] startPoint = {1, 1};
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(EPSILON, EPSILON) {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return iteration >= 10;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);

        Assert.assertEquals(2, result.getPoint()[0], EPSILON);
        Assert.assertEquals(1, result.getPoint()[1], EPSILON);
        Assert.assertEquals(5, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeLargeProblem() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                double sum = 0;
                for (double p : point) {
                    sum += p;
                }
                return sum;
            }
        };

        double[] startPoint = new double[1000];
        for (int i = 0; i < startPoint.length; i++) {
            startPoint[i] = 1;
        }

        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(EPSILON, EPSILON);

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE, startPoint);

        double expected = startPoint.length;
        Assert.assertEquals(expected, result.getValue(), EPSILON);
    }

}