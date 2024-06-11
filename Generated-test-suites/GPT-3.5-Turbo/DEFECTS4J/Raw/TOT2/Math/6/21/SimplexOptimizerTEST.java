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
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        PointValuePair result = optimizer.optimizeInternal(1000, function, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new Simplex());
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        PointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        try {
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

}