import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() {
        // Test constructor with valid arguments
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hd.getPopulationSize());
        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        assertEquals(sampleSize, hd.getSampleSize());

        // Test basic functionality
        double[] probabilities = new double[sampleSize + 1];
        for (int i = 0; i <= sampleSize; i++) {
            probabilities[i] = hd.probability(i);
        }
        assertArrayEquals(new double[]{0.0, 0.02431, 0.04862, 0.07711, 0.1116, 0.1511, 0.2038, 0.2687, 0.3448, 0.4211, 0.4966, 0.5703, 0.6412, 0.7072, 0.7674, 0.8198, 0.8633, 0.9001, 0.9302, 0.9538, 0.9697, 0.9826, 0.9911, 1.0}, probabilities, 0.0001);
        int[] cumulativeProbabilities = new int[sampleSize + 1];
        for (int i = 0; i <= sampleSize; i++) {
            cumulativeProbabilities[i] = (int) Math.round(hd.cumulativeProbability(i) * 10000.0);
        }
        assertArrayEquals(new int[]{0, 0, 0, 1, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27}, cumulativeProbabilities);

        // Test getNumericalMean and getNumericalVariance
        double numericalMean = hd.getNumericalMean();
        double numericalVariance = hd.getNumericalVariance();
        assertEquals(6.0 / 13.0, numericalMean, 0.001);
        assertEquals(3.0 / 13.0, numericalVariance, 0.001);

        // Test exception handling
        try {
            new HypergeometricDistribution(-5, 5, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, -5, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 5, -3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 15, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 5, 15);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }
}