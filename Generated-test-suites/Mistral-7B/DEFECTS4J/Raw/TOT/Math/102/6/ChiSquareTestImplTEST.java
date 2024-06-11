package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test simple case with positive and non-zero expected and observed counts.
     */
    @Test
    public void testSimpleCase() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {2.5, 3.5, 1.5, 4.5};
        try {
            double chiSquareStat = test.chiSquare(expected, observed);
            double pValue = test.chiSquareTest(expected, observed);
            Assert.assertEquals("Wrong chi-square statistic", 6.0, chiSquareStat, 0.001);
            Assert.assertEquals("Wrong p-value", 0.0123, pValue, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test edge case with zero observed count for at least one category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroObservedCount() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 0, 1, 4};
        double[] expected = {2.5, 3.5, 1.5, 4.5};
        test.chiSquare(expected, observed);
    }

    /**
     * Test error case with non-positive expected counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExpectedCounts() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {-1.5, 3.5, 1.5, 4.5};
        test.chiSquare(expected, observed);
    }

    /**
     * Test error case with non-positive observed counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeObservedCounts() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, -1, 4};
        double[] expected = {2.5, 3.5, 1.5, 4.5};
        test.chiSquare(expected, observed);
    }

    /**
     * Test error case with non-matching sizes of observed and expected arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMismatchingSizes() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1};
        double[] expected = {2.5, 3.5, 1.5, 4.5};
        test.chiSquare(expected, observed);
    }

    /**
     * Test error case with non-rectangular two-way table for chi-square test with datasets comparison.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonRectangularTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 1};
        long[] observed2 = {2, 3, 1, 4};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test simple case with positive and non-zero expected and observed counts for chi-square test with datasets comparison.
     */
    @Test
    public void testSimpleCaseDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 1};
        long[] observed2 = {2, 3, 1};
        try {
            double chiSquareStat = test.chiSquareDataSetsComparison(observed1, observed2);
            double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
            Assert.assertEquals("Wrong chi-square statistic", 0.0, chiSquareStat, 0.001);
            Assert.assertEquals("Wrong p-value", 1.0, pValue, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test edge case with zero observed count for at least one category in one dataset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroObservedCountOneDataset() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 0, 1};
        long[] observed2 = {2, 3, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test error case with non-matching sizes of observed arrays for chi-square test with datasets comparison.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMismatchingSizesObservedArrays() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3};
        long[] observed2 = {2, 3, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test error case with non-positive expected counts for chi-square test with datasets comparison.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExpectedCountsDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 1};
        long[] observed2 = {2, 3, 1};
        double[] expected = {-1.5, 3.5, 1.5};
        test.chiSquareTestDataSetsComparison(observed1, observed2, expected);
    }

    /**
     * Test error case with non-positive observed counts for chi-square test with datasets comparison.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeObservedCountsDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 1};
        long[] observed2 = {2, 3, -1};
        test.chiSquareTestDataSetsComparison(observed1, observed2, null);
    }

    /**
     * Test edge case with no observed counts for both datasets.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoObservedCounts() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        test.chiSquareTestDataSetsComparison(observed1, observed2, null);
    }
}