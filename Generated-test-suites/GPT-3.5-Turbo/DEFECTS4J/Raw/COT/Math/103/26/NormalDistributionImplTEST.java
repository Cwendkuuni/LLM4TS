import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        double mean = distribution.getMean();
        
        assertEquals(1.0, mean, 0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        distribution.setMean(2.0);
        
        double mean = distribution.getMean();
        
        assertEquals(2.0, mean, 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        double sd = distribution.getStandardDeviation();
        
        assertEquals(2.0, sd, 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        distribution.setStandardDeviation(3.0);
        
        double sd = distribution.getStandardDeviation();
        
        assertEquals(3.0, sd, 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        double cumulativeProbability = distribution.cumulativeProbability(2.0);
        
        assertEquals(0.6914624613, cumulativeProbability, 1e-10);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
        
        assertEquals(1.0, inverseCumulativeProbability, 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        double domainLowerBound = distribution.getDomainLowerBound(0.5);
        
        assertEquals(1.0, domainLowerBound, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        double domainUpperBound = distribution.getDomainUpperBound(0.5);
        
        assertEquals(1.0, domainUpperBound, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        
        double initialDomain = distribution.getInitialDomain(0.5);
        
        assertEquals(3.0, initialDomain, 0);
    }
}

