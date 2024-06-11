import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testConstructorWithDefaultChecker() {
        // Test constructor with default convergence checker
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCustomChecker() {
        // Test constructor with custom convergence checker
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        // Test setSimplex method
        AbstractSimplex simplex = new MockSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        // Assert that simplex is set correctly
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        // Test optimizeInternal method with a simple optimization problem
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {1.0, 2.0};
        double result = optimizer.optimizeInternal(
            100,
            new MultivariateFunction() {
                public double value(double[] point) {
                    return point[0] * point[1];
                }
            },
            GoalType.MINIMIZE,
            new InitialGuess(startPoint)
        ).getPoint()[0];
        // Assert that result is as expected
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testDoOptimize() {
        // Test doOptimize method with a simple optimization problem
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {1.0, 2.0};
        optimizer.setSimplex(new MockSimplex());
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.doOptimize();
        // Assert that result is as expected
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}