import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_ValidArguments() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;

        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, hypergeometric.getPopulationSize());
        assertEquals(numberOfSuccesses, hypergeometric.getNumberOfSuccesses());
        assertEquals(sampleSize, hypergeometric.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        RandomGenerator rng = new Well19937c();
        int populationSize = -10;
        int numberOfSuccesses = 50;
        int sampleSize = 10;

        new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 100;
        int numberOfSuccesses = -5;
        int sampleSize = 10;

        new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
    }

    // Add more test cases for other methods as needed

}