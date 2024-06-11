package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest);
        assertNotNull(chiSquareTest.getDistribution());
        assertEquals(new ChiSquaredDistributionImpl(1.0), chiSquareTest.getDistribution());
    }

    /**
     * Test constructor with custom ChiSquaredDistribution.
     */
    @Test
    public void testCustomConstructor() {
        ChiSquaredDistribution chiSquaredDist = new ChiSquaredDistributionImpl(3.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(chiSquaredDist);
        assertNotNull(chiSquareTest);
        assertNotNull(chiSquareTest.getDistribution());
        assertEquals(chiSquaredDist, chiSquareTest.getDistribution());
    }

    /**
     * Test chiSquare method with valid input.
     */
    @Test
    public void testChiSquareValidInput() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double chiSquareStat = chiSquareTest.chiSquare(expected, observed);
        assertEquals(5.991_489_832_452_001_026_000_000_000_001d, chiSquareStat, 0.001);
    }

    /**
     * Test chiSquare method with invalid input - wrong array lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputLengthMismatch() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input - negative expected frequencies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputNegativeExpected() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0};
        long[] observed = {2, 3};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertTrue(pValue > 0.04);
    }

    /**
     * Test chiSquareTest method with invalid input - wrong array lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputLengthMismatch() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 2};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input - negative expected frequencies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputNegativeExpected() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0};
        long[] observed = {2, 3};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquare method with valid input - 2x2 contingency table.
     */
    @Test
    public void testChiSquare2x2() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 2}};
        double chiSquareStat = chiSquareTest.chiSquare(counts);
        assertEquals(2.776_021_234_415_445_079_436_380_775_825_000_000_000_000_001d, chiSquareStat, 0.001);
        double pValue = chiSquareTest.chiSquareTest(counts);
        assertTrue(pValue > 0.05);
    }

    /**
     * Test chiSquare method with valid input - large 2-way table.
     */
    @Test
    public void testChiSquareLargeTable() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = new long[5][5];
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[i].length; j++) {
                counts[i][j] = 100;
            }
        }
        double chiSquareStat = chiSquareTest.chiSquare(counts);
        double expectedChiSquare = 1247.051_135_377_929_274_020_404_068_401_010_721_000_000_000_000_001d;
        assertEquals(expectedChiSquare, chiSquareStat, 0.001);
        double pValue = chiSquareTest.chiSquareTest(counts, 0.05);
        assertFalse(pValue > 0.05);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetsComparisonValidInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 1, 3, 4, 5};
        double chiSquareStat = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(2.776_021_234_415_445_079_436_380_775_825_000_000_000_000_001d, chiSquareStat, 0.001);
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue > 0.05);
    }

    /**
     * Test chiSquareDataSetsComparison method with invalid input - different array lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidInputLengthMismatch() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {1, 2, 3, 4, 5};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(pValue > 0.05);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid input - different array lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputLengthMismatch() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {1, 2, 3, 4, 5};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }
}