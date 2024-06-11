import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Test
    public void testDensity() {
        distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.density(-1), 0.000001);
        assertEquals(1.0, distribution.density(0), 0.000001);
        assertEquals(1.0, distribution.density(0.5), 0.000001);
        assertEquals(1.0, distribution.density(1), 0.000001);
        assertEquals(0.0, distribution.density(2), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.000001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.000001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(1), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.08333333333333333, distribution.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        distribution = new UniformRealDistribution(0, 1);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 1);
    }

}