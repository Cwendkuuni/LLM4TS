import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.addPoint(new double[]{1.0});
        simplex.addPoint(new double[]{2.0});
        simplex.addPoint(new double[]{3.0});
        optimizer.setSimplex(simplex);
        
        assertEquals(simplex, optimizer.getSimplex());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }
    
    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.addPoint(new double[]{1.0});
        simplex.addPoint(new double[]{2.0});
        simplex.addPoint(new double[]{3.0});
        optimizer.setSimplex(simplex);
        
        double[] startPoint = new double[]{0.0};
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[]{};
        PointValuePair result = optimizer.optimize(100, f, goalType, optData);
        
        assertTrue(result.getValue() < 0.1);
        assertEquals(0.0, result.getPoint()[0], 0.1);
    }
}