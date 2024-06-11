import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case - positive value
        double cumulativeProb = fd.cumulativeProbability(6);
        assertEquals(0.789, cumulativeProb, 0.001);
        
        // Typical use case - zero value
        cumulativeProb = fd.cumulativeProbability(0);
        assertEquals(0, cumulativeProb, 0.001);
        
        // Edge case - negative value
        cumulativeProb = fd.cumulativeProbability(-5);
        assertEquals(0, cumulativeProb, 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case - positive probability
        double inverseCumulativeProb = fd.inverseCumulativeProbability(0.5);
        assertEquals(0.904, inverseCumulativeProb, 0.001);
        
        // Typical use case - zero probability
        inverseCumulativeProb = fd.inverseCumulativeProbability(0);
        assertEquals(0, inverseCumulativeProb, 0.001);
        
        // Typical use case - maximum probability
        inverseCumulativeProb = fd.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProb, 0.001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case - positive probability
        double domain = fd.getDomainLowerBound(0.3);
        assertEquals(0.0, domain, 0.001);
        
        // Typical use case - zero probability
        domain = fd.getDomainLowerBound(0);
        assertEquals(0.0, domain, 0.001);
        
        // Edge case - negative probability
        domain = fd.getDomainLowerBound(-0.5);
        assertEquals(0.0, domain, 0.001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case - positive probability
        double domain = fd.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, domain, 0.001);
        
        // Typical use case - zero probability
        domain = fd.getDomainUpperBound(0);
        assertEquals(Double.MAX_VALUE, domain, 0.001);
        
        // Edge case - negative probability
        domain = fd.getDomainUpperBound(-0.5);
        assertEquals(Double.MAX_VALUE, domain, 0.001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        double initialDomain = fd.getInitialDomain(0.5);
        assertEquals(1.25, initialDomain, 0.001);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case - positive value
        fd.setNumeratorDegreesOfFreedom(7);
        assertEquals(7, fd.getNumeratorDegreesOfFreedom(), 0.001);
        
        // Edge case - zero value
        fd.setNumeratorDegreesOfFreedom(0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case - positive value
        fd.setDenominatorDegreesOfFreedom(7);
        assertEquals(7, fd.getDenominatorDegreesOfFreedom(), 0.001);
        
        // Edge case - zero value
        fd.setDenominatorDegreesOfFreedom(0);
    }
}