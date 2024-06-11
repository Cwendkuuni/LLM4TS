import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.03368973499542734, distribution.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.12465201948308109, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.9297175826859722, distribution.normalApproximateProbability(10), 0.0001);
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
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}