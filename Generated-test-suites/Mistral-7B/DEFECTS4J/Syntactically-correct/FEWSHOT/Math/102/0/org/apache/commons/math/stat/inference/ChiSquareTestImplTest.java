package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double expected = test.chiSquare(counts);
        assertEquals(14.25, expected, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput1() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {2, 3, 4}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput2() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {2, 3}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput3() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1}, {2, 3}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput4() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {2, -3}};
        test.chiSquare(counts);
    }

    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = test.chiSquareTest(counts);
        assertLessThan(0.05, pValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {2, 3, 4}};
        test.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 4, 5, 6};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertLessThan(0.05, pValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4, 5, 6};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 4, 5, 6};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlphaRejected() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 4, 5, 7};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }
}