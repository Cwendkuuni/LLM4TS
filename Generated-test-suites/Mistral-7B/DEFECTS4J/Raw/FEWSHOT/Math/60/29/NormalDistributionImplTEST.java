package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0);
        assertEquals(0.398942280401, density, 0.001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = dist.cumulativeProbability(0);
        assertEquals(0.5, p, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(1000);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.25;
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(-0.84262258186, inverseCumulativeProbability, 0.001);

        p = 0.75;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(0.67448975078, inverseCumulativeProbability, 0.001);

        p = 1;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0.0);

        p = 0;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityConvergenceError() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        dist.inverseCumulativeProbability(0.51);
    }

    @Test
    public void testSample() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.25;
        double domainLowerBound = dist.getDomainLowerBound(p);
        assertEquals(-3.17533625723, domainLowerBound, 0.001);

        p = 0.75;
        domainLowerBound = dist.getDomainLowerBound(p);
        assertEquals(0.23379597013, domainLowerBound, 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.25;
        double domainUpperBound = dist.getDomainUpperBound(p);
        assertEquals(2.64751375157, domainUpperBound, 0.001);

        p = 0.75;
        domainUpperBound = dist.getDomainUpperBound(p);
        assertEquals(1.47966521506, domainUpperBound, 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.25;
        double initialDomain = dist.getInitialDomain(p);
        assertEquals(-1.09532632758, initialDomain, 0.001);

        p = 0.75;
        initialDomain = dist.getInitialDomain(p);
        assertEquals(1.10518411302, initialDomain, 0.001);
    }
}