package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);

        assertEquals(2.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);

        assertEquals(1.2941, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);

        assertEquals(0.9919, dist.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);

        assertEquals(0.0080, dist.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);

        assertEquals(0.3363, dist.probability(3), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativePopulationSize() {
        new HypergeometricDistribution(-20, 5, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(20, -5, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() {
        new HypergeometricDistribution(20, 5, -10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testTooLargeNumberOfSuccesses() {
        new HypergeometricDistribution(10, 20, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testTooLargeSampleSize() {
        new HypergeometricDistribution(20, 5, 25);
    }

}