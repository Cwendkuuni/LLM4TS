package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 5);

        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getSampleSize());

        assertTrue(dist.getNumericalMean() == 5.0 * 5.0 / 10.0);
        assertTrue(dist.getNumericalVariance() == 5.0 * 5.0 * 5.0 * (10.0 - 5.0) * (10.0 - 5.0) / (10.0 * 10.0 * (10.0 - 1.0)));
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonPositivePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 0, 5, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
    }

    @Test
    public void testCumulativeProbability() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 5);

        int[] domain = dist.getDomain();

        for (int i = domain[0]; i <= domain[1]; i++) {
            assertEquals(dist.cumulativeProbability(i),
                         dist.cumulativeProbability(i), 1e-15);
        }
    }

    @Test
    public void testProbability() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 5);

        for (int i = 0; i <= 10; i++) {
            assertEquals(dist.probability(i), dist.probability(i), 1e-15);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 5);

        for (int i = 0; i <= 10; i++) {
            assertEquals(dist.upperCumulativeProbability(i), 1 - dist.cumulativeProbability(i), 1e-15);
        }
    }
}