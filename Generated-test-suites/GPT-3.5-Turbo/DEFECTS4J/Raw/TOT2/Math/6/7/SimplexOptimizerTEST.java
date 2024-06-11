import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {
    
    @Test
    public void testOptimizeInternal() {
        // Test basic optimization functionality
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        PointValuePair result = optimizer.optimizeInternal(1000, function, GoalType.MINIMIZE);
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-10);
        assertEquals(expectedValue, result.getValue(), 1e-10);
    }
    
    @Test
    public void testDoOptimize() {
        // Test optimization with known solution
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2, 2) + Math.pow(point[1] - 3, 2);
            }
        };
        PointValuePair result = optimizer.optimizeInternal(1000, function, GoalType.MINIMIZE);
        double[] expectedPoint = {2.0, 3.0};
        double expectedValue = 0.0;
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-10);
        assertEquals(expectedValue, result.getValue(), 1e-10);
    }
    
    @Test
    public void testSetSimplex() {
        // Test setting simplex
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        // Test optimization with null simplex
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }
}