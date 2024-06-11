import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    NormalDistributionImpl distribution = new NormalDistributionImpl();
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        distribution.setMean(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.3), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.7), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, distribution.getInitialDomain(0.3), 0.0001);
    }
}