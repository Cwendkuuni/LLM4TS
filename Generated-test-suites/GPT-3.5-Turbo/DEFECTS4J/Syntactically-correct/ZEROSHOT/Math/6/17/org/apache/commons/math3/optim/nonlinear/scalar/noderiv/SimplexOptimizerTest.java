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
                return 0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[0];
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertNotNull(result);
    }
    
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        double[] startPoint = {0, 0, 0};
        simplex.build(startPoint);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        OptimizationData[] optData = {simplex};
        optimizer.parseOptimizationData(optData);
        assertEquals(simplex, optimizer.getSimplex());
    }
    
}