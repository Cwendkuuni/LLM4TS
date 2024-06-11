import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        int[] domain = dist.getDomain();
        assertEquals(2, domain.length);
        assertEquals(2, domain[0]);
        assertEquals(5, domain[1]);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() throws Exception {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroSampleSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 0;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooManySuccesses() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooLargeSampleSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 2; x <= 5; x++) {
            double p = dist.cumulativeProbability(x);
            assertEquals(Double.valueOf(p), dist.cumulativeProbability(x));
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 2; x <= 5; x++) {
            double p = dist.probability(x);
            assertEquals(Double.valueOf(p), dist.probability(x), 1e-8);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 2; x <= 5; x++) {
            double p = dist.upperCumulativeProbability(x);
            assertEquals(1.0 - dist.cumulativeProbability(x - 1), p, 1e-8);
        }
    }

    @Test
    public void testHypergeometricDistributionMean() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double mean = dist.getNumericalMean();
        assertEquals(3.0 * 5.0 / 10.0, mean, 1e-8);
    }

    @Test
    public void testHypergeometricDistributionVariance() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double variance = dist.getNumericalVariance();
        double expectedVariance = ((3.0 * 5.0 / 10.0) * (10.0 - 3.0) * (10.0 - 5.0)) / (10.0 * 10.0 * (10.0 - 1.0));
        assertEquals(expectedVariance, variance, 1e-8);
    }

    @Test
    public void testHypergeometricDistributionSupport() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();

        assertEquals(3, lowerBound);
        assertEquals(5, upperBound);
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(dist.isSupportConnected());
    }
}