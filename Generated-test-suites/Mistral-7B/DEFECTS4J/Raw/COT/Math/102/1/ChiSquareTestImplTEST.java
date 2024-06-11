package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    /**
     * Instance under test.
     */
    private ChiSquareTestImpl chiSquareTest;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    /**
     * Tests constructor with no arguments.
     */
    @Test
    public void testConstructor() {
        assertNotNull(chiSquareTest);
    }

    /**
     * Tests constructor with ChiSquaredDistribution argument.
     */
    @Test
    public void testConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(2.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(distribution);
        assertNotNull(chiSquareTest);
        assertEquals(distribution, chiSquareTest.getDistribution());
    }

    /**
     * Tests {@code chiSquare()} method with valid arguments.
     */
    @Test
    public void testChiSquareWithValidArguments() {
        long[] observed = {1, 2, 3, 4, 5, 5, 5, 5, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
        double chiSquare = chiSquareTest.chiSquare(expected, observed);
        assertEquals(11.071428571428572, chiSquare, 1e-6);
    }

    /**
     * Tests {@code chiSquare()} method with invalid arguments (different lengths).
     */
    @Test
    public void testChiSquareWithInvalidLengths() {
        double[] expected = {2.5};
        long[] observed = {1, 2, 3, 4, 5};
        assertThrows(MathIllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    /**
     * Tests {@code chiSquare()} method with invalid arguments (negative expected values).
     */
    @Test
    public void testChiSquareWithNegativeExpectedValues() {
        double[] expected = {-1.5, 2.5, 2.5};
        long[] observed = {1, 2, 3};
        assertThrows(MathIllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    /**
     * Tests {@code chiSquareTest()} method with valid arguments.
     */
    @Test
    public void testChiSquareTestWithValidArguments() throws MathException {
        long[] observed = {1, 2, 3, 4, 5, 5, 5, 5, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.024963393166378555, pValue, 1e-6);
    }

    /**
     * Tests {@code chiSquareTest()} method with invalid arguments (different lengths).
     */
    @Test
    public void testChiSquareTestWithInvalidLengths() {
        double[] expected = {2.5};
        long[] observed = {1, 2, 3, 4, 5};
        assertThrows(MathIllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed));
    }

    /**
     * Tests {@code chiSquareTest()} method with invalid arguments (negative expected values).
     */
    @Test
    public void testChiSquareTestWithNegativeExpectedValues() {
        double[] expected = {-1.5, 2.5, 2.5};
        long[] observed = {1, 2, 3};
        assertThrows(MathIllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed));
    }

    /**
     * Tests {@code chiSquareTest()} method with valid arguments and
     * given significance level.
     */
    @Test
    public void testChiSquareTestWithValidArgumentsAndAlpha() throws MathException {
        long[] observed = {1, 2, 3, 4, 5, 5, 5, 5, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
        boolean rejectNullHypothesis = chiSquareTest(expected, observed, 0.05);
        assertTrue(rejectNullHypothesis);
    }

    /**
     * Tests {@code chiSquareTest()} method with invalid arguments
     * (different lengths) and given significance level.
     */
    @Test
    public void testChiSquareTestWithInvalidLengthsAndAlpha() {
        double[] expected = {2.5};
        long[] observed = {1, 2, 3, 4, 5};
        assertThrows(MathIllegalArgumentException.class,
                () -> chiSquareTest(expected, observed, 0.05));
    }

    /**
     * Tests {@code chiSquareTest()} method with invalid arguments
     * (negative expected values) and given significance level.
     */
    @Test
    public void testChiSquareTestWithNegativeExpectedValuesAndAlpha() {
        double[] expected = {-1.5, 2.5, 2.5};
        long[] observed = {1, 2, 3};
        assertThrows(MathIllegalArgumentException.class,
                () -> chiSquareTest(expected, observed, 0.05));
    }

    /**
     * Tests {@code chiSquare()} method with 2-way table.
     */
    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double chiSquare = chiSquareTest.chiSquare(counts);
        assertEquals(7.778489753623188, chiSquare, 1e-6);
    }

    /**
     * Tests {@code chiSquareTest()} method with 2-way table and given significance level.
     */
    @Test
    public void testChiSquareTestWith2WayTableAndAlpha() throws MathException {
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = chiSquareTest.chiSquareTest(counts, 0.05);
        assertFalse(pValue < 0.05);
    }

    /**
     * Tests {@code chiSquareDataSetsComparison()} method with valid arguments.
     */
    @Test
    public void testChiSquareDataSetComparisonWithValidArguments() throws MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        double chiSquare = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(2.201564654441365, chiSquare, 1e-6);
    }

    /**
     * Tests {@code chiSquareTestDataSetsComparison()} method with valid arguments.
     */
    @Test
    public void testChiSquareTestDataSetComparisonWithValidArguments() throws MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.3344638233382055, pValue, 1e-6);
    }

    /**
     * Tests {@code chiSquareTestDataSetsComparison()} method with valid arguments and given significance level.
     */
    @Test
    public void testChiSquareTestDataSetComparisonWithValidArgumentsAndAlpha() throws MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        boolean rejectNullHypothesis = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    /**
     * Tests {@code checkArray()} method.
     */
    @Test
    public void testCheckArray() {
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}, {4, 5, 6}};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.checkArray(counts));
    }

    /**
     * Tests {@code chiSquareTest} method with invalid arguments.
     */
    @Test
    public void testChiSquareTestWithInvalidArguments() {
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, null}};
        assertThrows(NullPointerException.class, () -> chiSquareTest.chiSquare(counts));
    }
}