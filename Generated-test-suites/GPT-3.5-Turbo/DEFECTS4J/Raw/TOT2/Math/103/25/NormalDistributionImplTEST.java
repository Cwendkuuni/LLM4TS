import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Test
    public void testGetMean() {
        normalDistribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testSetMean() {
        normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(10.0);
        assertEquals(10.0, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        normalDistribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(3.0);
        assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.0), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.5), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.25), 0.0);
    }

}