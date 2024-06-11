package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long[] observed2 = {1, 2, 3, 4, 5, 5, 6, 6, 7, 8};

        assertEquals(10.738836561570156, test.chiSquare(observed1, observed2), 0.001);

        long[] observed3 = {1, 1, 2, 3, 5, 8, 13, 21};
        long[] expected3 = {2, 4, 3, 3, 3, 3, 2, 3};

        assertEquals(6.604438565634227, test.chiSquare(expected3, observed3), 0.001);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long[] observed2 = {1, 2, 3, 4, 5, 5, 6, 6, 7, 8};

        assertEquals(0.04120953663281752, test.chiSquareTest(observed1, observed2), 0.001);

        long[] observed3 = {1, 1, 2, 3, 5, 8, 13, 21};
        long[] expected3 = {2, 4, 3, 3, 3, 3, 2, 3};

        assertEquals(0.9999718376870236, test.chiSquareTest(expected3, observed3), 0.001);
    }

    @Test
    public void testChiSquareTestGivenAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long[] observed2 = {1, 2, 3, 4, 5, 5, 6, 6, 7, 8};

        assertFalse(test.chiSquareTest(observed1, observed2, 0.5));

        long[] observed3 = {1, 1, 2, 3, 5, 8, 13, 21};
        long[] expected3 = {2, 4, 3, 3, 3, 3, 2, 3};

        assertTrue(test.chiSquareTest(expected3, observed3, 0.05));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidObservedOrExpected() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = null;
        long[] expected = {1, 2, 3, 4, 5};

        test.chiSquare(observed, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidObservedOrExpectedLengths() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {1, 2};
        long[] expected = {1, 2, 3};

        test.chiSquare(observed, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpected() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = null;

        test.chiSquare(observed, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedCounts() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {1, 2, 3};

        test.chiSquare(observed, expected);
    }

    @Test
    public void testChiSquareWithZeroObservedOrExpected() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {0, 0};
        long[] expected = {1, 1};

        assertEquals(0.0, test.chiSquareTest(observed, expected), 0.0);

        long[] observed2 = {1, 1};
        long[] expected2 = {0, 0};

        assertEquals(1.0, test.chiSquareTest(observed2, expected2), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidDistribution() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.setDistribution(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeExpected() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {-1, 2, 3, 4, 5};

        test.chiSquare(observed, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeObserved() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {-1, 2, 3, 4, 5};
        long[] expected = {1, 2, 3, 4, 5};

        test.chiSquare(observed, expected);
    }

    @Test
    public void testChiSquareWithLargeArrays() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = new long[10000];
        long[] expected = new long[10000];

        for (int i = 0; i < 10000; i++) {
            observed[i] = (long) (Math.random() * 10);
            expected[i] = (long) (Math.random() * 10);
        }

        assertEquals(test.chiSquare(expected, observed), test.chiSquare(observed, expected), 0.001);

        assertEquals(test.chiSquareTest(expected, observed), test.chiSquareTest(observed, expected), 0.001);

        assertEquals(test.chiSquareTest(expected, observed, 0.05), test.chiSquareTest(observed, expected, 0.05), 0.001);
    }
}