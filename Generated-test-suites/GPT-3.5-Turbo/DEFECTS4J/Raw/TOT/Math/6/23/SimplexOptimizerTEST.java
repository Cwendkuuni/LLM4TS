import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.build(new double[] {1.0, 2.0, 3.0});
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }
    
    @Test(expected=NullArgumentException.class)
    public void testSetSimplexNullArg() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }
    
    @Test
    public void testOptimizeInternal_minimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair expected = new PointValuePair(new double[] {0.0}, 0.0);
        
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testOptimizeInternal_maximize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return -point[0] * point[0];
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair expected = new PointValuePair(new double[] {0.0}, 0.0);
        
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);
        
        assertEquals(expected, result);
    }
    
    @Test(expected=NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        
        optimizer.doOptimize();
    }
    
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.build(new double[] {1.0, 2.0, 3.0});
        optimizer.setSimplex(simplex);
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair expected = new PointValuePair(new double[] {0.0}, 0.0);
        
        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(expected, result);
    }
}