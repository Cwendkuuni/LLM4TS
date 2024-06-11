import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructors() {
        try {
            int populationSize = 10;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            assertEquals(populationSize, hd.getPopulationSize());
            assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
            assertEquals(sampleSize, hd.getSampleSize());

            HypergeometricDistribution hd2 = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            assertEquals(populationSize, hd2.getPopulationSize());
            assertEquals(numberOfSuccesses, hd2.getNumberOfSuccesses());
            assertEquals(sampleSize, hd2.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        try {
            int populationSize = 15;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

            int[] domain = hd.getDomain();
            for (int i = domain[0]; i <= domain[1]; i++) {
                double prob = hd.cumulativeProbability(i);
                double expectedProb = getHypergeometricCumulativeProbability(populationSize, numberOfSuccesses, sampleSize, i);
                assertEquals(expectedProb, prob, 1e-12);
            }
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        try {
            int populationSize = 15;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

            for (int i = 0; i <= sampleSize; i++) {
                double prob = hd.probability(i);
                double expectedProb = getHypergeometricProbability(populationSize, numberOfSuccesses, sampleSize, i);
                assertEquals(expectedProb, prob, 1e-12);
            }
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        try {
            int populationSize = 15;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

            int[] domain = hd.getDomain();
            for (int i = domain[0]; i <= domain[1]; i++) {
                double prob = hd.upperCumulativeProbability(i);
                double expectedProb = getHypergeometricUpperCumulativeProbability(populationSize, numberOfSuccesses, sampleSize, i);
                assertEquals(expectedProb, prob, 1e-12);
            }
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNumericalMean() {
        try {
            int populationSize = 15;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

            double numericalMean = hd.getNumericalMean();
            double expectedMean = (double) sampleSize * numberOfSuccesses / populationSize;
            assertEquals(expectedMean, numericalMean, 1e-12);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNumericalVariance() {
        try {
            int populationSize = 15;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

            double numericalVariance = hd.getNumericalVariance();
            double expectedVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
            assertEquals(expectedVariance, numericalVariance, 1e-12);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSupportBounds() {
        try {
            int populationSize = 15;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

            int lowerBound = hd.getSupportLowerBound();
            int upperBound = hd.getSupportUpperBound();
            int[] domain = hd.getDomain();

            assertTrue(lowerBound >= domain[0]);
            assertTrue(upperBound <= domain[1]);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSupportConnected() {
        try {
            int populationSize = 15;
            int numberOfSuccesses = 5;
            int sampleSize = 3;

            HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

            assertTrue(hd.isSupportConnected());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail(e.getMessage());
        }
    }

    private double getHypergeometricCumulativeProbability(int N, int m, int n, int x) {
        double p = (double) n / N;
        double q = (double) (N - n) / N;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, m, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(n - x, N - m, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(n, N, p, q);
        return Math.exp(p1 + p2 - p3);
    }

    private double getHypergeometricProbability(int N, int m, int n, int x) {
        double p = (double) n / N;
        double q = (double) (N - n) / N;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, m, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(n - x, N - m, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(n, N, p, q);
        return Math.exp(p1 + p2 - p3);
    }

    private double getHypergeometricUpperCumulativeProbability(int N, int m, int n, int x) {
        double p = (double) n / N;
        double q = (double) (N - n) / N;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, m, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(n - x, N - m, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(n - 1, N - 1, p, q);
        return Math.exp(p1 + p2 - p3);
    }
}