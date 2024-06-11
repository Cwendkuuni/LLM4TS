import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractTests;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest extends AbstractTests {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 4;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooManySuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 4;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooLargeSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, 1.0, 1.0 - 1.0 / populationSize);
            double actual = distribution.probability(x);
            assertEquals(expected, actual, 1e-8);
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = innerCumulativeProbability(x, populationSize, numberOfSuccesses, sampleSize);
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 1e-8);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = 1.0 - innerCumulativeProbability(domain[1], populationSize, numberOfSuccesses, sampleSize);
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual, 1e-8);
        }
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 1e-8);
    }

    @Test
    public void testHypergeometricDistributionMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = sampleSize * numberOfSuccesses.doubleValue() / populationSize.doubleValue();
        double actual = distribution.getExpectedValue();
        assertEquals(expected, actual, 1e-8);
    }

    private static double innerCumulativeProbability(int x, int populationSize, int numberOfSuccesses, int sampleSize) {
        double p = (double) sampleSize / populationSize;
        double q = (double) (populationSize - sampleSize) / populationSize;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
        return Math.exp(p1 + p2 - p3);
    }

}