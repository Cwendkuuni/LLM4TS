import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.1, distribution.density(5), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.5, distribution.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(5.0, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(8.333, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(10.0, distribution.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorException() {
        new UniformRealDistribution(10, 0);
    }
}