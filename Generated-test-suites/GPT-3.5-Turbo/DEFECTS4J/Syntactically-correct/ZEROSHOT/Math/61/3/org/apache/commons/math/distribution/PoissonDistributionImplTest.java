import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.006737946999085467, distribution.probability(3), 1e-6);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.7621834629725637, distribution.cumulativeProbability(8), 1e-6);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.7621834629725637, distribution.normalApproximateProbability(8), 1e-6);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
    }
}