package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Unit tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 0, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 2, -1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 2, 0, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooManySuccesses() {
        new HypergeometricDistribution(new Well19937c(), 2, 3, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooLargeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 2, 1, 3);
    }

    @Test
    public void testHypergeometricDistributionBasic() {
        final int populationSize = 5;
        final int numberOfSuccesses = 3;
        final int sampleSize = 2;

        final HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);

        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(sampleSize, distribution.getSampleSize());

        assertEquals(0.0, distribution.cumulativeProbability(-1), 1e-14);
        assertEquals(0.0, distribution.probability(-1), 1e-14);
        assertEquals(1.0, distribution.cumulativeProbability(populationSize), 1e-14);
        assertEquals(1.0, distribution.probability(populationSize), 1e-14);

        final int[] domain = distribution.getDomain();
        assertEquals(numberOfSuccesses, domain[0]);
        assertEquals(Math.min(numberOfSuccesses, sampleSize), domain[1]);

        assertEquals(numberOfSuccesses, distribution.getSupportLowerBound());
        assertEquals(sampleSize, distribution.getSupportUpperBound());

        assertEquals(true, distribution.isSupportConnected());

        assertEquals(numberOfSuccesses * sampleSize / populationSize, distribution.getNumericalMean(), 1e-14);
        assertEquals(distribution.getNumericalVariance(), 
                ((sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) /
                        (populationSize * populationSize * (populationSize - 1))), 1e-14);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        final int populationSize = 5;
        final int numberOfSuccesses = 3;
        final int sampleSize = 2;

        final HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= populationSize; i++) {
            double p = distribution.probability(i);
            double c = distribution.cumulativeProbability(i);
            double u = distribution.upperCumulativeProbability(i);
            assertEquals(p, u - c, 1e-14);
        }
    }
}