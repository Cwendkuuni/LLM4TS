package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
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
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        assertNotNull(chiSquareTestImpl);
    }

    /**
     * Test constructor with given distribution.
     */
    @Test
    public void testConstructorWithDistribution() {
        ChiSquareDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(chiSquaredDistribution);
        assertNotNull(chiSquareTestImpl);
        assertEquals(chiSquaredDistribution, chiSquareTestImpl.getDistribution());
    }

    /**
     * Test chiSquare method with valid input.
     */
    @Test
    public void testChiSquareWithValidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 1.0, 3.0};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(5.0, result, 1e-6);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2.0};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestWithValidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 1.0, 3.0};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        double significanceLevel = 0.05;
        assertTrue(pValue < significanceLevel);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2.0};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with significant result.
     */
    @Test
    public void testChiSquareTestWithSignificantResult() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1.0, 3.0, 2.0};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        double significanceLevel = 0.01;
        assertTrue(pValue < significanceLevel);
    }

    /**
     * Test chiSquareTest method with non-significant result.
     */
    @Test
    public void testChiSquareTestWithNonSignificantResult() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 2.0, 2.0};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        double significanceLevel = 0.05;
        assertTrue(pValue >= significanceLevel);
    }

    /**
     * Test chiSquareTest method with invalid significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 1.0, 3.0};
        chiSquareTestImpl.chiSquareTest(expected, observed, -1.0);
    }

    /**
     * Test chiSquare method with long[][] input.
     */
    @Test
    public void testChiSquareWithLongArrayInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 2}};
        double result = chiSquareTestImpl.chiSquare(counts);
        assertEquals(2.0, result, 1e-6);
    }

    /**
     * Test chiSquareTest method with long[][] input and valid p-value.
     */
    @Test
    public void testChiSquareTestWithLongArrayInputAndValidPValue() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 2}};
        double significanceLevel = 0.05;
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        assertTrue(pValue < significanceLevel);
    }

    /**
     * Test chiSquareTest method with long[][] input and significant result.
     */
    @Test
    public void testChiSquareTestWithLongArrayInputAndSignificantResult() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 4}};
        double significanceLevel = 0.01;
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        assertTrue(pValue < significanceLevel);
    }

    /**
     * Test chiSquareTest method with long[][] input and non-significant result.
     */
    @Test
    public void testChiSquareTestWithLongArrayInputAndNonSignificantResult() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 2}, {2, 2}};
        double significanceLevel = 0.05;
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        assertTrue(pValue >= significanceLevel);
    }

    /**
     * Test chiSquareTest method with long[][] input and invalid significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithLongArrayInputAndInvalidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 2}};
        chiSquareTestImpl.chiSquareTest(counts, -1.0);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetsComparisonWithValidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {3, 2, 3};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(2.0, result, 1e-6);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid p-value.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidPValue() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {3, 2, 3};
        double significanceLevel = 0.05;
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue < significanceLevel);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with significant result.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithSignificantResult() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 5, 3};
        double significanceLevel = 0.01;
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue < significanceLevel);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with non-significant result.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithNonSignificantResult() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {3, 3, 3};
        double significanceLevel = 0.05;
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue >= significanceLevel);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {3, 2, 3};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, -1.0);
    }
}