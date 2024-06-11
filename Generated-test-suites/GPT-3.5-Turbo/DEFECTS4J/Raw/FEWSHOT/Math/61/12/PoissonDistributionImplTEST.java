
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PoissonDistributionImplTest {
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        double mean = distribution.getMean();
        assertEquals(5.0, mean, 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        double probability = distribution.probability(3);
        assertEquals(0.10082, probability, 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        double cumulativeProbability = distribution.cumulativeProbability(3);
        assertEquals(0.26503, cumulativeProbability, 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        double normalApproximateProbability = distribution.normalApproximateProbability(3);
        assertEquals(0.26167, normalApproximateProbability, 0.00001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int domainLowerBound = distribution.getDomainLowerBound(0.5);
        assertEquals(0, domainLowerBound);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int domainUpperBound = distribution.getDomainUpperBound(0.5);
        assertEquals(Integer.MAX_VALUE, domainUpperBound);
    }
}
