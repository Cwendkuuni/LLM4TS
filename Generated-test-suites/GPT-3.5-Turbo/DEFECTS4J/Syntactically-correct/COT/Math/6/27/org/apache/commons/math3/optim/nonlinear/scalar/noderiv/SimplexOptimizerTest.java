
import org.junit.Test;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {

    // Test optimizeInternal() method using a simple objective function.
    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);

        // Verify that the result is close to the known optimal point.
        double[] expected = {0.0, 0.0};
        double[] actual = result.getPoint();
        double tolerance = 1e-6;
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], actual[i], tolerance);
        }
    }

    // Test setSimplex() method.
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex(2);
        simplex.setPoint(0, new double[]{1.0, 2.0});
        simplex.setPoint(1, new double[]{2.0, 3.0});
        simplex.setPoint(2, new double[]{3.0, 4.0});
        optimizer.setSimplex(simplex);

        AbstractSimplex expected = simplex;
        AbstractSimplex actual = optimizer.getSimplex();
        Assert.assertEquals(expected, actual);
    }

    // Test optimize() method using a simple objective function.
    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(maxEval, f, goalType, startPoint);

        // Verify that the result is close to the known optimal point.
        double[] expected = {0.0, 0.0};
        double[] actual = result.getPoint();
        double tolerance = 1e-6;
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], actual[i], tolerance);
        }
    }
}

