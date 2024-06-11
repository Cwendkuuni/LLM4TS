package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionWithValidParams() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, poissonDistribution.getMean(), 0.001);

        double probability = poissonDistribution.probability(0);
        assertEquals(0.07378195, probability, 0.0001);

        probability = poissonDistribution.probability(1);
        assertEquals(0.11832098, probability, 0.0001);

        probability = poissonDistribution.probability(3);
        assertEquals(0.12641528, probability, 0.0001);

        double cumulativeProbability = poissonDistribution.cumulativeProbability(2);
        assertEquals(0.25936262, cumulativeProbability, 0.0001);

        int sample = poissonDistribution.sample();
        assertEquals(2, sample, 0);

        int sampleFromCdf = poissonDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1, sampleFromCdf, 0);

        PoissonDistribution poissonDistributionWithEpsilon = new PoissonDistributionImpl(3.5, 0.0001);
        double normalApproximation = poissonDistributionWithEpsilon.normalApproximateProbability(1);
        assertEquals(0.11832098, normalApproximation, 0.0001);
    }

    @Test
    public void testPoissonDistributionWithNegativeMean() {
        assertThrows(MathRuntimeException.class, () -> new PoissonDistributionImpl(-1));
    }

    @Test
    public void testProbabilityWithNegativeArgument() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        assertThrows(MathIllegalArgumentException.class, () -> poissonDistribution.probability(-1));
    }

    @Test
    public void testCumulativeProbabilityWithNegativeArgument() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        assertThrows(MathIllegalArgumentException.class, () -> poissonDistribution.cumulativeProbability(-1));
    }

    @Test
    public void testCumulativeProbabilityWithMaxIntegerArgument() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        assertThrows(MathIllegalArgumentException.class, () -> poissonDistribution.cumulativeProbability(Integer.MAX_VALUE));
    }

    @Test
    public void testSampleWithMaxIntegerArgument() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        int sample = poissonDistribution.sample();
        assertEquals(Integer.MAX_VALUE, sample, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithNegativeArgument() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        assertThrows(MathIllegalArgumentException.class, () -> poissonDistribution.inverseCumulativeProbability(-0.1));
        assertThrows(MathIllegalArgumentException.class, () -> poissonDistribution.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testSampleWithMaxIterationsReachLimit() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1e10, 1e10, 1);
        int sample = poissonDistribution.sample();
        assertEquals(1e10, sample, 0);
    }

    @Test
    public void testSampleWithMaxIterationsNotReachLimit() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5, 100, 100000);
        int sample = poissonDistribution.sample();
        assertEquals(5, sample, 0);
    }

    @Test
    public void testProbabilityWithMeanAndMaxIterations() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5, 100, 100000);
        double probability = poissonDistribution.probability(2);
        assertEquals(0.2226926, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithMeanAndMaxIterations() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5, 100, 100000);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.3275043, cumulativeProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(4.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.36787944, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testSampleStdev() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5);
        StandardDeviation stdev = new StandardDeviation();
        double stdevExpected = Math.sqrt(5);
        for (int i = 0; i < 10000; i++) {
            int sample = poissonDistribution.sample();
            stdev.incrementAndGetAddend(sample - 5);
        }
        assertEquals(stdevExpected, stdev.getResult(), 0.001);
    }
}