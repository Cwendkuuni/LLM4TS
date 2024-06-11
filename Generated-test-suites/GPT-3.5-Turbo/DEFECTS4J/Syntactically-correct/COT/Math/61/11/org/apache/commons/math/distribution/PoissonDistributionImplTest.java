import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, distribution.getMean(), 0.001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.2706705664732254, distribution.probability(3), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.42319008112684365, distribution.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        assertEquals(0.439592626192205, distribution.normalApproximateProbability(3), 0.001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6.0);
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(7.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}