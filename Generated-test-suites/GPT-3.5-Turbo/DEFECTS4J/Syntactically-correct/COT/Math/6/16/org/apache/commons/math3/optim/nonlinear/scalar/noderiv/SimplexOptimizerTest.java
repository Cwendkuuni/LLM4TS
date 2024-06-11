
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {
    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[] { new AbstractSimplex() };
        PointValuePair result = optimizer.optimizeInternal(100, f, goalType, optData);
        double[] expectedPoint = { 0.0, 0.0 };
        assertEquals(expectedPoint, result.getPoint(), 0.0001);
        double expectedValue = 0.0;
        assertEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.build(new double[]{1.0, 1.0, 1.0});
        optimizer.setSimplex(simplex);
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.doOptimize();
        double[] expectedPoint = { 0.0, 0.0 };
        assertEquals(expectedPoint, result.getPoint(), 0.0001);
        double expectedValue = 0.0;
        assertEquals(expectedValue, result.getValue(), 0.0001);
    }
}
