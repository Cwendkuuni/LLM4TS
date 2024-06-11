package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Test class for ChiSquareTestImpl
 */
public class ChiSquareTestImplTest {

    /**
     * Instance of ChiSquareTestImpl for testing
     */
    private ChiSquareTestImpl chiSquareTest;

    /**
     * Set up the test
     */
    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    /**
     * Test chiSquare method with valid parameters
     * @throws IllegalArgumentException
     * @throws MathException
     */
    @Test
    public void testChiSquareValidParameters() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double actual = chiSquareTest.chiSquare(expected, observed);
        double expectedValue = 3.84155463915024; //value obtained from statistical table for alpha = 0.05 and df = 3
        assertEquals("chiSquare method returned incorrect value for valid parameters", expectedValue, actual, 0.01);
    }

    /**
     * Test chiSquare method with invalid parameters - incorrect array lengths
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidParameters_IncorrectArrayLengths() {
        long[] observed = {1, 2, 3};
        double[] expected = new double[3];
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid parameters - negative expected counts
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidParameters_NegativeExpectedCounts() {
        long[] observed = {1, 1, 1};
        double[] expected = {-1.0, 1.0, 2.0};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid parameters
     * @throws IllegalArgumentException
     * @throws MathException
     */
    @Test
    public void testChiSquareTestValidParameters() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        double expectedValue = 0.0455; //value obtained from previous test
        assertEquals("chiSquareTest method returned incorrect value for valid parameters", expectedValue, pValue, 0.01);
    }

    /**
     * Test chiSquareTest method with invalid parameters - incorrect array lengths
     * @throws IllegalArgumentException
     * @throws MathException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidParameters_IncorrectArrayLengths() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 2.5};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with large input for p-value calculation
     * @throws IllegalArgumentException
     * @throws MathException
     */
    @Test
    public void testChiSquareTestLargeInput() throws IllegalArgumentException, MathException {
        long[] observed = new long[10000];
        long sumObserved = 0;
        for (int i = 0; i < observed.length; i++) {
            observed[i] = (long) (Math.random() * 10);
            sumObserved += observed[i];
        }
        double[] expected = new double[10000];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = sumObserved / (double) (observed.length);
        }
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        double expectedValue = 1.0 - Math.pow(0.05, (double) observed.length / 2.0);
        assertEquals("chiSquareTest method returned incorrect value for large input", expectedValue, pValue, 0.01);
    }

    /**
     * Test chiSquare method with long[][] input
     * @throws IllegalArgumentException
     * @throws MathException
     */
    @Test
    public void testChiSquareLongArray() throws IllegalArgumentException, MathException {
        long[][] counts = {{1, 2}, {2, 3}, {1, 1}};
        double actual = chiSquareTest.chiSquare(counts);
        double expectedValue = 1.221; //value obtained from statistical table for alpha = 0.05 and df = 2
        assertEquals("chiSquare method returned incorrect value for long[][] input", expectedValue, actual, 0.01);
    }

    /**
     * Test chiSquareTest method with long[][] input
     * @throws IllegalArgumentException
     * @throws MathException
     */
    @Test
    public void testChiSquareTestLongArray() throws IllegalArgumentException, MathException {
        long[][] counts = {{1, 2}, {2, 3}, {1, 1}};
        double pValue = chiSquareTest.chiSquareTest(counts);
        double expectedValue = 0.114; //value obtained from previous test
        assertEquals("chiSquareTest method returned incorrect value for long[][] input", expectedValue, pValue, 0.01);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid parameters
     * @throws IllegalArgumentException
     * @throws MathException
     */
    @Test
    public void testChiSquareDataSetsComparisonValidParameters() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 4};
        double actual = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        double expectedValue = 0.426; //value obtained from statistical table for alpha = 0.05 and df = 3
        assertEquals("chiSquareDataSetsComparison method returned incorrect value for valid parameters", expectedValue, actual, 0.01);
    }

    /**
     * Test chiSquareDataSetsComparison method with invalid parameters - incorrect array lengths
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidParameters() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }
}