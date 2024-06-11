package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl.
 */
public class ChiSquareTestImplTest {

    /** Test instance. */
    private ChiSquareTestImpl test;

    /**
     * Sets up test instance.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
    }

    /**
     * Test chiSquare method with valid input.
     */
    @Test
    public void testChiSquareWithValidInput() {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        double result = test.chiSquare(expected, observed);
        double expectedResult = 3.0434523412848425; // calculated using
                                                    // MathCad
        assertEquals(expectedResult, result, 0.00000001);
    }

    /**
     * Test chiSquare method with invalid input (null expected).
     */
    @Test
    public void testChiSquareWithNullExpected() {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = null;
        assertThrows(IllegalArgumentException.class, () -> {
            test.chiSquare(expected, observed);
        });
    }

    /**
     * Test chiSquare method with invalid input (null observed).
     */
    @Test
    public void testChiSquareWithNullObserved() {
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        long[] observed = null;
        assertThrows(IllegalArgumentException.class, () -> {
            test.chiSquare(expected, observed);
        });
    }

    /**
     * Test chiSquare method with invalid input (length mismatch).
     */
    @Test
    public void testChiSquareWithLengthMismatch() {
        long[] observed = new long[] { 1, 2, 3 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        assertThrows(IllegalArgumentException.class, () -> {
            test.chiSquare(expected, observed);
        });
    }

    /**
     * Test chiSquare method with invalid input (negative expected).
     */
    @Test
    public void testChiSquareWithNegativeExpected() {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { -2.5, 2.5, 2.5, 2.5 };
        assertThrows(IllegalArgumentException.class, () -> {
            test.chiSquare(expected, observed);
        });
    }

    /**
     * Test chiSquare method with invalid input (negative observed).
     */
    @Test
    public void testChiSquareWithNegativeObserved() {
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        long[] observed = new long[] { -1, 2, 3, 4 };
        assertThrows(IllegalArgumentException.class, () -> {
            test.chiSquare(expected, observed);
        });
    }

    /**
     * Test chiSquareTest method with valid input and expected p-value.
     */
    @Test
    public void testChiSquareTestWithValidInput() throws MathException {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.95; // calculated using MathCad
        assertEquals(expectedPValue, pValue, 0.0000001);
    }

    /**
     * Test chiSquareTest method with valid input and alpha > 0.5.
     */
    @Test
    public void testChiSquareTestWithAlphaGreaterThan05()
            throws MathException, IllegalArgumentException {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        boolean result = test.chiSquareTest(expected, observed, 0.6);
        assertFalse(result);
    }

    /**
     * Test chiSquareTest method with valid input and alpha = 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithAlphaZero() throws MathException {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        test.chiSquareTest(expected, observed, 0);
    }

    /**
     * Test chiSquareTest method with valid input and alpha < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithAlphaNegative() throws MathException {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        test.chiSquareTest(expected, observed, -0.1);
    }

    /**
     * Test chiSquareTest method with invalid input (null expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithNullExpected() throws MathException {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = null;
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input (null observed).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithNullObserved() throws MathException {
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        long[] observed = null;
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input (length mismatch).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithLengthMismatch() throws MathException {
        long[] observed = new long[] { 1, 2, 3 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input (negative expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithNegativeExpected() throws MathException {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { -2.5, 2.5, 2.5, 2.5 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input (negative observed).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithNegativeObserved() throws MathException {
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        long[] observed = new long[] { -1, 2, 3, 4 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid input and alpha = 1.
     */
    @Test
    public void testChiSquareTestWithAlphaOne() throws MathException {
        long[] observed = new long[] { 1, 2, 3, 4 };
        double[] expected = new double[] { 2.5, 2.5, 2.5, 2.5 };
        boolean result = test.chiSquareTest(expected, observed, 1);
        assertTrue(result);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input and expected
     * p-value.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInput()
            throws MathException {
        long[] observed1 = new long[] { 1, 2, 3, 4, 5 };
        long[] observed2 = new long[] { 2, 2, 3, 4, 5 };
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.05373027144272668; // calculated using MathCad
        assertEquals(expectedPValue, pValue, 0.0000001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input and alpha > 0.5.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlphaGreaterThan05()
            throws MathException, IllegalArgumentException {
        long[] observed1 = new long[] { 1, 2, 3, 4, 5 };
        long[] observed2 = new long[] { 2, 2, 3, 4, 5 };
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2,
                0.6);
        assertFalse(result);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input and alpha = 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithAlphaZero()
            throws MathException {
        long[] observed1 = new long[] { 1, 2, 3, 4, 5 };
        long[] observed2 = new long[] { 2, 2, 3, 4, 5 };
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input and alpha < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithAlphaNegative()
            throws MathException {
        long[] observed1 = new long[] { 1, 2, 3, 4, 5 };
        long[] observed2 = new long[] { 2, 2, 3, 4, 5 };
        test.chiSquareTestDataSetsComparison(observed1, observed2, -0.1);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid input (null observed1).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithNullObserved1()
            throws MathException {
        long[] observed2 = new long[] { 2, 2, 3, 4, 5 };
        test.chiSquareTestDataSetsComparison(null, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid input (null observed2).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithNullObserved2()
            throws MathException {
        long[] observed1 = new long[] { 1, 2, 3, 4, 5 };
        test.chiSquareTestDataSetsComparison(observed1, null);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid input (length
     * mismatch).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithLengthMismatch()
            throws MathException {
        long[] observed1 = new long[] { 1, 2, 3, 4 };
        long[] observed2 = new long[] { 2, 2, 3, 4, 5 };
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid input (negative
     * observed).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithNegativeObserved()
            throws MathException {
        long[] observed1 = new long[] { 1, 2, 3, 4, 5 };
        long[] observed2 = new long[] { -1, 2, 3, 4, 5 };
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input and alpha = 1.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlphaOne()
            throws MathException {
        long[] observed1 = new long[] { 1, 2, 3, 4, 5 };
        long[] observed2 = new long[] { 2, 2, 3, 4, 5 };
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2,
                1);
        assertTrue(result);
    }
}