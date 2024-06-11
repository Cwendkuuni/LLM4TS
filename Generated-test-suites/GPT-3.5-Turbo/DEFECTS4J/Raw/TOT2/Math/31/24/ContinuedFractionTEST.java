import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    // Define test cases for getA method
    @Test
    public void testGetA() {
        // Test case for a positive value
        double result = Math3ContinuedFraction.getA(2, 0.5);
        assertEquals(5.0, result, 0.0001);
        
        // Test case for a negative value
        double result = Math3ContinuedFraction.getA(3, -1.0);
        assertEquals(-7.0, result, 0.0001);
    }

    // Define test cases for getB method
    @Test
    public void testGetB() {
        // Test case for a positive value
        double result = Math3ContinuedFraction.getB(1, 1.5);
        assertEquals(3.0, result, 0.0001);
        
        // Test case for a negative value
        double result = Math3ContinuedFraction.getB(4, -2.0);
        assertEquals(-4.0, result, 0.0001);
    }

    // Define test cases for evaluate method
    @Test
    public void testEvaluate() {
        // Test case for a typical evaluation
        double result = Math3ContinuedFraction.evaluate(2.0);
        assertEquals(3.0, result, 0.0001);
        
        // Test case for an edge case with close to zero x
        double result = Math3ContinuedFraction.evaluate(0.00001);
        assertEquals(1.0, result, 0.0001);
        
        // Test case for an error scenario with maxIterations reached
        try {
            double result = Math3ContinuedFraction.evaluate(10.0, 10e-9, 5);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // test passes
        }
        
        // Test case for an error scenario with non-convergence
        try {
            double result = Math3ContinuedFraction.evaluate(1.0/3, 10e-9, 100);
            fail("Expected ConvergeException");
        } catch (ConvergenceException e) {
            // test passes
        }
    }
}