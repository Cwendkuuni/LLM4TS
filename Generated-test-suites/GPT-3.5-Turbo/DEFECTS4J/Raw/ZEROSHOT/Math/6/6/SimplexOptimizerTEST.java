import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal_linearObjectiveFunction() {
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 3 * point[0] + 2 * point[1] + 5;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = null;

        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);

        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 5.0;
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();

        assertArrayEquals(expectedPoint, actualPoint, 0.0001);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    @Test
    public void testOptimizeInternal_quadraticObjectiveFunction() {
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1] + 1;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = null;

        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);

        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 1.0;
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();

        assertArrayEquals(expectedPoint, actualPoint, 0.0001);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_nullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_validSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex(2);
        simplex.setPoint(0, new double[] {-1.0, -2.0});
        simplex.setPoint(1, new double[] {1.0, 0.0});
        simplex.setPoint(2, new double[] {0.0, 1.0});
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();

        double[] expectedPoint = {0.0, 1.0};
        double expectedValue = 1.0;
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();

        assertArrayEquals(expectedPoint, actualPoint, 0.0001);
        assertEquals(expectedValue, actualValue, 0.0001);
    }
}