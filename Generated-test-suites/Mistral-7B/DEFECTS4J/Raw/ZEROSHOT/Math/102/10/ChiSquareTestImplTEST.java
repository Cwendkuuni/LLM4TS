package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.distribution.DistributionFactory;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    @org.junit.Before
    public void setUp() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        testImpl.setDistribution(new ChiSquaredDistributionImpl(1.0));
    }

    @org.junit.Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double chiSquareStatistic = testImpl.chiSquare(expected, observed);
        assertEquals(6.0, chiSquareStatistic, 0.01);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidObserved() throws IllegalArgumentException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        testImpl.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpected() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5};
        testImpl.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareMismatchedLengths() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        testImpl.chiSquare(expected, observed);
    }

    @org.junit.Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double pValue = testImpl.chiSquareTest(expected, observed);
        assertTrue(1.0 - pValue > 0.05);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidObserved() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        testImpl.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidExpected() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5};
        testImpl.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMismatchedLengths() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        testImpl.chiSquareTest(expected, observed);
    }

    @org.junit.Test
    public void testChiSquareTestAlpha() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        boolean result = testImpl.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegativeAlpha() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        testImpl.chiSquareTest(expected, observed, -0.05);
    }

    @org.junit.Test
    public void testChiSquareTestZeroAlpha() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        boolean result = testImpl.chiSquareTest(expected, observed, 0.0);
        assertFalse(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestAlphaGreaterThanOne() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        testImpl.chiSquareTest(expected, observed, 1.1);
    }

    @org.junit.Test
    public void testChiSquareLongArrayComparison() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        double chiSquareStatistic = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, chiSquareStatistic, 0.01);
    }

    @org.junit.Test
    public void testChiSquareLongArrayComparisonDifferentSizes() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3};
        double chiSquareStatistic = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(6.0, chiSquareStatistic, 0.01);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareLongArrayComparisonInvalidObserved1() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2, 3, 4, 5};
        testImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareLongArrayComparisonInvalidObserved2() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3};
        testImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        double pValue = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(1.0 - pValue > 0.05);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidObserved1() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2, 3, 4, 5};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidObserved2() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMismatchedLengths() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparisonAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        boolean result = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonNegativeAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2, -0.05);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonZeroAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonAlphaGreaterThanOne() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 1.1);
    }

    private void checkArray(long[][] in) {
        if (in == null) {
            throw new IllegalArgumentException("Input table cannot be null");
        }
        if (in.length < 2 || in[0].length < 2) {
            throw new IllegalArgumentException("Input table must have at least two rows and two columns");
        }
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                if (in[i][j] < 0) {
                    throw new IllegalArgumentException("All entries in input 2-way table must be non-negative");
                }
            }
        }
    }

}