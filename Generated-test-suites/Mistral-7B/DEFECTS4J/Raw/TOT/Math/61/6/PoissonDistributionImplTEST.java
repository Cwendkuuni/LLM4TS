package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Tests the constructor with valid mean value.
     */
    @Test
    public void testValidConstruction() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, distribution.getMean(), 0.001);
    }

    /**
     * Tests the constructor with negative mean value.
     */
    @Test(expected = MathRuntimeException.class)
    public void testInvalidConstructionNegativeMean() {
        new PoissonDistributionImpl(-0.5);
    }

    /**
     * Tests the probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.223139435312919, distribution.probability(2), 0.001);
    }

    /**
     * Tests the probability method with invalid input.
     */
    @Test
    public void testProbabilityInvalidInput() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(1.0, distribution.probability(Integer.MAX_VALUE), 0.0);
    }

    /**
     * Tests the cumulative probability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.100782463549971, distribution.cumulativeProbability(1), 0.001);
    }

    /**
     * Tests the cumulative probability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.cumulativeProbability(-1);
    }

    /**
     * Tests the normalApproximateProbability method with valid input.
     */
    @Test
    public void testNormalApproximateProbabilityValidInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.223139435312919, distribution.normalApproximateProbability(2), 0.001);
    }

    /**
     * Tests the normalApproximateProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityInvalidInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.normalApproximateProbability(-1);
    }

    /**
     * Tests the sample method with valid mean.
     */
    @Test
    public void testSampleValidMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        int sampledValue = distribution.sample();
        assertEquals(2, sampledValue, 0);
    }

    /**
     * Tests the sample method with large mean.
     */
    @Test
    public void testSampleLargeMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(50.0);
        int sampledValue = distribution.sample();
        assertEquals(51, sampledValue, 0);
    }

    /**
     * Tests the inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        double p = 0.3;
        double expected = 1;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.001);
    }

    /**
     * Tests the inverseCumulativeProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.inverseCumulativeProbability(1.5);
    }

    /**
     * Tests the inverseCumulativeProbability method with invalid input - invalid probability value.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.inverseCumulativeProbability(1.6);
    }
}