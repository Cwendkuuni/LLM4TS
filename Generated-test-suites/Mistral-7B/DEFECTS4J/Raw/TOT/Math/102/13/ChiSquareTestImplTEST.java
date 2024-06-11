package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class
 */
public class ChiSquareTestImplTest {

    /**
     * Test for constructor with no arguments
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        assertEquals(new ChiSquaredDistributionImpl(1.0), testImpl.getDistribution());
    }

    /**
     * Test for constructor with custom distribution
     */
    @Test
    public void testCustomConstructor() {
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(5.0);
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl(dist);
        assertEquals(dist, testImpl.getDistribution());
    }

    /**
     * Test for chiSquare method with correct parameters
     */
    @Test
    public void testChiSquareCorrect() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 3.0};
        double chiSquareResult = testImpl.chiSquare(expected, observed);
        assertEquals(7.0 / 3.0, chiSquareResult, 0.001);
    }

    /**
     * Test for chiSquare method with incorrect length parameters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareIncorrectLength() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 3.0, 3.0};
        testImpl.chiSquare(expected, observed);
    }

    /**
     * Test for chiSquare method with incorrect length parameters (expected)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareIncorrectLengthExpected() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2.0, 3.0};
        testImpl.chiSquare(expected, observed);
    }

    /**
     * Test for chiSquare method with incorrect parameters (non-non-negative observed)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNonNonNegativeObserved() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2.0, 3.0};
        testImpl.chiSquare(expected, observed);
    }

    /**
     * Test for chiSquare method with incorrect parameters (non-positive expected)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNonPositiveExpected() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0};
        testImpl.chiSquare(expected, observed);
    }

    /**
     * Test for chiSquareTest method with correct parameters
     */
    @Test
    public void testChiSquareTestCorrect() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 3.0};
        double pValue = testImpl.chiSquareTest(expected, observed);
        assertEquals(0.0579832731284876, pValue, 0.001);
    }

    /**
     * Test for chiSquareTest method with incorrect parameters (expected)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestIncorrectLengthExpected() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2.0, 3.0};
        testImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareTest method with incorrect parameters (observed)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestIncorrectLengthObserved() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0};
        testImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareTest method with incorrect parameters (non-non-negative observed)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNonNonNegativeObserved() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2.0};
        testImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareTest method with incorrect parameters (non-positive expected)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNonPositiveExpected() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0, 3.0};
        long[] observed = {2.0};
        testImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareTest method with incorrect parameters (alpha)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestIncorrectAlpha() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0};
        testImpl.chiSquareTest(expected, observed, -1.0);
    }

    /**
     * Test for chiSquareTest method with incorrect results
     */
    @Test
    public void testChiSquareTestIncorrectResults() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0};
        assertFalse(testImpl.chiSquareTest(expected, observed, 0.1));
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with correct parameters
     */
    @Test
    public void testChiSquareTestDataSetsComparisonCorrect() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 4};
        double pValue = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.217279031521633, pValue, 0.001);
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with incorrect parameters (observed1)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonIncorrectLengthObserved1() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1};
        long[] observed2 = {2, 2};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with incorrect parameters (observed2)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonIncorrectLengthObserved2() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with incorrect results
     */
    @Test
    public void testChiSquareTestDataSetsComparisonIncorrectResults() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2};
        assertFalse(testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.1));
    }
}