import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, normalDistribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(1);
        assertEquals(1, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            double probability = normalDistribution.cumulativeProbability(3);
            assertEquals(0.8413447460685429, probability, 0);
        } catch (MathException e) {
            fail("An unexpected MathException occurred");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            double inverseProbability = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(1, inverseProbability, 0);
        } catch (MathException e) {
            fail("An unexpected MathException occurred");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double domainLowerBound = normalDistribution.getDomainLowerBound(0.5);
        assertEquals(1, domainLowerBound, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double domainUpperBound = normalDistribution.getDomainUpperBound(0.5);
        assertEquals(1, domainUpperBound, 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double initialDomain = normalDistribution.getInitialDomain(0.5);
        assertEquals(1, initialDomain, 0);
    }
    
}