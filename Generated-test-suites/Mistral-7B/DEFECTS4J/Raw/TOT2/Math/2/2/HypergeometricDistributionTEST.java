import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            new HypergeometricDistribution(0, 0, 5);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(10, 12, 13);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        new HypergeometricDistribution(10, 5, 5);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        // Test with valid inputs
        assertEquals(0.2, dist.probability(2), 0.001);
        assertEquals(0.2, dist.probability(4), 0.001);
        assertEquals(0.3, dist.probability(5), 0.001);

        // Test with invalid inputs
        try {
            dist.probability(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            dist.probability(6);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        // Test with valid inputs
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.2, dist.cumulativeProbability(2), 0.001);
        assertEquals(0.4, dist.cumulativeProbability(3), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(4), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(5), 0.001);
        assertEquals(0.6, dist.cumulativeProbability(6), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.001); // Should be equal to 0.5 + probability(6)

        // Test with invalid inputs
        try {
            dist.cumulativeProbability(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            dist.cumulativeProbability(6);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        // Test with valid inputs
        assertEquals(0.8, dist.upperCumulativeProbability(2), 0.001);
        assertEquals(0.4, dist.upperCumulativeProbability(4), 0.001);
        assertEquals(0.2, dist.upperCumulativeProbability(5), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(6), 0.001);

        // Test with invalid inputs
        try {
            dist.upperCumulativeProbability(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            dist.upperCumulativeProbability(6);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMoments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        // Test mean
        double expectedMean = ((double) 5) * ((double) 5) / ((double) 10);
        assertEquals(expectedMean, dist.getNumericalMean(), 0.001);

        // Test variance
        double expectedVariance = ((double) 5 * ((double) 5 * ((double) 5 - ((double) 5)) * ((double) 10 - ((double) 5)))) / ((double) Math.pow((double) 10, 2) * ((double) 10 - 1));
        assertEquals(expectedVariance, dist.getNumericalVariance(), 0.001);

        try {
            dist.getNumericalVariance(); // Test multiple times to ensure no exceptions are thrown
        } catch (Exception e) {
            fail("Expected no exceptions");
        }
    }
}