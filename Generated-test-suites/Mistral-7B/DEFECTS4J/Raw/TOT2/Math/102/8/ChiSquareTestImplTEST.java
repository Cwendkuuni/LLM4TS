package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl
 */
public class ChiSquareTestImplTest {

    /** Distribution used to compute inference statistics. */
    private ChiSquareTestImpl chiSquareTest;

    /**
     * Set up before each test method.
     */
    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquare(long[], long[])}.
     */
    @Test
    public void testChiSquare() {
        // Positive and non-zero expected and observed data
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 2, 3, 4};
        double result = chiSquareTest.chiSquare(expected, observed);
        double expectedResult = 2.405;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(long[], long[])}.
     */
    @Test
    public void testChiSquareTest() throws MathException {
        // Positive and non-zero expected and observed data
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 2, 3, 4};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        double expectedPValue = 0.496;
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(long[], long[], double)}.
     */
    @Test
    public void testChiSquareTestSignificanceLevel() throws MathException, IllegalArgumentException {
        // Positive and non-zero expected and observed data
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 2, 3, 4};
        double pValue = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        boolean expectedResult = true;
        assertTrue(expectedResult == chiSquareTest.chiSquareTest(expected, observed, 0.05));
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquare(long[][])}.
     */
    @Test
    public void testChiSquare2wayTable() throws IllegalArgumentException {
        // Positive and non-zero data
        long[][] counts = {{1, 2, 3, 4}, {2, 2, 3, 4}};
        double result = chiSquareTest.chiSquare(counts);
        double expectedResult = 6.605;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(long[][])}.
     */
    @Test
    public void testChiSquareTest2wayTable() throws MathException, IllegalArgumentException {
        // Positive and non-zero data
        long[][] counts = {{1, 2, 3, 4}, {2, 2, 3, 4}};
        double pValue = chiSquareTest.chiSquareTest(counts);
        double expectedPValue = 0.252;
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(long[][], double)}.
     */
    @Test
    public void testChiSquareTestSignificanceLevel2wayTable() throws MathException, IllegalArgumentException {
        // Positive and non-zero data
        long[][] counts = {{1, 2, 3, 4}, {2, 2, 3, 4}};
        double pValue = chiSquareTest.chiSquareTest(counts, 0.05);
        boolean expectedResult = true;
        assertTrue(expectedResult == chiSquareTest.chiSquareTest(counts, 0.05));
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareDataSetsComparison(long[], long[])}.
     */
    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        // Positive and non-zero data
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 0.251;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])}.
     */
    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        // Positive and non-zero data
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.252;
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[], double)}.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonSignificanceLevel() throws IllegalArgumentException, MathException {
        // Positive and non-zero data
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        boolean expectedResult = true;
        assertTrue(expectedResult == chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05));
    }
}