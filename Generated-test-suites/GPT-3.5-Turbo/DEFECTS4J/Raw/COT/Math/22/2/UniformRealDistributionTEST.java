import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.density(0.5);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, result, 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getNumericalMean();
        assertEquals(0.5, result, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getNumericalVariance();
        assertEquals(0.083, result, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getSupportLowerBound();
        assertEquals(0, result, 0.001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.getSupportUpperBound();
        assertEquals(1, result, 0.001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportUpperBoundInclusive();
        assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportConnected();
        assertTrue(result);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.sample();
        assertTrue(result >= 0 && result <= 1);
    }

}