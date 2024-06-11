import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        assertEquals(0.090223, dist.probability(2), 0.000001);
        assertEquals(0.270670, dist.probability(1), 0.000001);
        assertEquals(0.270670, dist.probability(3), 0.000001);
        assertEquals(0.180447, dist.probability(0), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        assertEquals(0.857123, dist.cumulativeProbability(2), 0.000001);
        assertEquals(0.776869, dist.cumulativeProbability(1), 0.000001);
        assertEquals(0.950213, dist.cumulativeProbability(3), 0.000001);
        assertEquals(0.406005, dist.cumulativeProbability(0), 0.000001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= Integer.MAX_VALUE);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(-2.0);
    }

    @Test
    public void testMeanGetter() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        assertEquals(2.0, dist.getMean(), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        assertEquals(0.957025, dist.normalApproximateProbability(2), 0.000001);
        assertEquals(0.841345, dist.normalApproximateProbability(1), 0.000001);
        assertEquals(0.977249, dist.normalApproximateProbability(3), 0.000001);
        assertEquals(0.735773, dist.normalApproximateProbability(0), 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        assertEquals(0, dist.getDomainLowerBound(0.5));
        assertEquals(0, dist.getDomainLowerBound(0.0));
        assertEquals(0, dist.getDomainLowerBound(-1.0));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5));
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.0));
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(-1.0));
    }

}