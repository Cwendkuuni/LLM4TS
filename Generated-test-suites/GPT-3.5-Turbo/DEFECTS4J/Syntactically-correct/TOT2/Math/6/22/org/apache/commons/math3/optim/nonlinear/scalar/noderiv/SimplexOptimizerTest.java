import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(new double[] {1.0, 1.0}, 0.5);
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(new double[] {1.0, 1.0}, 0.5);
        optimizer.setSimplex(simplex);
        double[] startPoint = {2.0, 2.0};
        PointValuePair result = optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 3.0, 2) + Math.pow(point[1] - 5.0, 2);
            }
        }, GoalType.MINIMIZE, startPoint);
        assertEquals(3.0, result.getPointRef()[0], 1e-10);
        assertEquals(5.0, result.getPointRef()[1], 1e-10);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        }, GoalType.MAXIMIZE, new double[] {1.0, 1.0});
    }
}