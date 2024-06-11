package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl.
 */
public class ChiSquareTestImplTest {

    private long[] expected = {1, 2, 3, 2, 1};
    private long[] observed = {2, 3, 1, 3, 2};
    private long[][] counts = {{1, 2, 3, 2, 1}, {2, 3, 1, 3, 2}};

    /**
     * Tests the constructor that uses the default Chi-Square distribution.
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    /**
     * Tests the constructor that uses a specified Chi-Square distribution.
     */
    @Test
    public void testSpecifiedConstructor() {
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(5.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals(dist, test.getDistribution());
    }

    /**
     * Tests the chiSquare method with valid input.
     */
    @Test
    public void testChiSquareValidInput() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(5.671334438122843, chiSquare, 0.0001);
    }

    /**
     * Tests the chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.02425747023333333, pValue, 0.0001);
    }

    /**
     * Tests the chiSquareTest method with valid input and a specified alpha value.
     */
    @Test
    public void testChiSquareTestWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    /**
     * Tests the chiSquare method with invalid input (null expected or observed arrays).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquare(null, observed);
    }

    /**
     * Tests the chiSquare method with invalid input (mismatched expected and observed arrays).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMismatchedArrays() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] invalidExpected = {1, 2};
        test.chiSquare(invalidExpected, observed);
    }

    /**
     * Tests the chiSquare method with invalid input (negative expected or observed counts).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegativeCounts() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] invalidObserved = {-1, 2};
        test.chiSquare(expected, invalidObserved);
    }

    /**
     * Tests the chiSquareTest method with invalid input (null expected or observed arrays).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(null, observed);
    }

    /**
     * Tests the chiSquareTest method with invalid input (mismatched expected and observed arrays).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMismatchedArrays() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] invalidExpected = {1, 2};
        test.chiSquareTest(invalidExpected, observed);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.02425747023333333, pValue, 0.0001);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with valid input and a specified alpha value.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with invalid input (null observed1 or observed2 arrays).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTestDataSetsComparison(null, observed2);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with invalid input (mismatched observed1 and observed2 arrays).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMismatchedArrays() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] invalidObserved1 = {1, 2};
        test.chiSquareTestDataSetsComparison(observed1, invalidObserved1);
    }
}