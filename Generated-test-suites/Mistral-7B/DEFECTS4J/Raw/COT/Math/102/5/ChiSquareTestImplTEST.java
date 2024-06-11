package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test suite for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImpl() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest);
    }

    @Test
    public void testChiSquareTestImplConstructorDistribution() {
        ChiSquareDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(chiSquaredDistribution);
        assertNotNull(chiSquareTest);
        assertSame(chiSquaredDistribution, chiSquareTest.getDistribution());
    }

    @Test
    public void testChiSquare_1d() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 3};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(6.0, result, 1e-6);
    }

    @Test
    public void testChiSquare_1d_NegativeExpected() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0};
        long[] observed = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquare_1d_InvalidObservedLength() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquare_1d_InvalidExpectedLength() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquare_1d_InvalidExpected() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, -1.0};
        long[] observed = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTest_1d() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 3};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(2.0);
        double criticalValue = chiSquaredDistribution.inverseCumulativeProbability(0.01);
        assertTrue(pValue < criticalValue);
    }

    @Test
    public void testChiSquareTest_1d_InvalidExpectedLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTest_1d_InvalidObservedLength() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTest_1d_InvalidExpected() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, -1.0};
        long[] observed = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTest_1d_InvalidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed, -1.0));
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed, 1.1));
    }

    @Test
    public void testChiSquareTest_1d_ValidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 3};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.01);
        assertFalse(result);
    }

    @Test
    public void testChiSquare_2d() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        double result = chiSquareTest.chiSquare(counts);
        assertEquals(6.0, result, 1e-6);
    }

    @Test
    public void testChiSquare_2d_InvalidCountsShape() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3}};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(counts));
    }

    @Test
    public void testChiSquareTest_2d() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        double pValue = chiSquareTest.chiSquareTest(counts);
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(3.0);
        double criticalValue = chiSquaredDistribution.inverseCumulativeProbability(0.01);
        assertTrue(pValue < criticalValue);
    }

    @Test
    public void testChiSquareTest_2d_InvalidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(counts, -1.0));
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(counts, 1.1));
    }

    @Test
    public void testChiSquareTest_2d_ValidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        boolean result = chiSquareTest.chiSquareTest(counts, 0.01);
        assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetComparison_1d() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 3};
        long[] observed2 = {1, 3};
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testChiSquareDataSetComparison_1d_InvalidLengths() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1};
        long[] observed2 = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareDataSetComparison_1d_InvalidCounts() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 3};
        long[] observed2 = {1, -3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestDataSetComparison_1d() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 3};
        long[] observed2 = {1, 3};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertNotNull(pValue);
    }

    @Test
    public void testChiSquareTestDataSetComparison_1d_InvalidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 3};
        long[] observed2 = {1, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, -1.0));
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 1.1));
    }

    @Test
    public void testChiSquareTestDataSetComparison_1d_ValidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 3};
        long[] observed2 = {1, 3};
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.01);
        assertTrue(result);
    }
}