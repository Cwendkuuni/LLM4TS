package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareImplConstructor() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        assertNotNull(testImpl);
    }

    @Test
    public void testChiSquareImplConstructorWithDistribution() {
        ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl(dist);
        assertNotNull(testImpl);
        assertEquals(dist, testImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareImplConstructorInvalidObservedOrExpectedLength() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = new long[2];
        testImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareImplConstructorInvalidObservedOrExpectedArray() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = null;
        long[] observed = new long[1];
        testImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareImplInvalidExpectedOrObservedArrayLengthMismatch() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[1];
        testImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareImplValidData() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.5, 2.5, 1.5, 1.5};
        long[] observed = new long[]{3, 3, 2, 2};
        double chiSquareStat = testImpl.chiSquare(expected, observed);
        assertEquals(6.25, chiSquareStat, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareImplNegativeExpectedData() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.5, 2.5, 1.5, 1.5};
        long[] observed = new long[]{3, 3, 2, 2};
        testImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareImplNegativeObservedData() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.5, 2.5, 1.5, 1.5};
        long[] observed = new long[]{-3, 3, 2, 2};
        testImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestPValue() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.5, 2.5, 1.5, 1.5};
        long[] observed = new long[]{3, 3, 2, 2};
        double pValue = testImpl.chiSquareTest(expected, observed);
        double expectedPValue = 0.05;
        assertEquals(expectedPValue, pValue, 0.01);
    }

    @Test
    public void testChiSquareTestSignificanceLevel() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.5, 2.5, 1.5, 1.5};
        long[] observed = new long[]{3, 3, 2, 2};
        boolean rejectNullHypothesis = testImpl.chiSquareTest(expected, observed, 0.05);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestNonRejectNullHypothesis() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.5, 2.5, 1.5, 1.5};
        long[] observed = new long[]{3, 3, 3, 3};
        boolean rejectNullHypothesis = testImpl.chiSquareTest(expected, observed, 0.1);
        assertFalse(rejectNullHypothesis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidSignificanceLevel() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.5, 2.5, 1.5, 1.5};
        long[] observed = new long[]{3, 3, 2, 2};
        testImpl.chiSquareTest(expected, observed, 1.1);
    }

    @Test
    public void testChiSquare2WayTableData() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{2, 3}, {3, 2}};
        double chiSquareStat = testImpl.chiSquare(counts);
        assertEquals(2.0, chiSquareStat, 0.01);
    }

    @Test
    public void testChiSquare2WayTablePValue() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{2, 3}, {3, 2}};
        double pValue = testImpl.chiSquareTest(counts);
        double expectedPValue = 0.1;
        assertEquals(expectedPValue, pValue, 0.01);
    }

    @Test
    public void testChiSquare2WayTableSignificanceLevel() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{2, 3}, {3, 2}};
        boolean rejectNullHypothesis = testImpl.chiSquareTest(counts, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquare2WayTableValidData() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{3, 3}, {3, 3}};
        boolean rejectNullHypothesis = testImpl.chiSquareTest(counts, 0.05);
        assertTrue(rejectNullHypothesis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableInvalidCountsShape() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{2}, {3}};
        testImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableNegativeCounts() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{-2, 3}, {3, 2}};
        testImpl.chiSquareTest(counts);
    }
}