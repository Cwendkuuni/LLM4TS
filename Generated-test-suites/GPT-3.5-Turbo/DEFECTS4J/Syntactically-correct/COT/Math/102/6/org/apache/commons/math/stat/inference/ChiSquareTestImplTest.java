import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {
    
    private ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
    
    @Test
    public void testChiSquare() {
        double[] expected = {1, 2, 3, 4};
        long[] observed = {1, 2, 3, 4};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.0, result, 0.0001); // Basic test with equal expected and observed counts
        
        double[] expected2 = {1, 2, 3, 4};
        long[] observed2 = {1, 1, 1, 1};
        double result2 = chiSquareTest.chiSquare(expected2, observed2);
        assertEquals(5.0, result2, 0.0001); // Basic test with different expected and observed counts
        
        // Add more test cases for edge cases and exception handling scenarios
    }
    
    @Test
    public void testChiSquareTest() throws MathException {
        double[] expected = {1, 2, 3, 4};
        long[] observed = {1, 2, 3, 4};
        double result = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.0, result, 0.0001); // Basic test with equal expected and observed counts
        
        double[] expected2 = {1, 2, 3, 4};
        long[] observed2 = {1, 1, 1, 1};
        double result2 = chiSquareTest.chiSquareTest(expected2, observed2);
        assertEquals(0.9929, result2, 0.0001); // Basic test with different expected and observed counts
        
        // Add more test cases for edge cases and exception handling scenarios
    }
    
    // Continue adding individual test cases for the other methods in the class
    
}