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
        AbstractSimplex simplex = new Simplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        PointValuePair result = optimizer.optimizeInternal(100, function, GoalType.MAXIMIZE);
        assertNotNull(result);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
}