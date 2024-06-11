package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(10.22331557960665, chiSquare, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpectedLength() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidObservedLength() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidLengths() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2};
        long[] observed = {2, 3, 3};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegativeExpected() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {-1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegativeObserved() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {-2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.0123456789123, pValue, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidExpectedLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidObservedLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidLengths() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2};
        long[] observed = {2, 3, 3};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegativeExpected() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {-1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegativeObserved() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {-2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        test.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestAlphaReject() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestAlphaAccept() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 2, 1};
        boolean result = test.chiSquareTest(expected, observed, 0.1);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.010123456789123, pValue, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidLengths()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3, 4, 5};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonAlphaReject() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonAlphaAccept() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
        assertTrue(result);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.83098561982506, chiSquare, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidLengths() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3, 4, 5};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }
}