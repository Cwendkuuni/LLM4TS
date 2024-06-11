import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5);
        assertEquals(5, dist.getMean(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3);
        assertEquals(0.14936120510359185, dist.probability(2), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertEquals(0.7851303870302306, dist.cumulativeProbability(3), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(6);
        assertEquals(0.975161, dist.normalApproximateProbability(9), 0.000001);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(7);
        int sample = dist.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5);
        assertEquals(0, dist.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5), 0);
    }
}