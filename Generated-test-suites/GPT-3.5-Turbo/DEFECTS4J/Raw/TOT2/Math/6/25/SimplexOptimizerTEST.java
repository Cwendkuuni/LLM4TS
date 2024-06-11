import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSimplexOptimizerConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(2);
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {1.0, 2.0, 3.0};
        MultivariateFunction f = new QuadraticFunction();
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = {new InitialGuess(startPoint)};
        PointValuePair result = optimizer.optimizeInternal(1000, f, goalType, optData);
        assertNotNull(result);
        assertEquals(0.0, f.value(result.getPoint()), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }
}