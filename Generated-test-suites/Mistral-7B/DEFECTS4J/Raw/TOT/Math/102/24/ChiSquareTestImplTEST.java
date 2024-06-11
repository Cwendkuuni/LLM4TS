package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    /** Test subject. */
    private ChiSquareTestImpl test;

    /** Distribution factory. */
    private DistributionFactory factory;

    /**
     * Sets up test.
     */
    @SuppressWarnings("resource")
    @Before
    public void setUp() {
        factory = new DistributionFactory();
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
    }

    /**
     * Test chi square statistic computation with valid input.
     * @throws MathIllegalArgumentException if the input is invalid
     */
    @Test
    public void testChiSquareStatisticValid() throws MathIllegalArgumentException {
        long[] expected = { (long) 10, (long) 15 };
        long[] observed = { 11, 14 };
        assertEquals(12.314913555588581, test.chiSquare(expected, observed), 0.001);
    }

    /**
     * Test chi square p-value computation with valid input.
     * @throws MathIllegalArgumentException if the input is invalid
     * @throws MathRuntimeError if an error occurs computing the p-value
     */
    @Test
    public void testChiSquarePValueValid() throws MathIllegalArgumentException, MathRuntimeError {
        long[] expected = { (long) 10, (long) 15 };
        long[] observed = { 11, 14 };
        assertEquals(0.021126824716691, test.chiSquareTest(expected, observed), 0.0001);
    }

    /**
     * Test chi square test with valid input and significance level.
     * @throws MathIllegalArgumentException if the input is invalid
     * @throws MathRuntimeError if an error occurs performing the test
     */
    @Test
    public void testChiSquareTestValid() throws MathIllegalArgumentException, MathRuntimeError {
        long[] expected = { (long) 10, (long) 15 };
        long[] observed = { 11, 14 };
        assertTrue(test.chiSquareTest(expected, observed, 0.05));
    }

    /**
     * Test chi square statistic computation with invalid input length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareStatisticInvalidLength() {
        long[] expected = { 1, 2 };
        long[] observed = { 1, 2, 3 };
        test.chiSquare(expected, observed);
    }

    /**
     * Test chi square statistic computation with invalid input length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidLength() {
        long[] expected = { 1, 2 };
        long[] observed = { 1, 2, 3 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chi square statistic computation with invalid input length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidLength2() {
        long[] expected = { 1, 2 };
        long[] observed = { 1, 2 };
        test.chiSquareTest(expected, observed, 0.05);
    }

    /**
     * Test chi square p-value computation with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquarePValueInvalid() {
        long[] expected = { 1, 2 };
        long[] observed = { 1, 2 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chi square test with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalid() {
        long[] expected = { 1, 2 };
        long[] observed = { 1, 2 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chi square test with invalid significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidSignificance() {
        long[] expected = { 1, 2 };
        long[] observed = { 1, 2 };
        test.chiSquareTest(expected, observed, 1.5);
    }

    /**
     * Test chi square test with negative expected counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegativeExpected() {
        long[] expected = { -1, 2 };
        long[] observed = { 1, 2 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chi square test with negatiobserved counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegativeObserved() {
        long[] expected = { 1, 2 };
        long[] observed = { -1, 2 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chi square test with input arrays of different lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDifferentLengths() {
        long[] expected = { 1, 2 };
        long[] observed = { 1, 2, 3 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chi square test with inconsistent input arrays sums.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInconsistentSum() {
        long[] expected = { 1, 1 };
        long[] observed = { 1, 2 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test 2-way table comparison with valid input.
     * @throws MathIllegalArgumentException if the input is invalid
     * @throws MathRuntimeError if an error occurs performing the test
     */
    @Test
    public void testTwoWayComparisonValid() throws MathIllegalArgumentException, MathRuntimeError {
        long[] observed1 = { 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6 };
        long[] observed2 = { 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6 };
        assertTrue(test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05));
    }

    /**
     * Test 2-way table comparison with different sums.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTwoWayComparisonDifferentSum() {
        long[] observed1 = { 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6 };
        long[] observed2 = { 1, 2, 2, 3, 3, 4, 4, 4, 4, 5, 5, 5, 6, 6 };
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    /**
     * Test 2-way table comparison with inconsistent input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTwoWayComparisonInconsistent() {
        long[] observed1 = { 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6 };
        long[] observed2 = { 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 7, 6, 6 };
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    /**
     * Test 2-way table comparison with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTwoWayComparisonNegative() {
        long[] observed1 = { 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6 };
        long[] observed2 = { 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, -7, 6, 6 };
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    /**
     * Test distribution setter.
     */
    @Test
    public void testSetDistribution() {
        DistributionFactory newFactory = new DistributionFactory();
        ChiSquaredDistribution newDist = new ChiSquaredDistributionImpl(2.0);
        test.setDistribution(newDist);
        assertEquals(newDist, test.getDistribution());
    }
}