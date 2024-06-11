import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(0.17603266338214976, distribution.density(0), 1e-9);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(0.3085375387259869, distribution.cumulativeProbability(0), 1e-9);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(-0.9544997361036416, distribution.inverseCumulativeProbability(0.1), 1e-9);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.1), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.1), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(-1, distribution.getInitialDomain(0.1), 0);
    }
}