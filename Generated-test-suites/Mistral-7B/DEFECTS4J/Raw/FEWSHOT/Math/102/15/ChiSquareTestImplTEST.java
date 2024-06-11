package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl testImpl;
    private ChiSquaredDistribution distribution;

    @Test
    public void testChiSquareTestImplConstruction() {
        testImpl = new ChiSquareTestImpl();
        assertNull(testImpl.getDistribution());
    }

    @Test
    public void testChiSquareTestImplConstructionWithDistribution() {
        distribution = new ChiSquaredDistributionImpl(1.0);
        testImpl = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, testImpl.getDistribution());
    }

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] expected = {1, 2, 3};
        long[] observed = {2, 2, 3};
        double chiSquare = testImpl.chiSquare(expected, observed);
        assertEquals(5.5, chiSquare, 0.01);
    }

    @Test
    public void testChiSquareThrowsIllegalArgumentExceptionWhenExpectedAndObservedArrayLengthsDoNotMatch() {
        long[] expected = {1, 2};
        long[] observed = {2, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> testImpl.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareThrowsIllegalArgumentExceptionWhenExpectedArrayContainsNegativeValues() {
        long[] expected = {-1, 2};
        long[] observed = {2, 2};
        assertThrows(IllegalArgumentException.class, () -> testImpl.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareThrowsIllegalArgumentExceptionWhenObservedArrayContainsNegativeValues() {
        long[] expected = {1, 2};
        long[] observed = {2, -2};
        assertThrows(IllegalArgumentException.class, () -> testImpl.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3};
        long[] observed = {2, 2, 3};
        double pValue = testImpl.chiSquareTest(expected, observed);
        assertEquals(0.0512715752636262, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestThrowsIllegalArgumentExceptionWhenExpectedAndObservedArrayLengthsDoNotMatch() {
        long[] expected = {1, 2};
        long[] observed = {2, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> testImpl.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestThrowsMathException() throws IllegalArgumentException {
        long[] expected = {1, 2};
        long[] observed = {1, 1, 1};
        assertThrows(MathException.class, () -> testImpl.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestThrowsIllegalArgumentExceptionWhenExpectedArrayContainsNegativeValues() {
        long[] expected = {-1, 2};
        long[] observed = {2, 2};
        assertThrows(IllegalArgumentException.class, () -> testImpl.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestThrowsIllegalArgumentExceptionWhenObservedArrayContainsNegativeValues() {
        long[] expected = {1, 2};
        long[] observed = {2, -2};
        assertThrows(IllegalArgumentException.class, () -> testImpl.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException {
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 1};
        double chiSquare = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.0, chiSquare, 0.01);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonThrowsIllegalArgumentException() {
        long[] observed1 = {1, 2};
        long[] observed2 = {1, 1};
        assertThrows(IllegalArgumentException.class, () -> testImpl.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonThrowsMathException() {
        long[] observed1 = {1, 2};
        long[] observed2 = {1, 0};
        assertThrows(MathException.class, () -> testImpl.chiSquareTestDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 1};
        double alpha = 0.05;
        boolean result = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }

    @Test
    public void testCheckArray() throws IllegalArgumentException {
        long[][] counts = new long[][]{{1, 2}, {1, 2, 3}};
        testImpl.checkArray(counts);
        testImpl.checkArray(new long[][]{{1, 2}, {1, 2}});
    }

    @Test
    public void testCheckArrayThrowsIllegalArgumentExceptionWhenInputTableHasLessThanTwoRows() {
        long[][] counts = new long[][]{{1}};
        assertThrows(IllegalArgumentException.class, () -> testImpl.checkArray(counts));
    }

    @Test
    public void testCheckArrayThrowsIllegalArgumentExceptionWhenInputTableHasLessThanTwoColumns() {
        long[][] counts = new long[][]{{1, 1}};
        assertThrows(IllegalArgumentException.class, () -> testImpl.checkArray(counts));
    }

    @Test
    public void testCheckArrayThrowsIllegalArgumentExceptionWhenInputTableIsNotRectangular() {
        long[][] counts = new long[][]{{1, 2}, {1, 2, 3}};
        assertThrows(IllegalArgumentException.class, () -> testImpl.checkArray(counts));
    }

    @Test
    public void testCheckArrayThrowsIllegalArgumentExceptionWhenInputTableContainsNegativeEntries() {
        long[][] counts = new long[][]{{-1, 2}, {1, 2}};
        assertThrows(IllegalArgumentException.class, () -> testImpl.checkArray(counts));
    }

    @Test
    public void testIsPositive() {
        double[] in = new double[]{1.0, 2.0, 3.0};
        assertTrue(testImpl.isPositive(in));
    }

    @Test
    public void testIsPositiveThrowsNullPointerException() {
        double[] in = null;
        assertThrows(NullPointerException.class, () -> testImpl.isPositive(in));
    }

    @Test
    public void testIsNonNegative() {
        long[] in = new long[]{1, 2, 3};
        assertTrue(testImpl.isNonNegative(in));
    }

    @Test
    public void testIsNonNegativeThrowsNullPointerException() {
        long[] in = null;
        assertThrows(NullPointerException.class, () -> testImpl.isNonNegative(in));
    }

    @Test
    public void testIsNonNegativeLongArray2D() {
        long[][] in = new long[][]{{1, 2, 3}, {1, 2, 3}};
        assertTrue(testImpl.isNonNegative(in));
    }

    @Test
    public void testIsNonNegativeLongArray2DThrowsNullPointerException() {
        long[][] in = null;
        assertThrows(NullPointerException.class, () -> testImpl.isNonNegative(in));
    }

    @Test
    public void testSetDistribution() {
        distribution = new ChiSquaredDistributionImpl(1.0);
        testImpl.setDistribution(distribution);
        assertEquals(distribution, testImpl.getDistribution());
    }

    @Test
    public void testSetDistributionThrowsNullPointerException() {
        testImpl.setDistribution(null);
        assertNull(testImpl.getDistribution());
    }
}