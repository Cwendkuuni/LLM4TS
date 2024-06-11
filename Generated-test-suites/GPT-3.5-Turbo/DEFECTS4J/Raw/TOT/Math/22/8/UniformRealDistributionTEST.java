import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertEquals(-10, distribution.getSupportLowerBound(), 0);
        assertEquals(10, distribution.getSupportUpperBound(), 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidBounds() {
        new UniformRealDistribution(10, -10);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.density(0), 0);
        assertEquals(1, distribution.density(0.5), 0);
        assertEquals(1, distribution.density(1), 0);
        assertEquals(0, distribution.density(-1), 0);
        assertEquals(0, distribution.density(2), 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(0, distribution.cumulativeProbability(0), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
        assertEquals(1, distribution.cumulativeProbability(1), 0);
        assertEquals(1, distribution.cumulativeProbability(2), 0);
    }

    @Test
    public void testMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertEquals(0, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertEquals(33.333, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertEquals(-10, distribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertEquals(10, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(-10, 10);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 1);
    }

}