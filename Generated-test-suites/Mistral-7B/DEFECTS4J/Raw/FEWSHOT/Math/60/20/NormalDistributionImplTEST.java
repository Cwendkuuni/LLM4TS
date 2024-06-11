package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import java.util.Random;

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

        density = dist.density(1);
        assertEquals(0.398942280401, density, 0.001);

        density = dist.density(2);
        assertEquals(0.154308063482, density, 0.001);

        density = dist.density(-1);
        assertEquals(0.154308063482, density, 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = dist.cumulativeProbability(0);
        assertEquals(0.5, cumulativeProbability, 0.001);

        cumulativeProbability = dist.cumulativeProbability(-1);
        assertEquals(0, cumulativeProbability, 0.001);

        cumulativeProbability = dist.cumulativeProbability(3);
        assertEquals(1, cumulativeProbability, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(0.25);
        assertEquals(0.60653066746, inverseCumulativeProbability, 0.001);

        inverseCumulativeProbability = dist.inverseCumulativeProbability(0.75);
        assertEquals(0.63212055814, inverseCumulativeProbability, 0.001);

        inverseCumulativeProbability = dist.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0.0);

        inverseCumulativeProbability = dist.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0.0);
    }

    @Test
    public void testSample() throws MathException {
        Random random = new Random();
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        double standardDeviation = dist.getStandardDeviation();
        assertEquals(0, sample, standardDeviation * 3);

        sample = dist.sample();
        assertEquals(0, sample, standardDeviation * 3);

        assertEquals(0, dist.cumulativeProbability(sample), 0.001);
    }
}