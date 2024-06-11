import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {
    
    private double[] target;
    private RealMatrix weight;
    private double[] startPoint;
    private GaussNewtonOptimizer optimizer;
    
    @Before
    public void setUp() {
        // Set up the test data
        target = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        
        weight = new Array2DRowRealMatrix(5, 5);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        weight.setEntry(2, 2, 1.0);
        weight.setEntry(3, 3, 1.0);
        weight.setEntry(4, 4, 1.0);
        
        startPoint = new double[]{0.0, 0.0, 0.0};
        
        optimizer = new GaussNewtonOptimizer();
    }
    
    @Test
    public void testDoOptimize() {
        // Test typical use case
        PointVectorValuePair result = optimizer.optimize(target, weight, startPoint);
        double[] optimizedPoint = result.getPoint();
        
        // Check if optimization has converged
        assertTrue(optimizer.hasConverged());
        
        // Check if optimized point is close to target
        for (int i = 0; i < optimizedPoint.length; i++) {
            assertEquals(target[i], optimizedPoint[i], 0.01);
        }
        
        // Test edge case with empty target array
        double[] emptyTarget = new double[0];
        PointVectorValuePair emptyResult = optimizer.optimize(emptyTarget, weight, startPoint);
        double[] emptyOptimizedPoint = emptyResult.getPoint();
        
        // Check if optimization has converged
        assertTrue(optimizer.hasConverged());
        
        // Check if optimized point is empty
        assertEquals(0, emptyOptimizedPoint.length);
        
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullTarget() {
        // Test error scenario with null target array
        optimizer.optimize(null, weight, startPoint);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullWeight() {
        // Test error scenario with null weight matrix
        optimizer.optimize(target, null, startPoint);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSingularMatrix() {
        // Test error scenario with singular matrix
        double[] singularTarget = {1, 1, 1, 1, 1};
        optimizer.optimize(singularTarget, weight, startPoint);
    }
}