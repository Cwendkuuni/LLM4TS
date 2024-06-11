import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal_typicalCase() {
        // Typical test case where the optimization converges to the optimum.
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        double[] startPoint = {1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        int maxEval = 100;
        PointValuePair result = optimizer.optimizeInternal(maxEval, function, goalType, startPoint);
        
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;
        
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test(expected=NullArgumentException.class)
    public void testDoOptimize_nullSimplex() {
        // Error scenario where the simplex is not set, causing a NullArgumentException.
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

    @Test(timeout=1000)
    public void testDoOptimize_infiniteLoop() {
        // Error scenario where the optimization enters an infinite loop.
        // In this case, we set a timeout of 1000 milliseconds to fail the test if it takes too long.
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                // This function will never converge.
                return Math.sin(point[0]);
            }
        };
        AbstractSimplex simplex = new AbstractSimplex() {
            // We set up a custom simplex that does not converge.
            public void iterate(MultivariateFunction evaluator, Comparator<PointValuePair> comparator) {
                // Do nothing.
            }
        };
        optimizer.setSimplex(simplex);
        
        optimizer.doOptimize();
    }

}