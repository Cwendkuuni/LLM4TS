import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0, distribution.density(-1), 0);
        assertEquals(0.1, distribution.density(1), 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(0.5, distribution.cumulativeProbability(5), 0);
        assertEquals(1, distribution.cumulativeProbability(11), 0);
    }

    @Test
    public void testMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(5, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(8.333, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testSupportBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(10, distribution.getSupportUpperBound(), 0);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample < 10);
    }

}