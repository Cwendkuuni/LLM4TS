import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertEquals(0.5, uniform.density(0.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertEquals(0.5, uniform.cumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertEquals(0.5, uniform.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertEquals(0.0833, uniform.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertEquals(0, uniform.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertEquals(1, uniform.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertTrue(uniform.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertFalse(uniform.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        assertTrue(uniform.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution uniform = new UniformRealDistribution(0, 1);
        double sample = uniform.sample();
        assertTrue(sample >= 0 && sample < 1);
    }

}