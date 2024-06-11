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
        AbstractSimplex simplex = new SomeConcreteSimplex();
        optimizer.setSimplex(simplex);
        assertSame(simplex, optimizer.getSimplex());
    }
    
    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new SomeMultivariateFunction();
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[]{new SomeOptimizationData()};
        PointValuePair result = optimizer.optimizeInternal(100, function, goalType, optData);
        assertNotNull(result);
    }
    
    @Test
    public void testDoOptimize() {
    	SimplexOptimizer optimizer = new SimplexOptimizer();
    	double[] startPoint = new double[]{1.0, 2.0};
    	optimizer.setStartPoint(startPoint);
    	PointValuePair result = optimizer.doOptimize();
    	assertNotNull(result);
    }
}