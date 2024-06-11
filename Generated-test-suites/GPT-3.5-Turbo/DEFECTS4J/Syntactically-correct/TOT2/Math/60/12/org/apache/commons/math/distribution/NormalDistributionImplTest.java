import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014337, normalDistribution.density(0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            normalDistribution.sample();
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.3), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.7), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normalDistribution.getInitialDomain(0.2), 0.0001);
    }
}