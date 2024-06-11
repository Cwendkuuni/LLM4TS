package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.MathException;

import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double expected = test.chiSquare(counts);
        assertEquals(10.5, expected, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidLength() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidLength2() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4, 5}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeCounts() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, -2, 3}, {2, 3, 4}, {3, 4, -5}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidDimensions() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 2, 3}};
        test.chiSquare(counts);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = test.chiSquareTest(counts);
        assertEquals(0.0, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        test.chiSquareTest(counts);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestWithInvalidDegreesOfFreedom() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        test.setDistribution(new ChiSquaredDistributionImpl(-1));
        test.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 4};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidDegreesOfFreedom() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 4};
        test.setDistribution(new ChiSquaredDistributionImpl(-1));
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithLargeValues() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1000000, 1000000, 1000000, 1000000};
        long[] observed2 = {1000001, 1000001, 1000001, 1000001};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0, pValue, 0.001);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 4};
        double expected = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.25, expected, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidLength() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        boolean result = test.chiSquareTest(counts, 0.05);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        test.chiSquareTest(counts, 1.1);
    }

    @Test
    public void testChiSquareTestWithAlphaAndLargeValues() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1000000, 1000000, 1000000, 1000000};
        long[] observed2 = {1000001, 1000001, 1000001, 1000001};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithAlphaAndInvalidLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }
}