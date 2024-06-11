import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test the constructor with a convergence checker
        // Edge case: convergence checker is null
        BrentOptimizer optimizer = new BrentOptimizer(1, 0, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test the constructor without a convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1, 0);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        // Test the doOptimize method of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1, 0);
        
        // Typical use case
        double result = optimizer.optimize();
        assertEquals(expectedResult, result, 0.0001);
        
        // Edge case: Lower bound
        double lowerBound = Double.NEGATIVE_INFINITY;
        double resultLower = optimizer.optimize(lowerBound);
        assertEquals(expectedResultLower, resultLower, 0.0001);
        
        // Edge case: Upper bound
        double upperBound = Double.POSITIVE_INFINITY;
        double resultUpper = optimizer.optimize(upperBound);
        assertEquals(expectedResultUpper, resultUpper, 0.0001);
        
        // Error scenario: Invalid bounds
        try {
            optimizer.optimize(10, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBest() {
        // Test the best method of BrentOptimizer
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 5);
        
        // Typical use case
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(b, result);
        
        // Edge case: Equal values
        UnivariatePointValuePair c = new UnivariatePointValuePair(3, 5);
        UnivariatePointValuePair resultEqual = optimizer.best(b, c, true);
        assertEquals(b, resultEqual);
        
        // Error scenario: Both inputs are null
        UnivariatePointValuePair resultNull = optimizer.best(null, null, true);
        assertNull(resultNull);
    }
}