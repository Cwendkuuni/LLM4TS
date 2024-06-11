import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    // Test cumulativeProbability method
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = distribution.cumulativeProbability(0);
        assertEquals(0.5, p, 0.0001);   
    }

    // Test inverseCumulativeProbability method
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double x = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, x, 0.0001);
    }

    // Test getDomainLowerBound method
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double lb = distribution.getDomainLowerBound(0.5);
        assertEquals(-Double.MAX_VALUE, lb, 0.0001);
    }

    // Test getDomainUpperBound method
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double ub = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, ub, 0.0001);
    }

    // Test getInitialDomain method
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double id = distribution.getInitialDomain(0.5);
        assertEquals(1, id, 0.0001);
    }
}