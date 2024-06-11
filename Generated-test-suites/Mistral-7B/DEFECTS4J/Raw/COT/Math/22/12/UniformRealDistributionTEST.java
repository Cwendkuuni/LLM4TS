import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    @Test
    public void testConstructorWithDefaultValues() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithCustomValues() {
        double lower = 2.5;
        double upper = 5.5;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(lower, distribution.getLowerBound(), 0.0);
        assertEquals(upper, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testInvalidConstructor() {
        double lower = 5.5;
        double upper = 2.5;
        try {
            new UniformRealDistribution(lower, upper);
            org.junit.Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testDensity() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        for (double x = lower; x <= upper; x += 0.01) {
            double density = distribution.density(x);
            double expectedDensity = 1 / (upper - lower);
            assertEquals(expectedDensity, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        for (double x = lower; x <= upper; x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = x / (upper - lower);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        double numericalMean = distribution.getNumericalMean();
        double expectedNumericalMean = 0.5 * (lower + upper);
        assertEquals(expectedNumericalMean, numericalMean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        double numericalVariance = distribution.getNumericalVariance();
        double expectedNumericalVariance = (upper - lower) * (upper - lower) / 12;
        assertEquals(expectedNumericalVariance, numericalVariance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(lower, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(upper, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(new Well19937c(), lower, upper, UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(lower <= sample && sample < upper);
        }
    }
}