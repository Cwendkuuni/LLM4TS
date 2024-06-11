import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0, dist.density(0.5), 0.0001);

        // Edge cases
        assertEquals(0.0, dist.density(2), 0.0001);
        assertEquals(0.0, dist.density(-1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0001);

        // Edge cases
        assertEquals(0.0, dist.cumulativeProbability(-2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0, dist.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1, dist.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        // Typical use case
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double sample = dist.sample();
        assertTrue(sample >= 0 && sample <= 1);
    }
}