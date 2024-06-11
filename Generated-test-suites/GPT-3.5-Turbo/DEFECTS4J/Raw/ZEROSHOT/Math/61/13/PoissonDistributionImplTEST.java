import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.3678794, distribution.probability(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.7357589, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.6838625, distribution.normalApproximateProbability(1), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}