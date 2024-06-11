import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;

    @Test
    public void testDoOptimize() {
        double[] startPoint = {0.0, 0.0};
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;

        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };

        PointValuePair result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, startPoint);

        assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
        assertEquals(expectedValue, result.getValue(), 0.0);
    }

    @Test
    public void testNewPointAndDirection() {
        double[] p = {0.0, 0.0};
        double[] d = {1.0, 1.0};
        double alpha = 0.5;
        double[][] expected = {{0.5, 0.5}, {0.5, 0.5}};

        double[][] result = optimizer.newPointAndDirection(p, d, alpha);

        assertArrayEquals(expected[0], result[0], 0.0);
        assertArrayEquals(expected[1], result[1], 0.0);
    }

    @Test
    public void testSearch() {
        double[] p = {0.0, 0.0};
        double[] d = {1.0, 1.0};
        double expectedValue = 0.0;
        double expectedPoint = 0.0;

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double alpha) {
                double[] x = {p[0] + alpha * d[0], p[1] + alpha * d[1]};
                return x[0] + x[1];
            }
        };

        PointValuePair result = optimizer.search(p, d, f);

        assertEquals(expectedValue, result.getValue(), 0.0);
        assertEquals(expectedPoint, result.getPoint(), 0.0);
    }
}