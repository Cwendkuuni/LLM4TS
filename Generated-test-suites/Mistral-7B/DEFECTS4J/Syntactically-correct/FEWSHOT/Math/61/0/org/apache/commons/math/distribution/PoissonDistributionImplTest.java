package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.RandomData;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNonPositiveMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.22318333, dist.probability(1), 0.0001);
        assertEquals(0.11179167, dist.probability(2), 0.0001);
        assertEquals(0.07042253, dist.probability(3), 0.0001);
        assertEquals(0.03602826, dist.probability(4), 0.0001);
        assertEquals(0.01611028, dist.probability(5), 0.0001);
        assertEquals(0.00729116, dist.probability(6), 0.0001);
        assertEquals(0.00321684, dist.probability(7), 0.0001);
        assertEquals(0.00149779, dist.probability(8), 0.0001);
        assertEquals(0.00067734, dist.probability(9), 0.0001);
        assertEquals(0.00028224, dist.probability(10), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0);
        assertEquals(0.68268949, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.880797, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.986344, dist.cumulativeProbability(2), 0.0001);
        assertEquals(0.998217, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0.999935, dist.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);

        assertNaN(dist.cumulativeProbability(Integer.MAX_VALUE));
        assertNaN(dist.cumulativeProbability(Integer.MIN_VALUE));
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.68268949, dist.normalApproximateProbability(0), 0.0001);
        assertEquals(0.880797, dist.normalApproximateProbability(1), 0.0001);
        assertEquals(0.986344, dist.normalApproximateProbability(2), 0.0001);
        assertEquals(0.998217, dist.normalApproximateProbability(3), 0.0001);
        assertEquals(0.999935, dist.normalApproximateProbability(4), 0.0001);
        assertEquals(1.0, dist.normalApproximateProbability(5), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        RandomData rnd = new RandomData();
        for (int i = 0; i < 10000; i++) {
            int sample = dist.sample();
            assertEquals(2.5, dist.probability(sample), 0.01);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0, dist.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(1, dist.inverseCumulativeProbability(1.0), 0.0);
        assertEquals(0, dist.inverseCumulativeProbability(0.68268949), 0.0);
        assertEquals(1, dist.inverseCumulativeProbability(0.986344), 0.0);
        assertEquals(2, dist.inverseCumulativeProbability(0.998217), 0.0);
        assertEquals(3, dist.inverseCumulativeProbability(0.999935), 0.0);

        assertNaN(dist.inverseCumulativeProbability(1.1));
        assertNaN(dist.inverseCumulativeProbability(0.55));
    }
}