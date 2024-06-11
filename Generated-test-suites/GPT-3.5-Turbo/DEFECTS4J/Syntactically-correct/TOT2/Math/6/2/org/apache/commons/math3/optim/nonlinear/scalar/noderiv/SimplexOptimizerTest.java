import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testSimplexSetter() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex(2);
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] variables) {
                return variables[0] + variables[1];
            }
        };
        PointValuePair result = optimizer.optimizeInternal(100, function, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimizeInternal(100, s -> s[0] * s[1], GoalType.MAXIMIZE);
        assertEquals(30.0, result.getValue(), 0.001);
    }
}