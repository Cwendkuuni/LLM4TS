package org.apache.commons.math.stat.inference;
import org.junit.Test;
import org.apache.commons.math.stat.inference.ChiSquareTestImpl;

public class ChiSquareTestImplTest {

    // Test cases for chiSquare method
    
    @Test
    public void testChiSquare_typicalCase() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        double result = test.chiSquare(expected, observed);
        // Assert result here
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_invalidLengths() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2};
        test.chiSquare(expected, observed);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_negativeExpectedCounts() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        test.chiSquare(expected, observed);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_negativeObservedCounts() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, -2, 3};
        test.chiSquare(expected, observed);
    }
    
    // Additional test cases for chiSquareTest, chiSquareTest and chiSquareTestDataSetsComparison methods
    // ...

}