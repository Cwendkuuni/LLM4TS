import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        // typical use case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());

        // edge case: populationSize is 0
        try {
            distribution = new HypergeometricDistribution(0, 5, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (org.apache.commons.math3.exception.NotStrictlyPositiveException e) {
            // success
        }

        // edge case: numberOfSuccesses is negative
        try {
            distribution = new HypergeometricDistribution(10, -1, 3);
            fail("Expected NotPositiveException");
        } catch (org.apache.commons.math3.exception.NotPositiveException e) {
            // success
        }

        // edge case: sampleSize is negative
        try {
            distribution = new HypergeometricDistribution(10, 5, -3);
            fail("Expected NotPositiveException");
        } catch (org.apache.commons.math3.exception.NotPositiveException e) {
            // success
        }

        // edge case: numberOfSuccesses > populationSize
        try {
            distribution = new HypergeometricDistribution(10, 15, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (org.apache.commons.math3.exception.NumberIsTooLargeException e) {
            // success
        }

        // edge case: sampleSize > populationSize
        try {
            distribution = new HypergeometricDistribution(10, 5, 10);
            fail("Expected NumberIsTooLargeException");
        } catch (org.apache.commons.math3.exception.NumberIsTooLargeException e) {
            // success
        }
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.4285714286, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }
}