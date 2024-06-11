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
        AbstractSimplex simplex = new MockSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(100, new TestFunction(), GoalType.MAXIMIZE);
        assertNotNull(result);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(100, new TestFunction(), GoalType.MAXIMIZE, new InitialGuess(new double[] {0.0, 0.0}));
        assertNotNull(result);
    }

    private static class TestFunction implements MultivariateFunction {
        public double value(double[] point) {
            return point[0] + point[1];
        }
    }

    private static class MockSimplex extends AbstractSimplex {
        public void build(double[] startPoint) {}

        public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {}

        public void iterate(MultivariateFunction f, Comparator<PointValuePair> comparator) {}
    }
}