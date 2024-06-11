import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {
    
    @Test
    public void testSolve() {
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 10.0;
        
        try {
            double result = solver.solve(min, max);
            // Basic test case to check if the solver returns a value within the given range
            assertTrue(result >= min && result <= max);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 10.0;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        
        try {
            double result = solver.solve(function, min, max);
            // Basic test case to check if the solver returns a value within the given range
            assertTrue(result >= min && result <= max);
            
            // Additional test case to check if the solver returns the correct root for the given function
            assertEquals(5.0, result, 0.0001);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolveWithInvalidRange() {
        BisectionSolver solver = new BisectionSolver();
        double min = 10.0;
        double max = 0.0; // Invalid range
        
        try {
            double result = solver.solve(min, max);
            fail("Expected an exception to be thrown due to invalid range");
        } catch (MaxIterationsExceededException e) {
    	    // Expected exception for invalid range
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    
    // Add more test cases for potential edge cases and exception handling scenarios
    
    // ...
    
}