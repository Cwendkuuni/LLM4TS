package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoSuchAlgorithmException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl
 */
public class ChiSquareTestImplTest {

    /**
     * Test for ChiSquareTestImpl constructor with valid input.
     */
    @Test
    public void testConstructorValidInput() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        Assert.assertNotNull(chiSquareTest.getDistribution());
    }

    /**
     * Test for ChiSquareTestImpl constructor with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullInput() {
        new ChiSquareTestImpl(null);
    }

    /**
     * Test for chiSquare method with valid input.
     */
    @Test
    public void testChiSquareValidInput() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        double[] expected = {2.5, 2.5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 2.5};
        double statistic = chiSquareTest.chiSquare(expected, observed);
        Assert.assertEquals(11.07741528, statistic, 0.001);
    }

    /**
     * Test for chiSquare method with invalid input - different lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputDifferentLengths() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 5.0, 5.0, 5.0};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test for chiSquare method with invalid input - negative expected counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputNegativeExpectedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        double[] expected = {-2.5, 2.5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 2.5};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test for chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() throws MathIllegalArgumentException, NoSuchAlgorithmException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        double[] expected = {2.5, 2.5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 2.5};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        Assert.assertEquals(0.01815516, pValue, 0.001);
    }

    /**
     * Test for chiSquareTest method with invalid input - different lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputDifferentLengths() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 5.0, 5.0, 5.0};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareTest method with invalid input - negative observed counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputNegativeObservedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {-1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        double[] expected = {2.5, 2.5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 2.5};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareTest method with invalid input - invalid expected counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputInvalidExpectedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        double[] expected = {1.5, 2.5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 2.5};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareTest method with valid input and small significance level.
     */
    @Test
    public void testChiSquareTestValidInputWithSmallSignificanceLevel()
            throws MathIllegalArgumentException, NoSuchAlgorithmException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        double[] expected = {2.5, 2.5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 2.5};
        boolean rejectNullHypothesis = chiSquareTest.chiSquareTest(expected, observed, 0.01);
        Assert.assertTrue(rejectNullHypothesis);
    }

    /**
     * Test for chiSquareTest method with valid input and large significance level.
     */
    @Test
    public void testChiSquareTestValidInputWithLargeSignificanceLevel()
            throws MathIllegalArgumentException, NoSuchAlgorithmException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        double[] expected = {2.5, 2.5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 2.5};
        boolean rejectNullHypothesis = chiSquareTest.chiSquareTest(expected, observed, 0.5);
        Assert.assertFalse(rejectNullHypothesis);
    }

    /**
     * Test for chiSquare method with long[][] input.
     */
    @Test
    public void testChiSquareLongArray() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};
        double statistic = chiSquareTest.chiSquare(counts);
        Assert.assertEquals(7.0, statistic, 0.0);
    }

    /**
     * Test for chiSquareTest method with long[][] input.
     */
    @Test
    public void testChiSquareTestLongArray()
            throws MathIllegalArgumentException, NoSuchAlgorithmException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};
        double pValue = chiSquareTest.chiSquareTest(counts);
        Assert.assertEquals(0.137931034, pValue, 0.001);
    }

    /**
     * Test for chiSquareTest method with long[][] input and small significance level.
     */
    @Test
    public void testChiSquareTestLongArrayWithSmallSignificanceLevel()
            throws MathIllegalArgumentException, NoSuchAlgorithmException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};
        boolean rejectNullHypothesis = chiSquareTest.chiSquareTest(counts, 0.01);
        Assert.assertFalse(rejectNullHypothesis);
    }

    /**
     * Test for chiSquareTest method with long[][] input and large significance level.
     */
    @Test
    public void testChiSquareTestLongArrayWithLargeSignificanceLevel()
            throws MathIllegalArgumentException, NoSuchAlgorithmException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};
        boolean rejectNullHypothesis = chiSquareTest.chiSquareTest(counts, 0.5);
        Assert.assertTrue(rejectNullHypothesis);
    }
}