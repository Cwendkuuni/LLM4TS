import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PoissonDistributionImplTest {
    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, dist.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        assertEquals(0.030197383, dist.probability(0), 1e-9);
        assertEquals(0.105690977, dist.probability(1), 1e-9);
        assertEquals(0.184969787, dist.probability(2), 1e-9);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        assertEquals(0.030197383, dist.cumulativeProbability(0), 1e-9);
        assertEquals(0.13588836, dist.cumulativeProbability(1), 1e-9);
        assertEquals(0.320858147, dist.cumulativeProbability(2), 1e-9);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        assertEquals(0.027564497, dist.normalApproximateProbability(0), 1e-9);
        assertEquals(0.133620225, dist.normalApproximateProbability(1), 1e-9);
        assertEquals(0.333107091, dist.normalApproximateProbability(2), 1e-9);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        int sample = dist.sample();
        assertTrue(sample >= 0);
        assertTrue(sample < Integer.MAX_VALUE);
    }
}