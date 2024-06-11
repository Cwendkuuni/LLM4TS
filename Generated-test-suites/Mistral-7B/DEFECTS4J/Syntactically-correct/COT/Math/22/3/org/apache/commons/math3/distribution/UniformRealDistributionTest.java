import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.RandomUtils;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.density(0.5), 0.0001);
    }

    @Test
    public void testConstructWithBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, dist.getLowerBound(), 0.0);
        assertEquals(2.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.density(1.5), 0.0001);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testConstructWithInvalidBounds() {
        new UniformRealDistribution(2.0, 1.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        for (double x = 1.0; x < 3.0; x += 0.1) {
            assertEquals(0.5, dist.density(x), 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        for (double x = 1.0; x < 3.0; x += 0.1) {
            assertEquals(x - 1.0 / 2.0, dist.cumulativeProbability(x), 0.001);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.5, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(0.16666666666666668, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(2.0, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1.0 || sample <= 2.0);
        }
    }

    @Test
    public void testSampleWithCustomRandomGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distWithRng = new UniformRealDistribution(rng, 1.0, 2.0);
        for (int i = 0; i < 1000; i++) {
            double sample = distWithRng.sample();
            assertTrue(sample >= 1.0 || sample <= 2.0);
        }
    }
}