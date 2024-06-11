package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        assertNotNull(instance);
    }

    /**
     * Test constructor with custom distribution.
     */
    @Test
    public void testConstructorWithCustomDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl instance = new ChiSquareTestImpl(distribution);
        assertNotNull(instance);
        assertEquals(distribution, instance.getDistribution());
    }

    /**
     * Test chiSquare method with valid input.
     */
    @Test
    public void testChiSquareWithValidInput() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 1.0, 3.0};
        double result = instance.chiSquare(expected, observed);
        assertEquals(5.366326662735682, result, 0.001);
    }

    /**
     * Test chiSquare method with invalid input (wrong sizes).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInput() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1.0};
        instance.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestWithValidInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 1.0, 3.0};
        double pValue = instance.chiSquareTest(expected, observed);
        assertEquals(0.1342229517543661, pValue, 0.001);
    }

    /**
     * Test chiSquareTest method with invalid input (wrong sizes).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1.0};
        instance.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest with valid input and given significance level.
     */
    @Test
    public void testChiSquareTestWithGivenSignificanceLevel()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 1.0, 3.0};
        boolean result = instance.chiSquareTest(expected, observed, 0.05);
        assertFalse(result);
    }

    /**
     * Test chiSquare method with long[][] counts.
     */
    @Test
    public void testChiSquareWithLongArray() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 2}};
        double result = instance.chiSquare(counts);
        assertEquals(2.0, result, 0.001);
    }

    /**
     * Test chiSquareTest method with long[][] counts.
     */
    @Test
    public void testChiSquareTestWithLongArray() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 2}};
        double pValue = instance.chiSquareTest(counts);
        assertEquals(0.1127423858431663, pValue, 0.001);
    }

    /**
     * Test chiSquareTest with long[][] counts and given significance level.
     */
    @Test
    public void testChiSquareTestWithLongArrayAndSignificanceLevel()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[][] counts = {{2, 3}, {3, 2}};
        boolean result = instance.chiSquareTest(counts, 0.05);
        assertFalse(result);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetsComparisonWithValidInput()
            throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        double result = instance.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.4282235763652985, result, 0.001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInput()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        double pValue = instance.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.15999746768840078, pValue, 0.001);
    }

    /**
     * Test chiSquareTestDataSetsComparison with valid input and given significance level.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithSignificanceLevel()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        boolean result = instance.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }

    /**
     * Test setDistribution method.
     */
    @Test
    public void testSetDistribution() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        ChiSquaredDistributionImpl distribution = new ChiSquaredDistributionImpl(1.0);
        instance.setDistribution(distribution);
        assertEquals(distribution, instance.getDistribution());
    }
}