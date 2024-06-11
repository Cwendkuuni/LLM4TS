import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.001);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(5.0);
        assertEquals(5.0, normalDistribution.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.5);
        assertEquals(2.5, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.001);
        assertEquals(0.1587, normalDistribution.cumulativeProbability(-1), 0.001);
        assertEquals(0.8413, normalDistribution.cumulativeProbability(1), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, normalDistribution.inverseCumulativeProbability(0), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.2), 0.001);
        assertEquals(0.0, normalDistribution.getDomainLowerBound(0.8), 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, normalDistribution.getDomainUpperBound(0.2), 0.001);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.8), 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.2), 0.001);
        assertEquals(1.0, normalDistribution.getInitialDomain(0.8), 0.001);
        assertEquals(0.0, normalDistribution.getInitialDomain(0.5), 0.001);
    }
}