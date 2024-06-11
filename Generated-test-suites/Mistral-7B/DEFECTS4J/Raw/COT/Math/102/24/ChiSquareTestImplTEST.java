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

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }

    /**
     * Test constructor with given distribution.
     */
    @Test
    public void testConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertNotNull(test);
        assertEquals(distribution, test.getDistribution());
    }

    /**
     * Test chiSquare method with valid inputs.
     */
    @Test
    public void testChiSquareWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(4.0, chiSquare, 1e-10);
    }

    /**
     * Test chiSquare method with invalid inputs: incorrect length of expected and observed arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2, 3};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid inputs: negative expected and observed counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0};
        long[] observed = {2, -3};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     */
    @Test
    public void testChiSquareTestWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double pValue = test.chiSquareTest(expected, observed);
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(3.0);
        double df = 2.0;
        double criticalValue = dist.inverseCumulativeProbability(0.01);
        assertEquals(1.0 - pValue, criticalValue, 1e-10);
    }

    /**
     * Test chiSquareTest method with invalid inputs: incorrect length of expected and observed arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2, 3};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid inputs: non-positive significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        test.chiSquareTest(expected, observed, -1.0);
    }

    /**
     * Test chiSquare method with long[][] counts.
     */
    @Test
    public void testChiSquareWithLongArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double chiSquare = test.chiSquare(counts);
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(3.0);
        double df = 2.0 * 2.0;
        double criticalValue = dist.inverseCumulativeProbability(0.05);
        assertEquals(1.0 - 0.05, chiSquare / criticalValue, 1e-10);
    }

    /**
     * Test chiSquareTest method with long[][] counts and significance level.
     */
    @Test
    public void testChiSquareTestWithLongArraySignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double pValue = test.chiSquareTest(counts);
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(3.0);
        double df = 2.0 * 2.0;
        double criticalValue = dist.inverseCumulativeProbability(0.05);
        assertEquals(pValue, 1.0 - criticalValue, 1e-10);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareDataSetsComparisonWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(3.0);
        double df = 3.0;
        double criticalValue = dist.inverseCumulativeProbability(0.05);
        assertEquals(1.0 - 0.05, chiSquare / criticalValue, 1e-10);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(3.0);
        double df = 3.0;
        double criticalValue = dist.inverseCumulativeProbability(0.05);
        assertEquals(pValue, 1.0 - criticalValue, 1e-10);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid inputs: non-matching array lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }
}