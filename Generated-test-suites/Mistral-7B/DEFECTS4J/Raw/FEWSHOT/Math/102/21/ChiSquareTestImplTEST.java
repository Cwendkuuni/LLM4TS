package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Test
    public void testChiSquareTestImpl() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        assertNotNull(chiSquareTestImpl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithEmptyExpectedOrObservedArray() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquare(new double[0], new long[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithIncorrectExpectedOrObservedArrayLengths() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = new long[2];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithNegativeExpectedOrNonPositiveObservedArray() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.0};
        long[] observed = new long[]{1, 2};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethod() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        double chiSquareStatistic = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(5.0, chiSquareStatistic, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithDegreesOfFreedom1() throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(0.1, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithDegreesOfFreedom4() throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed = new long[]{2, 3, 2, 3};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(0.01, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithDegreesOfFreedomNegative() throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed = new long[]{2, 3, 2, 3};
        chiSquareTestImpl.chiSquareTest(expected, observed, -1.0);
    }

    @Test
    public void testChiSquareTestMethodWithAlpha0() throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed = new long[]{2, 3, 2, 3};
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.0);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestMethodWithAlpha05() throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed = new long[]{2, 3, 2, 3};
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.5);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithAlphaMoreThan05() throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed = new long[]{2, 3, 2, 3};
        chiSquareTestImpl.chiSquareTest(expected, observed, 0.6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithEmptyObservedArrays() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithDifferentArrayLengths() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{1, 2, 3};
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethod() {
        chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3};
        long[] observed2 = new long[]{2, 3};
        double chiSquareStatistic = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, chiSquareStatistic, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithDifferentExpectedObservedCounts()
            throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3};
        long[] observed2 = new long[]{3, 2};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.05, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithDifferentExpectedObservedCountsAndAlpha05()
            throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3};
        long[] observed2 = new long[]{3, 2};
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.5);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithDifferentExpectedObservedCountsAndAlpha01()
            throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3};
        long[] observed2 = new long[]{3, 2};
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithNegativeAlpha() throws MathException {
        chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3};
        long[] observed2 = new long[]{3, 2};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, -1.0);
    }
}