import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double mean = distribution.getMean();
        assertEquals(0.0, mean, 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(2.5);
        double mean = distribution.getMean();
        assertEquals(2.5, mean, 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double sd = distribution.getStandardDeviation();
        assertEquals(1.0, sd, 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        double sd = distribution.getStandardDeviation();
        assertEquals(3.0, sd, 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double probability = distribution.cumulativeProbability(1.0);
        assertEquals(0.8413, probability, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double value = distribution.inverseCumulativeProbability(0.8413);
        assertEquals(1.0, value, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityLowerBound() 
        throws MathException {
            NormalDistributionImpl distribution = new NormalDistributionImpl();
            double value = distribution.inverseCumulativeProbability(0);
            assertEquals(Double.NEGATIVE_INFINITY, value, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityUpperBound() 
        throws MathException {
            NormalDistributionImpl distribution = new NormalDistributionImpl();
            double value = distribution.inverseCumulativeProbability(1);
            assertEquals(Double.POSITIVE_INFINITY, value, 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double bound = distribution.getDomainLowerBound(0.5);
        assertEquals(0.0, bound, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double bound = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, bound, 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double domain = distribution.getInitialDomain(0.5);
        assertEquals(0.0, domain, 0.0001);
    }
}