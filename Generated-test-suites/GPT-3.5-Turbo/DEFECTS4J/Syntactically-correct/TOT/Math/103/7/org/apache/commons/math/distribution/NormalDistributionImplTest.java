import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    // Test cases for getMean() method
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, normalDistribution.getMean(), 0.01);
    }

    // Test cases for setMean() method
    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(3.0);
        assertEquals(3.0, normalDistribution.getMean(), 0.01);
    }

    // Test cases for getStandardDeviation() method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(1.5, normalDistribution.getStandardDeviation(), 0.01);
    }

    // Test cases for setStandardDeviation() method
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.01);
    }

    // Test cases for cumulativeProbability() method
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), 0.01);
    }

    // Test cases for inverseCumulativeProbability() method
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), 0.01);
    }

    // Test case for getDomainLowerBound() method
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.4), 0.01);
        assertEquals(0.0, normalDistribution.getDomainLowerBound(0.6), 0.01);
    }

    // Test case for getDomainUpperBound() method
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, normalDistribution.getDomainUpperBound(0.4), 0.01);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.6), 0.01);
    }

    // Test case for getInitialDomain() method
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.4), 0.01);
        assertEquals(1.0, normalDistribution.getInitialDomain(0.6), 0.01);
        assertEquals(0.0, normalDistribution.getInitialDomain(0.5), 0.01);
    }

}