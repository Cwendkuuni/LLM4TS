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
        assertNotNull(optimizer);
        AbstractSimplex simplex = new MultiDirectionalSimplex(2);
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
        double[] startPoint = {1.0, 2.0};
        double[] optimum = optimizer.optimize(1000, new QuadraticFunction(), GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, optimum[0], 0.0001);
        assertEquals(0.0, optimum[1], 0.0001);
    }

    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
        AbstractSimplex simplex = new MultiDirectionalSimplex(2);
        optimizer.parseOptimizationData(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
        double[] startPoint = {1.0, 2.0};
        double[] optimum = optimizer.optimize(1000, new QuadraticFunction(), GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, optimum[0], 0.0001);
        assertEquals(0.0, optimum[1], 0.0001);
    }

    private static class QuadraticFunction implements MultivariateFunction {
        public double value(double[] variables) {
            return variables[0] * variables[0] + variables[1] * variables[1];
        }
    }
}