
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;
import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(2.5, distribution.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(1.5);
        assertEquals(1.5, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.inverseCumulativeProbability(0.5), 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.3), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.7), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, distribution.getInitialDomain(0.3), 0);
    }
}
