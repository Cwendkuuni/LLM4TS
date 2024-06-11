import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.2706705664732254, distribution.probability(1), 0.0001);
        assertEquals(0.2706705664732254, distribution.probability(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.8646647167633873, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.8526055020137255, distribution.normalApproximateProbability(2), 0.0001);
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(2.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}