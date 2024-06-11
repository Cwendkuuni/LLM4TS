import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        
        // Typical use case
        double[] expected = {10.0, 20.0, 30.0};
        long[] observed = {8, 15, 35};
        double result = test.chiSquare(expected, observed);
        assertEquals(7.916666666666667, result, 0.0001);
        
        // Edge case: Empty arrays
        double[] emptyExpected = {};
        long[] emptyObserved = {};
        try {
            test.chiSquare(emptyExpected, emptyObserved);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
        
        // Exception scenario: Negative expected count
        double[] negativeExpected = {10.0, -5.0, 20.0};
        try {
            test.chiSquare(negativeExpected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        
        // Typical use case
        double[] expected = {10.0, 20.0, 30.0};
        long[] observed = {8, 15, 35};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.273347, pValue, 1e-6);
        
        // Edge case: Small array length
        double[] smallExpected = {10.0};
        long[] smallObserved = {5};
        try {
            test.chiSquareTest(smallExpected, smallObserved);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
        
        // Exception scenario: Negative observed count
        long[] negativeObserved = {8, -15, 35};
        try {
            test.chiSquareTest(expected, negativeObserved);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    // Add more test methods for other public methods in ChiSquareTestImpl
    
}