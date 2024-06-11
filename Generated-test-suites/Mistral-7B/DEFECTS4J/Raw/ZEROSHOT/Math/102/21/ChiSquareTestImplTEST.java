package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;

/**
 * Unit tests for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest.getDistribution());
    }

    /**
     * Test constructor with provided distribution.
     */
    @Test
    public void testConstructorWithProvidedDistribution() {
        ChiSquaredDistributionImpl distribution = new ChiSquaredDistributionImpl(2.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, chiSquareTest.getDistribution());
    }

    /**
     * Test chiSquare method with valid inputs.
     */
    @Test
    public void testChiSquareWithValidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {2, 3, 5, 6};
        double[] expected = {2.5, 3.5, 5.5, 6.5};

        try {
            double chiSquareTestStatistic = chiSquareTest.chiSquare(expected, observed);
            assertEquals(12.0, chiSquareTestStatistic, 0.0);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    /**
     * Test chiSquare method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {2, 3};
        double[] expected = {2.5, 3.5};

        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     */
    @Test
    public void testChiSquareTestWithValidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {2, 3, 5, 6};
        double[] expected = {2.5, 3.5, 5.5, 6.5};

        try {
            double pValue = chiSquareTest.chiSquareTest(expected, observed);
            assertEquals(0.0125, pValue, 0.001);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTest method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        double[] expected = {2.5};
        long[] observed = {2};

        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with small expected counts.
     */
    @Test(expected = MathException.class)
    public void testChiSquareTestWithSmallExpectedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        double[] expected = {0.0, 0.0, 1.0};
        long[] observed = {2, 3, 5};

        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with large expected counts.
     */
    @Test
    public void testChiSquareTestWithLargeExpectedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        double[] expected = {1000.0, 1000.0, 1000.0, 1000.0};
        long[] observed = {1002, 1003, 1005, 1004};

        try {
            double pValue = chiSquareTest.chiSquareTest(expected, observed);
            assertEquals(0.0048, pValue, 0.0001);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTest method with large array.
     */
    @Test
    public void testChiSquareTestWithLargeArray() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[][] observed = new long[100][100];
        double[][] expected = new double[100][100];

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                observed[i][j] = (long) (Math.random() * 10);
                expected[i][j] = Math.random();
            }
        }

        try {
            double pValue = chiSquareTest.chiSquareTest(expected, observed);
            assertTrue(pValue > 0.0);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = {2, 3, 5, 6};
        long[] observed2 = {4, 3, 5, 6};

        try {
            double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
            assertEquals(0.0331, pValue, 0.001);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = {2};
        long[] observed2 = {3};

        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with small arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithSmallArrays() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = {2};
        long[] observed2 = {};

        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with large arrays.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithLargeArrays() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = new long[100];
        long[] observed2 = new long[100];

        for (int i = 0; i < 100; i++) {
            observed1[i] = (long) (Math.random() * 10);
            observed2[i] = (long) (Math.random() * 10);
        }

        try {
            double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
            assertTrue(pValue > 0.0);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test checkArray method.
     */
    @Test
    public void testCheckArray() {
        long[][] in = new long[2][2];

        try {
            ChiSquareTestImpl.checkArray(in);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }

        in[1][1] = -1;

        try {
            ChiSquareTestImpl.checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        in = new long[2][3];
        try {
            ChiSquareTestImpl.checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        in = new long[3][2];
        try {
            ChiSquareTestImpl.checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        in = new long[3][3];
        try {
            ChiSquareTestImpl.checkArray(in);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
}