import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {
    
    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with convergence checker
        // Edge case: Relative threshold equals minimum allowed value
        PowellOptimizer optimizer = new PowellOptimizer(2 * Math.ulp(1d), 1.0, null);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test constructor without convergence checker
        // Typical case: Relative and absolute thresholds set
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.01);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testDoOptimize() {
        // Test the doOptimize method
        // Error scenario: Objective function not properly set
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.01);
        double[] startPoint = new double[] {1.0, 2.0};
        optimizer.setStartPoint(startPoint);
        try {
            optimizer.optimize();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
    
    @Test
    public void testNewPointAndDirection() {
        // Test the newPointAndDirection method
        // Typical case: Check if new point and direction are computed correctly
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.01);
        double[] p = new double[] {1.0, 2.0};
        double[] d = new double[] {0.5, 0.5};
        double alpha = 0.1;
        double[][] result = optimizer.newPointAndDirection(p, d, alpha);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(2, result[1].length);
    }
    
    @Test
    public void testLineSearch() {
        // Test the LineSearch method
        // Edge case: Line search not converged
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.01);
        double[] p = new double[] {1.0, 2.0};
        double[] d = new double[] {0.5, 0.5};
        PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(0.0001, 0.01);
        try {
            lineSearch.search(p, d);
            fail("Expected TooManyEvaluationsException");
        } catch (TooManyEvaluationsException e) {
            // Expected
        }
    }
    
}