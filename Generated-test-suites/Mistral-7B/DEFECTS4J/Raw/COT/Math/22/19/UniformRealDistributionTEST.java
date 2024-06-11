import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertNotNull(dist.getRandomGenerator());
    }

    @Test
    public void testUniformRealDistributionConstructorWithBounds() {
        double lower = 0.1;
        double upper = 0.5;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertEquals(lower, dist.getSupportLowerBound(), 0.0);
        assertEquals(upper, dist.getSupportUpperBound(), 0.0);
        assertNotNull(dist.getRandomGenerator());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidBounds() {
        double lower = 2.0;
        double upper = 1.0;
        new UniformRealDistribution(lower, upper);
    }

    @Test
    public void testUniformRealDistributionConstructorWithBoundsAndAccuracy() {
        double lower = 0.1;
        double upper = 0.5;
        double inverseCumAccuracy = 1e-6;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper, inverseCumAccuracy);
        assertEquals(lower, dist.getSupportLowerBound(), 0.0);
        assertEquals(upper, dist.getSupportUpperBound(), 0.0);
        assertEquals(inverseCumAccuracy, dist.getSolverAbsoluteAccuracy(), 1e-7);
        assertNotNull(dist.getRandomGenerator());
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        // Test with lower bound
        double x = dist.getSupportLowerBound();
        double expected = 1 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
        assertEquals(expected, dist.density(x), 1e-9);
        // Test with upper bound
        x = dist.getSupportUpperBound() - 1e-9;
        expected = 1 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
        assertEquals(expected, dist.density(x), 1e-9);
        // Test with point in middle
        x = dist.getSupportUpperBound() / 2;
        expected = 1 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
        assertEquals(expected, dist.density(x), 1e-9);
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        // Test with lower bound
        double x = dist.getSupportLowerBound();
        double expected = 0;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-9);
        // Test with upper bound
        x = dist.getSupportUpperBound();
        expected = 1;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-9);
        // Test with point in middle
        x = dist.getSupportUpperBound() / 2;
        expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-9);
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double expected = 0.5 * (dist.getSupportLowerBound() + dist.getSupportUpperBound());
        assertEquals(expected, dist.getNumericalMean(), 1e-9);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double ul = dist.getSupportUpperBound() - dist.getSupportLowerBound();
        double expected = ul * ul / 12;
        assertEquals(expected, dist.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double lower = dist.getSupportLowerBound();
        double upper = dist.getSupportUpperBound();
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= lower);
            assertTrue(sample < upper);
        }
    }

    @Test
    public void testUniformRealDistributionGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(dist.getSupportLowerBound(), dist.getLowerBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(dist.getSupportUpperBound(), dist.getUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniforRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());
    }

}