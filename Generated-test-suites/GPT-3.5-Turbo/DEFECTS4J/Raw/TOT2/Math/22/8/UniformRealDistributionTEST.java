import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.density(-1), 0.0001);
        assertEquals(0.5, distribution.density(0.5), 0.0001);
        assertEquals(0.0, distribution.density(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0833, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testSupportInclusivity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample < 1);
    }
}