package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import java.util.Random;

/**
 * Tests for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    /**
     * Test chiSquare method with valid inputs.
     */
    @org.junit.Test
    public void testChiSquareValidInput() {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double result = test.chiSquare(expected, observed);
        assertEquals(6.4, result, 1e-6);
    }

    /**
     * Test chiSquare method with invalid inputs: different lengths.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputDifferentLengths() {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid inputs: negative expected counts.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputNegativeExpected() {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4};
        double[] expected = {-2.5, 2.5, 2.5, 2.5};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid inputs: non-positive observed counts.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputNonPositiveObserved() {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {-1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     */
    @org.junit.Test
    public void testChiSquareTestValidInput() throws MathException,
            IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.0446608, pValue, 1e-6);
    }

    /**
     * Test chiSquareTest method with invalid inputs: different lengths.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputDifferentLengths()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid inputs: negative expected counts.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputNegativeExpected()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4};
        double[] expected = {-2.5, 2.5, 2.5, 2.5};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid inputs: non-positive observed counts.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputNonPositiveObserved()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {-1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs and different significance
     * levels.
     */
    @org.junit.Test
    public void testChiSquareTestValidInputDifferentSignificanceLevels()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double pValue = test.chiSquareTest(expected, observed, 0.025);
        assertFalse(pValue > 0.025);
        pValue = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(pValue > 0.05);
        pValue = test.chiSquareTest(expected, observed, 0.1);
        assertTrue(pValue > 0.1);
    }

    /**
     * Test chiSquareTest method with valid inputs and alpha == 0.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputAlphaZero()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        test.chiSquareTest(expected, observed, 0.0);
    }

    /**
     * Test chiSquareTest method with valid inputs and alpha > 0.5.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputAlphaGreaterThan05()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        test.chiSquareTest(expected, observed, 0.6);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs.
     */
    @org.junit.Test
    public void testChiSquareTestDataSetsComparisonValidInput()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0167033, pValue, 1e-6);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid inputs: different lengths.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputDifferentLengths()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3, 4};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid inputs: negative observed counts.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputNegativeObserved()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {-2, 2, 3, 4};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs and different significance levels.
     */
    @org.junit.Test
    public void testChiSquareTestDataSetsComparisonValidInputDifferentSignificanceLevels()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.025);
        assertFalse(pValue > 0.025);
        pValue = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(pValue > 0.05);
        pValue = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
        assertTrue(pValue > 0.1);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs and alpha == 0.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputAlphaZero()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs and alpha > 0.5.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputAlphaGreaterThan05()
            throws MathException, IllegalArgumentException {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.6);
    }

    /**
     * Test createChiSquareTestImpl method.
     */
    @org.junit.Test
    public void testCreateChiSquareTestImpl() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertEquals(1.0, test.getDegreesOfFreedom(), 1e-9);
    }

    /**
     * Test createChiSquareTestImpl method with custom distribution.
     */
    @org.junit.Test
    public void testCreateChiSquareTestImplWithCustomDistribution() {
        final ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(2);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals(2.0, test.getDegreesOfFreedom(), 1e-9);
    }
}