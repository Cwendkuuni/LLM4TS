import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistribution() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        // Additional assertions
    }

    @Test
    public void testUniformRealDistributionWithBounds() {
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
            // Additional assertions
        } catch (NumberIsTooLargeException e) {
            fail("Failed to initialize UniformRealDistribution with valid bounds.");
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 0);
    }

    @Test
    public void testUniformRealDistributionWithInverseCumAccuracy() {
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(0, 1, 1e-9);
            // Additional assertions
        } catch (NumberIsTooLargeException e) {
            fail("Failed to initialize UniformRealDistribution with valid bounds and inverseCumAccuracy.");
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.density(0.5);
        // Additional assertions
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.cumulativeProbability(0.5);
        // Additional assertions
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getSolverAbsoluteAccuracy();
        // Additional assertions
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getNumericalMean();
        // Additional assertions
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getNumericalVariance();
        // Additional assertions
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getSupportLowerBound();
        // Additional assertions
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getSupportUpperBound();
        // Additional assertions
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportLowerBoundInclusive();
        // Additional assertions
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportUpperBoundInclusive();
        // Additional assertions
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportConnected();
        // Additional assertions
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.sample();
        // Additional assertions
    }

    @Test
    public void testSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testSupportUpperBoundExclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testDensityOutsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.density(-1);
        assertEquals(0, result, 0);

        result = distribution.density(2);
        assertEquals(0, result, 0);
    }

    @Test
    public void testCumulativeProbabilityOutsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.cumulativeProbability(-1);
        assertEquals(0, result, 0);

        result = distribution.cumulativeProbability(2);
        assertEquals(1, result, 0);
    }

    @Test
    public void testSampleWithCustomRandomGenerator() {
        Well19937c rng = new Well19937c();
        rng.setSeed(12345);
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0, 1);
        double result = distribution.sample();
        // Additional assertions
    }
}