import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.density(0.5), 0.0);
        assertEquals(0.0, distribution.density(-1), 0.0);
        assertEquals(0.0, distribution.density(1.5), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(1.5), 0.0);
    }

    @Test
    public void testMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testSupportBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0.0 && sample < 1.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidBounds() {
        new UniformRealDistribution(1, 0);
    }
}