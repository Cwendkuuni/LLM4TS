package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     * @throws MathException if initialization fails
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStdDev() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, dist.getMean(), 0.0);
        assertEquals(1.2, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting mean.
     */
    @Test
    public void testSettingMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(1.5);
        assertEquals(1.5, dist.getMean(), 0.0);
    }

    /**
     * Test setting standard deviation.
     * @throws IllegalArgumentException if standard deviation is not positive
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.5);
    }

    /**
     * Test accessing mean.
     */
    @Test
    public void testAccessingMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);

        dist.setMean(3.2);
        assertEquals(3.2, dist.getMean(), 0.0);
    }

    /**
     * Test accessing standard deviation.
     */
    @Test
    public void testAccessingStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        dist.setStandardDeviation(2.5);
        assertEquals(2.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test cumulative probability method.
     * @throws MathException if the algorithm fails to converge
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();

        double p = 0.3;
        assertEquals(0.1543575625, dist.cumulativeProbability(p), 0.001);

        p = 0.9;
        assertEquals(0.8413447462, dist.cumulativeProbability(p), 0.001);

        p = 2.5;
        assertEquals(0.9993701848, dist.cumulativeProbability(p), 0.001);

        p = -2.5;
        assertEquals(0.0006298152, dist.cumulativeProbability(p), 0.001);
    }

    /**
     * Test inverse cumulative probability method with valid probabilities.
     * @throws MathException if the inverse cumulative probability can not be
     *         computed due to convergence or other numerical errors
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidProbabilities() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();

        double p = 0.25;
        assertEquals(0.6826894922, dist.inverseCumulativeProbability(p), 0.01);

        p = 0.75;
        assertEquals(1.2758982675, dist.inverseCumulativeProbability(p), 0.01);
    }

    /**
     * Test inverse cumulative probability method with 0 probability.
     * @throws MathException if the inverse cumulative probability can not be
     *         computed due to convergence or other numerical errors
     */
    @Test
    public void testInverseCumulativeProbabilityWithZeroProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0), 0.0);
    }

    /**
     * Test inverse cumulative probability method with 1 probability.
     * @throws MathException if the inverse cumulative probability can not be
     *         computed due to convergence or other numerical errors
     */
    @Test
    public void testInverseCumulativeProbabilityWithOneProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1), 0.0);
    }

    /**
     * Test getDomainLowerBound method.
     * @throws IllegalArgumentException if p is not a valid probability
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDomainLowerBoundWithInvalidProbability() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.getDomainLowerBound(-0.5);
    }

    /**
     * Test getDomainLowerBound method with probability less than 0.5.
     */
    @Test
    public void testGetDomainLowerBoundWithProbabilityLessThan05() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.2), 0.0);
    }

    /**
     * Test getDomainLowerBound method with probability equal to 0.5.
     */
    @Test
    public void testGetDomainLowerBoundWithProbabilityEqualTo05() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getDomainLowerBound(0.5), 0.0);
    }

    /**
     * Test getDomainLowerBound method with probability greater than 0.5.
     */
    @Test
    public void testGetDomainLowerBoundWithProbabilityGreaterThan05() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(getMean(dist), dist.getDomainLowerBound(0.8), 0.0);
    }

    /**
     * Test getDomainUpperBound method.
     * @throws IllegalArgumentException if p is not a valid probability
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDomainUpperBoundWithInvalidProbability() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.getDomainUpperBound(-0.5);
    }

    /**
     * Test getDomainUpperBound method with probability less than 0.5.
     */
    @Test
    public void testGetDomainUpperBoundWithProbabilityLessThan05() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(getMean(dist), dist.getDomainUpperBound(0.2), 0.0);
    }

    /**
     * Test getDomainUpperBound method with probability equal to 0.5.
     */
    @Test
    public void testGetDomainUpperBoundWithProbabilityEqualTo05() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.5), 0.0);
    }

    /**
     * Test getDomainUpperBound method with probability greater than 0.5.
     */
    @Test
    public void testGetDomainUpperBoundWithProbabilityGreaterThan05() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.8), 0.0);
    }

    /**
     * Test getInitialDomain method.
     * @throws IllegalArgumentException if p is not a valid probability
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInitialDomainWithInvalidProbability() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.getInitialDomain(-0.5);
    }

    /**
     * Test getInitialDomain method with probability less than 0.5.
     */
    @Test
    public void testGetInitialDomainWithProbabilityLessThan05() {
        NormalDistribution dist = new NormalDistributionImpl();
        double mean = dist.getMean();
        assertEquals(mean - dist.getStandardDeviation(), dist.getInitialDomain(0.2), 0.0);
    }

    /**
     * Test getInitialDomain method with probability equal to 0.5.
     */
    @Test
    public void testGetInitialDomainWithProbabilityEqualTo05() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(mean(dist), dist.getInitialDomain(0.5), 0.0);
    }

    /**
     * Test getInitialDomain method with probability greater than 0.5.
     */
    @Test
    public void testGetInitialDomainWithProbabilityGreaterThan05() {
        NormalDistribution dist = new NormalDistributionImpl();
        double mean = dist.getMean();
        assertEquals(mean + dist.getStandardDeviation(), dist.getInitialDomain(0.8), 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with negative standard deviation.
     * @throws MathException if the inverse cumulative probability can not be
     *         computed due to convergence or other numerical errors
     */
    @Test
    public void testInverseCumulativeProbabilityWithNegativeStandardDeviation() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.2);

        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(0.5));
    }

    /**
     * Test inverseCumulativeProbability method with standard deviation equal to 0.
     * @throws MathException if the inverse cumulative probability can not be
     *         computed due to convergence or other numerical errors
     */
    @Test
    public void testInverseCumulativeProbabilityWithZeroStandardDeviation() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(0);

        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(0.5));
    }

    /**
     * Test inverseCumulativeProbability method with mean equal to negative infinity.
     * @throws MathException if the inverse cumulative probability can not be
     *         computed due to convergence or other numerical errors
     */
    @Test
    public void testInverseCumulativeProbabilityWithMeanEqualToNegativeInfinity() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(Double.NEGATIVE_INFINITY);

        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(0.5), 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with mean equal to positive infinity.
     * @throws MathException if the inverse cumulative probability can not be
     *         computed due to convergence or other numerical errors
     */
    @Test
    public void testInverseCumulativeProbabilityWithMeanEqualToPositiveInfinity() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(Double.POSITIVE_INFINITY);

        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.5), 0.0);
    }

    /**
     * Helper method to get the mean of a NormalDistribution instance.
     * @param dist the NormalDistribution instance
     * @return the mean value
     */
    private double getMean(NormalDistribution dist) {
        return dist.getMean();
    }

    /**
     * Helper method to get the mean value of the NormalDistribution instance.
     * @param dist the NormalDistribution instance
     * @return the mean value
     */
    private double mean(NormalDistribution dist) {
        return dist.getMean();
    }
}