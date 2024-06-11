import org.junit.Test;

import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.22313016014842982, poissonDistribution.probability(1), 0.0000001);
        assertEquals(0.3346952402226444, poissonDistribution.probability(2), 0.0000001);
        assertEquals(0.2500479123063156, poissonDistribution.probability(3), 0.0000001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.557825400371075, poissonDistribution.cumulativeProbability(1), 0.0000001);
        assertEquals(0.8925206405939618, poissonDistribution.cumulativeProbability(2), 0.0000001);
        assertEquals(0.9785685529002774, poissonDistribution.cumulativeProbability(3), 0.0000001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.7988615974098185, poissonDistribution.normalApproximateProbability(1), 0.0000001);
        assertEquals(0.9563350028394986, poissonDistribution.normalApproximateProbability(2), 0.0000001);
        assertEquals(0.9973737395041058, poissonDistribution.normalApproximateProbability(3), 0.0000001);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        for (int i = 0; i < 1000; i++) {
            int sample = poissonDistribution.sample();
            assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, poissonDistribution.getMean(), 0.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(0, poissonDistribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.5));
    }
}