import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    @Test
    public void testConstructorWithDefaultBounds() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithCustomBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(1.5, 4.1);
        assertEquals(1.5, dist.getLowerBound(), 0.0);
        assertEquals(4.1, dist.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(4.0, 4.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1 / (1.0 - 0.0), density, 1e-5);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            if (x <= 0.5) {
                assertEquals(x / 1.0, cdf, 1e-5);
            } else {
                assertEquals(1 - (1.0 - x) / 1.0, cdf, 1e-5);
            }
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, dist.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0002777777777777778, dist.getNumericalVariance(), 1e-14);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    @Test
    public void testSampleWithCustomGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }
}