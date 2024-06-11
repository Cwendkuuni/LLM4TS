import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014337, normalDistribution.density(0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 1e-9);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertTrue(normalDistribution.sample() >= Double.NEGATIVE_INFINITY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, normalDistribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.2), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.8), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normalDistribution.getInitialDomain(0.3), 0);
    }

}