import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.5);
        assertEquals(1.5, normalDistribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.5);
        assertEquals(2.5, normalDistribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            double result = normalDistribution.cumulativeProbability(1.0);
            assertEquals(0.8413447460685429, result, 0.000001);
        } catch (MathException e) {
            // Handle exception
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0.0, result, 0);
        } catch (MathException e) {
            // Handle exception
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result = normalDistribution.getDomainLowerBound(0.4);
        assertEquals(-Double.MAX_VALUE, result, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result = normalDistribution.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result = normalDistribution.getInitialDomain(0.5);
        assertEquals(1.0, result, 0);
    }

}