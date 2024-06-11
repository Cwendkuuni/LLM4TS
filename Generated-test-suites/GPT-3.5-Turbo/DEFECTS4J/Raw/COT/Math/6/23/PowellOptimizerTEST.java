
import org.junit.Test;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

public class PowellOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Create PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        
        // Set optimization parameters
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        // Test optimization
        PointValuePair result = optimizer.doOptimize();
        
        // Assert the result
        assertEquals(0.0, result.getValue(), 0.0001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0001);
    }
    
    @Test
    public void testNewPointAndDirection() {
        // Create PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        
        // Create test input data
        double[] p = {1.0, 2.0};
        double[] d = {0.5, -0.5};
        double optimum = 0.5;
        
        // Compute new point and direction
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        
        // Assert the result
        assertArrayEquals(new double[]{1.25, 1.75}, result[0], 0.0001);
        assertArrayEquals(new double[]{0.25, -0.25}, result[1], 0.0001);
    }
    
    @Test
    public void testLineSearch() {
        // Create PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        
        // Create test input data
        double[] p = {1.0, 2.0};
        double[] d = {0.5, -0.5};
        
        // Create LineSearch object
        PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(0.0001, 0.0001);
        
        // Perform line search
        UnivariatePointValuePair result = lineSearch.search(p, d);
        
        // Assert the result
        assertEquals(2.0, result.getValue(), 0.0001);
        assertEquals(0.0, result.getPoint(), 0.0001);
    }
}
