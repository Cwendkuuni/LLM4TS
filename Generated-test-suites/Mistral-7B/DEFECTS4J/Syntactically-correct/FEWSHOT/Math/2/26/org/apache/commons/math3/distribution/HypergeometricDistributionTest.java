package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -3, 2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 3, -2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionWithNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 2, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 4, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 3, 4);
    }

    @Test
    public void testHypergeometricDistributionWithValidParameters() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        int[] domain = dist.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(5, domain[1]);
        double mean = dist.getNumericalMean();
        assertEquals(5.0, mean, 0.001);
        double variance = dist.getNumericalVariance();
        assertEquals(2.585, variance, 0.001);

        for (int i = 0; i <= 5; i++) {
            double prob = dist.probability(i);
            double cumProb = dist.cumulativeProbability(i);
            assertEquals(cumProb, prob, 0.001);
        }

        double tailProb = dist.cumulativeProbability(6);
        assertEquals(0.0, tailProb, 0.001);

        double tailProbNeg = dist.upperCumulativeProbability(5);
        assertEquals(0.0, tailProbNeg, 0.001);

        int supportLowerBound = dist.getSupportLowerBound();
        int supportUpperBound = dist.getSupportUpperBound();
        assertEquals(5, supportLowerBound);
        assertEquals(5, supportUpperBound);

        assertTrue(dist.isSupportConnected());
    }
}