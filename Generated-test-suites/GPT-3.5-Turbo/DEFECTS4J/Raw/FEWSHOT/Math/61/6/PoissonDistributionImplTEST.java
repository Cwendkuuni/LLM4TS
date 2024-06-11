import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.2706705664732254, distribution.probability(2), 0.0001);
        assertEquals(0.03608940889759734, distribution.probability(4), 0.0001);
        assertEquals(0.0002706705664732254, distribution.probability(8), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.6766764161830634, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.9473477217384705, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(0.9997892919828432, distribution.cumulativeProbability(8), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.9772498680518209, distribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.9999683287581669, distribution.normalApproximateProbability(4), 0.0001);
        assertEquals(1.0, distribution.normalApproximateProbability(8), 0.0001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        int domainLowerBound = distribution.getDomainLowerBound(0.5);
        assertEquals(0, domainLowerBound);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        int domainUpperBound = distribution.getDomainUpperBound(0.5);
        assertEquals(Integer.MAX_VALUE, domainUpperBound);
    }
}