import org.junit.Test;

import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithIncorrectLengths() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2};
        
        chiSquareTest.chiSquare(expected, observed);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeObservedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2, 3};
        
        chiSquareTest.chiSquare(expected, observed);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithZeroExpectedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 0.0};
        long[] observed = {1, 2, 3};
        
        chiSquareTest.chiSquare(expected, observed);
    }
    
    @Test
    public void testChiSquareTest() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        
        double result = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(1.0, result, 0.0001);
    }
    
    // Similar tests for other methods 
    
    // ... 

}