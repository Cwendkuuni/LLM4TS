import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {
    
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
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = {};
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertNull(result);
    }
    
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.build(new double[] {1.0, 2.0, 3.0});
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
}