package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    private static final int SIZE = 5;
    private static final double ALPHA = 0.05;
    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquareMethod() throws IllegalArgumentException, MathException {
        long[][] counts = new long[2][2];
        counts[0][0] = 10;
        counts[0][1] = 20;
        counts[1][0] = 15;
        counts[1][1] = 15;
        double expectedResult = 3.841554901960853;
        double actualResult = chiSquareTest.chiSquare(counts);
        Assert.assertEquals(expectedResult, actualResult, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidLength() throws IllegalArgumentException {
        long[][] counts = new long[2][1];
        chiSquareTest.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidCounts() throws IllegalArgumentException {
        long[][] counts = new long[2][2];
        counts[0][0] = -5;
        counts[0][1] = 20;
        counts[1][0] = 15;
        counts[1][1] = 15;
        chiSquareTest.chiSquare(counts);
    }

    @Test
    public void testChiSquareTestMethod() throws IllegalArgumentException, MathException {
        long[][] counts = new long[2][2];
        counts[0][0] = 10;
        counts[0][1] = 20;
        counts[1][0] = 15;
        counts[1][1] = 15;
        double expectedResult = 0.02164887288732914;
        double actualResult = chiSquareTest.chiSquareTest(counts);
        Assert.assertEquals(expectedResult, actualResult, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidLength() throws IllegalArgumentException {
        long[][] counts = new long[2][1];
        chiSquareTest.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidCounts() throws IllegalArgumentException, MathException {
        long[][] counts = new long[2][2];
        counts[0][0] = -5;
        counts[0][1] = 20;
        counts[1][0] = 15;
        counts[1][1] = 15;
        chiSquareTest.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareTestMethodWithAlpha() throws IllegalArgumentException, MathException {
        long[][] counts = new long[2][2];
        counts[0][0] = 10;
        counts[0][1] = 20;
        counts[1][0] = 15;
        counts[1][1] = 15;
        boolean expectedResult = false;
        boolean actualResult = chiSquareTest.chiSquareTest(counts, ALPHA);
        Assert.assertFalse(expectedResult == actualResult);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethod() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{5, 10};
        long[] observed2 = new long[]{8, 8};
        double expectedResult = 2.706855492952115;
        double actualResult = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        Assert.assertEquals(expectedResult, actualResult, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodWithInvalidLength() throws IllegalArgumentException {
        long[] observed1 = new long[]{5};
        long[] observed2 = new long[]{8, 8};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodWithInvalidObserved() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{5, 10};
        long[] observed2 = new long[]{-1, 8};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethod() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{5, 10};
        long[] observed2 = new long[]{8, 8};
        double expectedResult = 0.04974198983755216;
        double actualResult = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(expectedResult, actualResult, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidLength() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{5};
        long[] observed2 = new long[]{8, 8};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidObserved() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{5, 10};
        long[] observed2 = new long[]{-1, 8};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[]{5, 10};
        long[] observed2 = new long[]{8, 8};
        boolean expectedResult = true;
        boolean actualResult = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, ALPHA);
        Assert.assertFalse(expectedResult == actualResult);
    }
}