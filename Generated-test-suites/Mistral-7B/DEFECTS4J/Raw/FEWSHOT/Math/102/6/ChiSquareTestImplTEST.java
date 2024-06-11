package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {
    @Test
    public void testChiSquare() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double expectedResult = 7.37798383152493;
        double result = test.chiSquare(counts);
        assertEquals(expectedResult, result, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidLength() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        long[] expected = new long[]{3, 2};
        test.chiSquare(observed, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArrayShapes() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {3}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidNonPositiveExpected() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        long[] expected = new long[]{-1, 3};
        test.chiSquare(observed, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidNonNonNegativeObserved() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{-1, 2};
        long[] expected = new long[]{3, 2};
        test.chiSquare(observed, expected);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        long[] expected = new long[]{3, 2};
        double significanceLevel = 0.05;
        double pValue = test.chiSquareTest(expected, observed, significanceLevel);
        assertEquals(0.02582677, pValue, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        long[] expected = new long[]{3};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidNonPositiveExpected() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        long[] expected = new long[]{-1, 3};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestInvalidDegreesOfFreedom() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        long[] expected = new long[]{3, 2};
        test.chiSquareTest(expected, observed);
        test.distribution.dispose(); // Dispose to ensure that the p-value calculation doesn't work
        double pValue = test.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        double significanceLevel = 0.05;
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{3};
        double significanceLevel = 0.05;
        test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidNonNonNegativeObserved() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{-1, 3};
        double significanceLevel = 0.05;
        test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonInvalidDegreesOfFreedom() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        double significanceLevel = 0.05;
        test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        test.distribution.dispose(); // Dispose to ensure that the p-value calculation doesn't work
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
    }
}