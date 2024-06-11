import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() {
        // Typical use case
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 10);
        assertEquals(5.0, result, 0.0001);
        
        // Edge case
        result = UnivariateRealSolverUtils.solve(f, 3, 7);
        assertEquals(5.0, result, 0.0001);
        
        // Exception handling
        try {
            UnivariateRealSolverUtils.solve(null, 0, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testBracket() {
        // Typical use case
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -10, 10);
        assertEquals(-2.0, result[0], 0.0001);
        assertEquals(2.0, result[1], 0.0001);
        
        // Edge case
        result = UnivariateRealSolverUtils.bracket(f, 50, -10, 10);
        assertEquals(9.0, result[0], 0.0001);
        assertEquals(10.0, result[1], 0.0001);
        
        // Exception handling
        try {
            UnivariateRealSolverUtils.bracket(null, 0, -10, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testMidpoint() {
        // Typical use case
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, result, 0.0001);
        
        // Edge case
        result = UnivariateRealSolverUtils.midpoint(0, -10);
        assertEquals(-5.0, result, 0.0001);
    }
}