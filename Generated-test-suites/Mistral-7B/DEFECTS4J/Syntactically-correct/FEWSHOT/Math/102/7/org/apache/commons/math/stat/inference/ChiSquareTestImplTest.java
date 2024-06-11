package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImpl() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNull(test.distribution);
    }

    @Test
    public void testChiSquareTestImplWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, test.distribution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidLengthObserved() {
        long[] observed = new long[1];
        double[] expected = new double[1];
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidLengthExpected() {
        long[] observed = new long[1];
        double[] expected = new double[0];
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidLengthBoth() {
        long[] observed = new long[1];
        double[] expected = new double[2];
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithNegativeExpected() {
        long[] observed = new long[1];
        double[] expected = new double[]{-1.0};
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithNegativeObserved() {
        long[] observed = new long[]{-1};
        double[] expected = new double[1];
        new ChiSquareTestImpl().chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidDegreesOfFreedom() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.distribution.setDegreesOfFreedom(-1.0);
        test.chiSquareTest(new double[1], new long[1]);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithEqualCounts() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{1, 2, 3, 4};
        double sumSq = new ChiSquareTestImpl().chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, sumSq, 0.0);
        double pValue = new ChiSquareTestImpl().chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(1.0, pValue, 0.0001);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithUnequalCounts() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{1, 2, 3};
        double sumSq = new ChiSquareTestImpl().chiSquareDataSetsComparison(observed1, observed2);
        double pValue = new ChiSquareTestImpl().chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(sumSq > 0.0);
        assertTrue(pValue < 0.5);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithZeroCounts() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{0, 0, 0, 0};
        long[] observed2 = new long[]{0, 0, 0, 0};
        double sumSq = new ChiSquareTestImpl().chiSquareDataSetsComparison(observed1, observed2);
        double pValue = new ChiSquareTestImpl().chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0, sumSq, 0.0);
        assertEquals(1.0, pValue, 0.0001);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithZeroCountsAndUnequalLength() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{0, 0, 0};
        long[] observed2 = new long[]{1, 1, 1, 1};
        assertFalse(new ChiSquareTestImpl().chiSquareDataSetsComparison(observed1, observed2) > 0.0);
        assertFalse(new ChiSquareTestImpl().chiSquareTestDataSetsComparison(observed1, observed2) < 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidLengthObserved1() {
        long[] observed1 = new long[1];
        long[] observed2 = new long[1];
        new ChiSquareTestImpl().chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidLengthObserved2() {
        long[] observed1 = new long[1];
        long[] observed2 = new long[2];
        new ChiSquareTestImpl().chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidLengthExpected() {
        long[] observed1 = new long[1];
        long[] observed2 = new long[1];
        new ChiSquareTestImpl().chiSquareTestDataSetsComparison(new double[0], observed1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidLengthExpected1() {
        long[] observed1 = new long[1];
        long[] observed2 = new long[1];
        new ChiSquareTestImpl().chiSquareTestDataSetsComparison(new double[]{1.0}, observed1);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonWithNegativeDegreesOfFreedom() throws IllegalArgumentException {
        long[] observed1 = new long[1];
        long[] observed2 = new long[1];
        new ChiSquareTestImpl().chiSquareTestDataSetsComparison(new double[1], observed1);
        new ChiSquareTestImpl().distribution.setDegreesOfFreedom(-1.0);
    }
}