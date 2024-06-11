package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl testImpl;

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {3, 4}};
        double expectedResult = 2.0;
        double result = testImpl.chiSquare(counts);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInput() throws IllegalArgumentException {
        testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {3}};
        testImpl.chiSquare(counts);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {3, 4}};
        double alpha = 0.05;
        boolean expectedResult = true;
        boolean result = testImpl.chiSquareTest(counts, alpha);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput() throws IllegalArgumentException, MathException {
        testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {3}};
        testImpl.chiSquareTest(counts, 0.05);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4, 5};
        long[] observed2 = new long[]{2, 1, 3, 4, 5};
        double alpha = 0.05;
        boolean expectedResult = true;
        boolean result = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInput() throws IllegalArgumentException, MathException {
        testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 1, 3};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4, 5};
        long[] observed2 = new long[]{2, 1, 3, 4, 5};
        double expectedResult = 0.25;
        double result = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidInput() throws IllegalArgumentException {
        testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 1, 3};
        testImpl.chiSquareDataSetsComparison(observed1, observed2);
    }
}