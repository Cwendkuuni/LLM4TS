import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.BeforeClass;
import org.junit.Test;

public class NormalDistributionImplTest {
    private static NormalDistributionImpl normalDistribution;
    
    @BeforeClass
    public static void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.01);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.5);
        assertEquals(1.5, normalDistribution.getMean(), 0.01);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.01);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.5);
        assertEquals(2.5, normalDistribution.getStandardDeviation(), 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        normalDistribution.setStandardDeviation(-1.0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), 0.01);
        assertEquals(0.6915, normalDistribution.cumulativeProbability(0.5), 0.01);
        assertEquals(0.8413, normalDistribution.cumulativeProbability(1.0), 0.01);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), 0.01);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), 0.01);
        assertEquals(0.0, normalDistribution.getDomainLowerBound(0.75), 0.01);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, normalDistribution.getDomainUpperBound(0.25), 0.01);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), 0.01);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.25), 0.01);
        assertEquals(1.0, normalDistribution.getInitialDomain(0.75), 0.01);
    }
}