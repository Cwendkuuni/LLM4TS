import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Test
    public void testConstructorWithDefaultBounds() {
        distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithValidBounds() {
        distribution = new UniformRealDistribution(-1.5, 3.7);
        assertEquals(-1.5, distribution.getSupportLowerBound(), 0.0);
        assertEquals(3.7, distribution.getSupportUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        distribution = new UniformRealDistribution(5, 2);
    }

    @Test
    public void testDensityWithinBounds() {
        distribution = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, distribution.density(1.2), 0.0);
    }

    @Test
    public void testDensityOutsideBounds() {
        distribution = new UniformRealDistribution(2.0, 3.0);
        assertEquals(0.0, distribution.density(1.5), 0.0);
    }

    @Test
    public void testCumulativeProbabilityWithinBounds() {
        distribution = new UniformRealDistribution(1.0, 2.0);
        assertEquals(0.3, distribution.cumulativeProbability(1.3), 0.0);
    }

    @Test
    public void testCumulativeProbabilityLowerBound() {
        distribution = new UniformRealDistribution(2.0, 3.0);
        assertEquals(0.0, distribution.cumulativeProbability(1.5), 0.0);
    }

    @Test
    public void testCumulativeProbabilityUpperBound() {
        distribution = new UniformRealDistribution(2.0, 3.0);
        assertEquals(1.0, distribution.cumulativeProbability(3.0), 0.0);
    }

    @Test
    public void testNumericalMean() {
        distribution = new UniformRealDistribution(2.0, 4.0);
        assertEquals(3.0, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testNumericalVariance() {
        distribution = new UniformRealDistribution(2.0, 4.0);
        assertEquals(0.3333, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        distribution = new UniformRealDistribution(2.0, 4.0);
        assertEquals(2.0, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testSupportUpperBound() {
        distribution = new UniformRealDistribution(2.0, 4.0);
        assertEquals(4.0, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        distribution = new UniformRealDistribution(2.0, 4.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        distribution = new UniformRealDistribution(2.0, 4.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        distribution = new UniformRealDistribution(2.0, 4.0);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        distribution = new UniformRealDistribution(1.0, 2.0);
        double sample = distribution.sample();
        assertTrue(sample >= 1.0 && sample <= 2.0);
    }

}