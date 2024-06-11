package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

/**
 * Test cases for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertEquals("Default constructor", 1.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with custom distribution.
     */
    @Test
    public void testConstructorWithCustomDistribution() {
        ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(5.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals("Custom constructor", 5.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test chiSquare method with valid input.
     */
    @Test
    public void testChiSquareWithValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double result = test.chiSquare(expected, observed);
        assertEquals("chiSquare: expected 6.0, got " + result, 6.0, result, 0.0);
    }

    /**
     * Test chiSquare method with invalid input: wrong array length.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareWithInvalidInput1() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 3.0, 1.0};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input: different array lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareWithInvalidInput2() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input: negative counts.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareWithInvalidInput3() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2.0, 3.0};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestWithValidInput() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals("chiSquareTest: expected < 0.05, got " + pValue, 0.01, pValue, 0.01);
    }

    /**
     * Test chiSquareTest method with invalid input: wrong array length.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput1() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input: different array lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput2() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input: negative counts.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput3() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2.0, 3.0};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with alpha = 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithAlphaZero() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        test.chiSquareTest(expected, observed, 0.0);
    }

    /**
     * Test chiSquareTest method with alpha > 0.5.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithAlphaGreaterThan05() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        test.chiSquareTest(expected, observed, 0.7);
    }

    /**
     * Test chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestWithValidInputAndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertTrue("chiSquareTest: expected true, got " + result, result);
    }

    /**
     * Test chiSquareTest method with invalid input: wrong array length.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput1AndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0};
        test.chiSquareTest(expected, observed, 0.05);
    }

    /**
     * Test chiSquareTest method with invalid input: different array lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput2AndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0};
        test.chiSquareTest(expected, observed, 0.05);
    }

    /**
     * Test chiSquareTest method with invalid input: negative counts.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput3AndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2.0, 3.0};
        test.chiSquareTest(expected, observed, 0.05);
    }

    /**
     * Test chiSquare method with 2x2 input.
     */
    @Test
    public void testChiSquareWith2x2Input() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        double result = test.chiSquare(counts);
        assertEquals("chiSquare: expected 3.0, got " + result, 3.0, result, 0.0);
    }

    /**
     * Test chiSquareTest method with 2x2 input.
     */
    @Test
    public void testChiSquareTestWith2x2Input() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        double pValue = test.chiSquareTest(counts);
        assertEquals("chiSquareTest: expected < 0.05, got " + pValue, 0.01, pValue, 0.01);
    }

    /**
     * Test chiSquare method with invalid 2x2 input: non-square array.
     */
    @Test(expected = IllegalStateException.class)
    public void testChiSquareWithInvalid2x2Input() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        test.chiSquare(counts);
    }

    /**
     * Test chiSquare method with 2x2 input and alpha.
     */
    @Test
    public void testChiSquareTestWith2x2InputAndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        boolean result = test.chiSquareTest(counts, 0.05);
        assertTrue("chiSquareTest: expected true, got " + result, result);
    }

    /**
     * Test chiSquare method with 2x2 input and invalid alpha.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWith2x2InputAndInvalidAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        test.chiSquareTest(counts, 0.7);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetsComparisonWithValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 1};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals("chiSquareDataSetsComparison: expected 2.9999999999999998, got " + result, 3.0, result, 0.01);
    }

    /**
     * Test chiSquareDataSetsComparison method with invalid input: observations of different lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidInput1() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3, 1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareDataSetsComparison method with invalid input: negative counts.
     */
    @Test(expected = MathRuntimeException.class)
    public void testChiSquareDataSetsComparisonWithInvalidInput2() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {-1, 2, 3};
        long[] observed2 = {2, 3, 3, 1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInput() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 1};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals("chiSquareTestDataSetsComparison: expected < 0.05, got " + pValue, 0.01, pValue, 0.01);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid input: observations of different lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInput1() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid input: negative counts.
     */
    @Test(expected = MathRuntimeException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInput2() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {-1, 2, 3};
        long[] observed2 = {2, 3, 3, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 1};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue("chiSquareTestDataSetsComparison: expected true, got " + result, result);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid alpha.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.7);
    }
}