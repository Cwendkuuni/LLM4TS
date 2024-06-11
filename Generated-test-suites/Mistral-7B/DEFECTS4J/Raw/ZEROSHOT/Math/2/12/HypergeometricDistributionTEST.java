import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.stats.distribution.SaddlePointExpansion;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() throws NotPositiveException, NotStrictlyPositiveException {
        // Test with valid parameters
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Test mean
        double mean = dist.getNumericalMean();
        assertEquals(mean, (double) (sampleSize * numberOfSuccesses / populationSize), 1e-10);

        // Test variance
        double variance = dist.getNumericalVariance();
        double expectedVariance = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(variance, expectedVariance, 1e-10);

        // Test support bounds
        int lowerBound = dist.getSupportLowerBound();
        int expectedLowerBound = populationSize + numberOfSuccesses - sampleSize;
        assertEquals(lowerBound, expectedLowerBound);

        int upperBound = dist.getSupportUpperBound();
        int expectedUpperBound = Math.min(numberOfSuccesses, sampleSize);
        assertEquals(upperBound, expectedUpperBound);

        // Test distribution properties
        for (int i = lowerBound; i <= upperBound; i++) {
            double probability = dist.probability(i);
            double expectedProbability = SaddlePointExpansion.binomialProbability(i, sampleSize, numberOfSuccesses / (double) populationSize);
            assertEquals(probability, expectedProbability, 1e-10);
        }

        // Test cumulative distribution
        double cumulativeProbability = dist.cumulativeProbability(numberOfSuccesses);
        assertEquals(cumulativeProbability, 0.5, 1e-10);

        // Test tail probability
        double tailProbability = dist.upperCumulativeProbability(numberOfSuccesses + 1);
        assertEquals(tailProbability, 1 - cumulativeProbability, 1e-10);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() throws NotPositiveException {
        int populationSize = 10;
        int numberOfSuccesses = -3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() throws NotStrictlyPositiveException {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativePopulationSize() throws NotStrictlyPositiveException {
        int populationSize = -10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
}