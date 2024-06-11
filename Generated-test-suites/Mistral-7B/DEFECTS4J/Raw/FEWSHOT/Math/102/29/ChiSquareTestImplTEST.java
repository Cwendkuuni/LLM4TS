package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.distribution.DistributionFactory;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl test;
    private final long[][] counts = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };

    @Test
    public void testChiSquareTestImplInitialization() {
        test = new ChiSquareTestImpl();
        assertEquals(1.0, test.distribution.getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testChiSquareTestImplInitializationWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(3);
        test = new ChiSquareTestImpl(distribution);
        assertEquals(3.0, test.distribution.getDegreesOfFreedoms(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareConstructorWithNullObserved() {
        test = new ChiSquareTestImpl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareConstructorWithNullExpected() {
        test = new ChiSquareTestImpl(new long[0][], new double[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareConstructorWithUnequalLengths() {
        test = new ChiSquareTestImpl(new long[][] { { 1, 2 }, { 1 } }, new long[] { 2, 2, 1 });
    }

    @Test
    public void testChiSquareWithExpectedAndObservedArrays() throws IllegalArgumentException {
        test = new ChiSquareTestImpl();
        long[] observed = { 1, 2, 3 };
        long[] expected = { 2, 3, 1 };
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(7.0 / 3.0, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestWithExpectedAndObservedArrays() throws IllegalArgumentException, MathException {
        test = new ChiSquareTestImpl();
        long[] observed = { 1, 2, 3 };
        long[] expected = { 2, 3, 1 };
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.141, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestWithExpectedAndObservedArraysWithLowAlpha()
            throws IllegalArgumentException, MathException {
        test = new ChiSquareTestImpl();
        long[] observed = { 1, 2, 3 };
        long[] expected = { 2, 3, 1 };
        boolean rejectHypothesis = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(rejectHypothesis);
    }

    @Test
    public void testChiSquareWithLongArray() throws IllegalArgumentException {
        test = new ChiSquareTestImpl();
        long[] counts = { 1, 2, 3 };
        double chiSquare = test.chiSquare(counts);
        assertEquals(7.0 / 3.0, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestWithLongArray() throws IllegalArgumentException, MathException {
        test = new ChiSquareTestImpl();
        long[] counts = { 1, 2, 3 };
        double pValue = test.chiSquareTest(counts);
        assertEquals(0.141, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithLongArrays()
            throws IllegalArgumentException, MathException {
        test = new ChiSquareTestImpl();
        long[] observed1 = { 1, 2, 3 };
        long[] observed2 = { 2, 3, 3 };
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.036, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithLongArraysWithLowAlpha()
            throws IllegalArgumentException, MathException {
        test = new ChiSquareTestImpl();
        long[] observed1 = { 1, 2, 3 };
        long[] observed2 = { 2, 3, 3 };
        boolean rejectHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(rejectHypothesis);
    }

    @Test
    public void testCheckArray() {
        long[][] in = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        test.checkArray(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithEmptyTable() {
        long[][] in = new long[0][];
        test.checkArray(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithNegativeValue() {
        long[][] in = { { -1, 2 }, { 4, 5 } };
        test.checkArray(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithNonRectangularTable() {
        long[][] in = { { 1, 2 }, { 3, 4, 5 } };
        test.checkArray(in);
    }
}