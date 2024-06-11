import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test constructor with valid inputs
    @Test
    public void testConstructorValidInputs() {
        int populationSize = 1000;
        int numberOfSuccesses = 300;
        int sampleSize = 50;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertNotNull(distribution);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    // Test constructor with invalid inputs
    @Test
    public void testConstructorInvalidInputs() {
        // Test populationSize <= 0
        int populationSize = -1;
        int numberOfSuccesses = 300;
        int sampleSize = 50;

        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test numberOfSuccesses < 0
        populationSize = 1000;
        numberOfSuccesses = -1;
        sampleSize = 50;

        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test sampleSize < 0
        populationSize = 1000;
        numberOfSuccesses = 300;
        sampleSize = -1;

        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test numberOfSuccesses > populationSize
        populationSize = 1000;
        numberOfSuccesses = 2000;
        sampleSize = 50;

        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test sampleSize > populationSize
        populationSize = 1000;
        numberOfSuccesses = 300;
        sampleSize = 1500;

        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    // Add more test cases for other methods

}