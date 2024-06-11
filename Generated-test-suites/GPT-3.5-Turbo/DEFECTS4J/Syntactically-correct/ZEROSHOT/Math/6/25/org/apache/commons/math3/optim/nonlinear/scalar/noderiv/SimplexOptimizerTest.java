import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testOptimize() {
        double[] startPoint = new double[]{0.0, 0.0};
        int maxEval = 100;
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                // Your objective function here
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        OptimizationData[] optData = new OptimizationData[]{
                new AbstractSimplex(new double[][]{
                        new double[]{1.0, 1.0},
                        new double[]{2.0, 2.0},
                        new double[]{3.0, 3.0}
                })
        };

        PointValuePair result = optimizer.optimize(maxEval, function, goalType, startPoint, optData);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getPoint()[1], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);

        startPoint = new double[]{1.0, 1.0};
        optData = new OptimizationData[]{
                new AbstractSimplex(new double[][]{
                        new double[]{1.0, 1.0},
                        new double[]{2.0, 2.0},
                        new double[]{3.0, 3.0}
                })
        };

        result = optimizer.optimize(maxEval, function, goalType, startPoint, optData);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getPoint()[1], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}