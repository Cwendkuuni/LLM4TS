package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;

import java.util.Random;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);
        assertEquals(0.154308037, dist.density(0), 0.0001);
        assertEquals(0.5, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.15865525, dist.inverseCumulativeProbability(0.95), 0.0001);
    }

    @Test
    public void testNegativeStandardDeviation() {
        assertThrows(NotStrictlyPositiveException.class, () -> new NormalDistributionImpl(0, -1));
    }

    @Test
    public void testMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(2, 3);
        assertEquals(2, dist.getMean(), 0);
        assertEquals(3, dist.getStandardDeviation(), 0);
    }

    @Test
    public void testDensityWithMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(3, 2);
        assertEquals(0.130899727, dist.density(3.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.841344746, dist.cumulativeProbability(1), 0.0001);
        assertEquals(1, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0, dist.cumulativeProbability(5), 0.0001);

        // Test edge cases
        assertEquals(1, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
        assertEquals(0, dist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.inverseCumulativeProbability(0), 0.0001);
        assertEquals(1, dist.inverseCumulativeProbability(1), 0.0001);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(2));
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(-1));
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            double density = dist.density(sample);
            assertEquals(density, dist.density(sample), 0.01);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0), 0.0);
        assertEquals(0, dist.getDomainLowerBound(0.5), 0.0);
        assertEquals(0, dist.getDomainLowerBound(1), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0), 0.0);
        assertEquals(0, dist.getDomainUpperBound(0.5), 0.0);
        assertEquals(0, dist.getDomainUpperBound(1), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, dist.getInitialDomain(0), 0.0);
        assertEquals(0, dist.getInitialDomain(0.5), 0.0);
        assertEquals(0, dist.getInitialDomain(1), 0.0);
    }

    @Test
    public void testErf() throws MathException {
        assertEquals(0, Erf.erf(0), 0.0001);
        assertEquals(1, Erf.erfc(0), 0.0001);
        assertEquals(0.842701165, Erf.erf(1), 0.0001);
        assertEquals(0.157298835, Erf.erfc(1), 0.0001);
    }
}