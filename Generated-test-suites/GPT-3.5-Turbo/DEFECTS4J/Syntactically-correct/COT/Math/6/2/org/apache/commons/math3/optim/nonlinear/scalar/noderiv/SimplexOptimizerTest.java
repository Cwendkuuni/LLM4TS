
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 100 * (point[1] - point[0] * point[0]) * (point[1] - point[0] * point[0])
                                + (1 - point[0]) * (1 - point[0]);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[0];

        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        Assert.assertEquals(-1.0, result.getPoint()[0], 0.001);
        Assert.assertEquals(1.0, result.getPoint()[1], 0.001);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(new double[] {0.1, 0.2});
        optimizer.setSimplex(simplex);
        Assert.assertEquals(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }
}
