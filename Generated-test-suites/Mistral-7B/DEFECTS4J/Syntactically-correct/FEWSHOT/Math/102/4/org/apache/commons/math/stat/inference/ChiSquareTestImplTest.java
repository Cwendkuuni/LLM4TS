package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest.getDistribution());
    }

    @Test
    public void testChiSquareTestImplConstructorDistribution() {
        ChiSquaredDistributionImpl distribution = new ChiSquaredDistributionImpl(4);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, chiSquareTest.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidObservedOrExpectedLength() {
        long[] observed = new long[1];
        double[] expected = new double[2];
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidExpectedOrObservedLengthsMismatch() {
        double[] expected = new double[2];
        long[] observed = new long[3];
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidExpectedNegativeOrObservedNonPositive() {
        double[] expected = new double[]{-1, 1};
        long[] observed = new long[]{1, 1};
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestImplChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2, 3, 4};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(7.0 / 3.0, result, 0.001);
    }

    @Test
    public void testChiSquareTestImplChiSquareRescaledExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.5, 0.5};
        long[] observed = new long[]{1, 2, 1, 1}; // sum = 5
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(3.0 / 3.0, result, 0.001);
    }

    @Test
    public void testChiSquareTestImplPValue() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2, 3, 4};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.999999996, pValue, 0.00000001);
    }

    @Test
    public void testChiSquareTestImplPValueRescaledExpected() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.5, 0.5};
        long[] observed = new long[]{1, 2, 1, 1}; // sum = 5
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.975, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestImplAlphaRejectNullHypothesis() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2, 3, 4};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestImplAlphaFailToRejectNullHypothesis() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2, 1, 1}; // sum = 5
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.95);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidAlpha() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2, 3, 4};
        chiSquareTest.chiSquareTest(expected, observed, 1.0);
    }

    @Test
    public void testChiSquareTestImplChiSquareDataSetComparison() throws IllegalArgumentException {
        long[] observed1 = new long[]{1, 3, 3, 4};
        long[] observed2 = new long[]{1, 2, 4, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(3.5, result, 0.001);
    }

    @Test
    public void testChiSquareTestImplPValueDataSetComparison()
            throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{1, 3, 3, 4};
        long[] observed2 = new long[]{1, 2, 4, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.04324, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestImplAlphaRejectNullHypothesisDataSetComparison()
            throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{1, 3, 3, 4};
        long[] observed2 = new long[]{1, 2, 4, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestImplAlphaFailToRejectNullHypothesisDataSetComparison()
            throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{1, 3, 3, 4};
        long[] observed2 = new long[]{1, 2, 4, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.95);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidAlphaDataSetComparison()
            throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{1, 3, 3, 4};
        long[] observed2 = new long[]{1, 2, 4, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 1.0);
    }
}