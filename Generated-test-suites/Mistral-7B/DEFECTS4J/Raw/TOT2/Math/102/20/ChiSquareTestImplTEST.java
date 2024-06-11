package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Tests for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor without initializing distribution.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    /**
     * Test constructor with initializing distribution.
     */
    @Test
    public void testConstructorWithDistribution() {
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals(dist, test.getDistribution());
    }

    /**
     * Test chiSquare method with valid input.
     */
    @Test
    public void testChiSquareValidInput() {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 3.5, 1.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double result = test.chiSquare(expected, observed);
        assertEquals(1.6666666666666668, result, 1e-6);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput() {
        long[] observed = {1, 2};
        double[] expected = {2.5, 3.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquare(expected, observed);
    }

    /**
     * TestchiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() throws MathException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 3.5, 1.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double alpha = 0.01;
        double result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result < alpha);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput() {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 3.5, 1.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed, 1.1);
    }

    /**
     * Test chiSquare method with 2-way table.
     */
    @Test
    public void testChiSquare2WayTable() throws IllegalArgumentException {
        long[][] counts = {{1, 2}, {3, 4}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double result = test.chiSquare(counts);
        assertEquals(2.777777777777778, result, 1e-6);
    }

    /**
     * Test chiSquareTest method with 2-way table.
     */
    @Test
    public void testChiSquareTest2WayTable() throws IllegalArgumentException, MathException {
        long[][] counts = {{1, 2}, {3, 4}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double alpha = 0.01;
        double result = test.chiSquareTest(counts, alpha);
        assertTrue(result < alpha);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetComparisonValidInput() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.0, result, 1e-6);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetComparisonValidInput() throws MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double alpha = 0.01;
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result < alpha);
    }
}