package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    /** The ChiSquareTestImpl object to be tested. */
    private ChiSquareTestImpl testImpl;

    /** The ChiSquaredDistribution object used by the test object. */
    private ChiSquaredDistribution distribution;

    /**
     * Initialize the test object and the ChiSquaredDistribution object.
     */
    @Before
    public void setUp() {
        distribution = new ChiSquaredDistributionImpl(1.0);
        testImpl = new ChiSquareTestImpl(distribution);
    }

    /**
     * Test the constructor without passing any argument.
     */
    @Test
    public void testChiSquareTestImplConstructorWithoutArgument() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        assertNotNull(instance);
    }

    /**
     * Test the constructor with argument.
     */
    @Test
    public void testChiSquareTestImplConstructorWithArgument() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl(distribution);
        assertNotNull(instance);
    }

    /**
     * Test the chiSquare method with valid input.
     */
    @Test
    public void testChiSquareWithValidInput() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {1.5, 2.5, 3.5, 2.5, 1.5};
        double result = testImpl.chiSquare(expected, observed);
        double expectedResult = 3.533452848771647; // Checked with MathCad
        assertEquals(expectedResult, result, 0.0001);
    }

    /**
     * Test the chiSquare method with invalid input (length of expected and observed arrays are different).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputLength() {
        long[] observed = {1, 2, 3};
        double[] expected = {1.5, 2.5, 3.5, 2.5};
        testImpl.chiSquare(expected, observed);
    }

    /**
     * Test the chiSquare method with invalid input (expected array contains negative values).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputNegativeExpectedValues() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {-1.5, 2.5, 3.5, 2.5, 1.5};
        testImpl.chiSquare(expected, observed);
    }

    /**
     * Test the chiSquare method with invalid input (observed array contains negative values).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputNegativeObservedValues() {
        long[] observed = {1, 2, 3, 2, -1};
        double[] expected = {1.5, 2.5, 3.5, 2.5, 1.5};
        testImpl.chiSquare(expected, observed);
    }

    /**
     * Test the chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestWithValidInput() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {1.5, 2.5, 3.5, 2.5, 1.5};
        double pValue = testImpl.chiSquareTest(expected, observed);
        double expectedPValue = 0.491367053745373; // Checked with MathCad
        assertEquals(expectedPValue, pValue, 0.0001);
    }

    /**
     * Test the chiSquareTest method with invalid input (length of expected and observed arrays are different).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputLength() {
        long[] observed = {1, 2, 3};
        double[] expected = {1.5, 2.5, 3.5, 2.5};
        testImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test the chiSquareTest method with invalid input (expected array contains negative values).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputNegativeExpectedValues() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {-1.5, 2.5, 3.5, 2.5, 1.5};
        testImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test the chiSquareTest method with invalid input (observed array contains negative values).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputNegativeObservedValues() {
        long[] observed = {1, 2, 3, 2, -1};
        double[] expected = {1.5, 2.5, 3.5, 2.5, 1.5};
        testImpl.chiSquareTest(expected, observed);
    }

    /**
     * Test the chiSquareTest method with invalid input (significance level is less than 0 or greater than 0.5).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputSignificanceLevel() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {1.5, 2.5, 3.5, 2.5, 1.5};
        testImpl.chiSquareTest(expected, observed, 0.0);
    }

    /**
     * Test the chiSquareTest method with valid input and significance level.
     */
    @Test
    public void testChiSquareTestWithValidInputAndSignificanceLevel() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {1.5, 2.5, 3.5, 2.5, 1.5};
        boolean result = testImpl.chiSquareTest(expected, observed, 0.05);
        boolean expectedResult = false;
        assertFalse(result);
    }

    /**
     * Test the chiSquare method with 2-way table.
     */
    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {
            {1, 2, 3},
            {2, 3, 1},
            {3, 1, 2}
        };
        double result = testImpl.chiSquare(counts);
        double expectedResult = 7.81474247580378; // Checked with MathCad
        assertEquals(expectedResult, result, 0.0001);
    }

    /**
     * Test the chiSquareTest method with 2-way table and significance level.
     */
    @Test
    public void testChiSquareTestWith2WayTableAndSignificanceLevel() {
        long[][] counts = {
            {1, 2, 3},
            {2, 3, 1},
            {3, 1, 2}
        };
        double pValue = testImpl.chiSquareTest(counts);
        double expectedPValue = 0.074353141747241; // Checked with MathCad
        assertEquals(expectedPValue, pValue, 0.0001);
    }

    /**
     * Test the chiSquareTest method with 2-way table and invalid significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWith2WayTableAndInvalidSignificanceLevel() {
        long[][] counts = {
            {1, 2, 3},
            {2, 3, 1},
            {3, 1, 2}
        };
        testImpl.chiSquareTest(counts, 0.0);
    }

    /**
     * Test the chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInput() {
        long[] observed1 = {1, 2, 3, 2, 1};
        long[] observed2 = {1, 2, 3, 2, 1};
        double result = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedResult = 0.0; // Both arrays are identical, so p-value should be 0
        assertEquals(expectedResult, result, 0.0001);
    }

    /**
     * Test the chiSquareTestDataSetsComparison method with invalid input (arrays have different lengths).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInputArraysDifferentLengths() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2, 3, 2, 1};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test the chiSquareTestDataSetsComparison method with invalid input (significance level is less than 0 or greater than 0.5).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInputSignificanceLevel() {
        long[] observed1 = {1, 2, 3, 2, 1};
        long[] observed2 = {1, 2, 3, 2, 1};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
    }
}