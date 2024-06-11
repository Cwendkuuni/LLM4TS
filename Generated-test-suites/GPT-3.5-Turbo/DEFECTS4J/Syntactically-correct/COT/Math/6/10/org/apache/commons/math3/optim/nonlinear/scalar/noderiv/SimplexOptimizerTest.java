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
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(100, function, goalType);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint()[0], 1e-5);
        assertEquals(0.0, result.getPoint()[1], 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.parseOptimizationData(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.sin(point[0]) + Math.cos(point[1]);
            }
        };
        double[] startPoint = {0.0, 0.0};
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(Math.PI/2, result.getPoint()[0], 1e-5);
        assertEquals(0.0, result.getPoint()[1], 1e-5);
        assertEquals(1.0, result.getValue(), 1e-5);
    }

}