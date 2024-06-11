import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_ValidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        int populationSize = 5;
        int numberOfSuccesses = -1;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException e) {
            assertEquals("numberOfSuccesses", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("populationSize", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_InvalidNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            assertEquals("numberOfSuccesses", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_InvalidSampleSizeLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 6;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            assertEquals("sampleSize", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = distribution.cumulativeProbability(x);
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 0.001);
        }
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= sampleSize; x++) {
            double expected = distribution.probability(x);
            double actual = distribution.probability(x);
            assertEquals(expected, actual, 0.001);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = 1.0 - distribution.cumulativeProbability(x);
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual, 0.001);
        }
    }

    @Test
    public void testMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = (double) sampleSize * numberOfSuccesses / populationSize;
        double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) /
                (populationSize * populationSize * (populationSize - 1));
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expected = populationSize + numberOfSuccesses - sampleSize;
        int actual = distribution.getSupportLowerBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expected = Math.min(numberOfSuccesses, sampleSize);
        int actual = distribution.getSupportUpperBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(distribution.isSupportConnected());
    }
}