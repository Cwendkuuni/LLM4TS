package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0758960265956027, dist.probability(0), 0.0000001);
        assertEquals(0.522553113192863, dist.probability(1), 0.0000001);
        assertEquals(0.398942280401433, dist.probability(2), 0.0000001);
        assertEquals(0.113154735593237, dist.probability(3), 0.0000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, dist.cumulativeProbability(Integer.MIN_VALUE), 0.0);
        assertEquals(0.0758960265956027, dist.cumulativeProbability(0), 0.0000001);
        assertEquals(0.281328995663981, dist.cumulativeProbability(1), 0.0000001);
        assertEquals(0.602735148824258, dist.cumulativeProbability(2), 0.0000001);
        assertEquals(0.810733553401233, dist.cumulativeProbability(3), 0.0000001);
        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0758960265956027, dist.normalApproximateProbability(0), 0.0000001);
        assertEquals(0.522553113192863, dist.normalApproximateProbability(1), 0.0000001);
        assertEquals(0.398942280401433, dist.normalApproximateProbability(2), 0.0000001);
        assertEquals(0.113154735593237, dist.normalApproximateProbability(3), 0.0000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sampledValue = dist.sample();
        assertEquals(1, sampledValue, 0);
        sampledValue = dist.sample();
        assertEquals(2, sampledValue, 0);
        sampledValue = dist.sample();
        assertEquals(0, sampledValue, 0);
    }

    @Test
    public void testCumulativeProbabilityWithLargeMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        double epsilon = 1e-12;
        double maxIterations = 100000;
        double cumProb = dist.cumulativeProbability(50);
        assertEquals(0.999996531346134, cumProb, epsilon);
    }

    @Test
    public void testCumulativeProbabilityWithSmallMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        int sampledValues[] = new int[100_000];
        Random random = new Random();
        for (int i = 0; i < sampledValues.length; i++) {
            sampledValues[i] = dist.sample(random);
        }
        int sum = 0;
        for (int i = 0; i < sampledValues.length; i++) {
            sum += sampledValues[i];
        }
        double expectedSum = 0.0;
        for (int i = 0; i < sampledValues.length; i++) {
            expectedSum += dist.probability(i);
        }
        assertEquals(expectedSum, sum, 1);
    }

    @Test
    public void testDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0, dist.getDomainLowerBound(0.1), 0);
    }

    @Test
    public void testDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.1), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double p = 0.1;
        int x = dist.inverseCumulativeProbability(p);
        assertEquals(1, x, 0);
        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(1, x, 0);
        p = 0.8;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(2, x, 0);
        p = 0.95;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(3, x, 0);
    }
}