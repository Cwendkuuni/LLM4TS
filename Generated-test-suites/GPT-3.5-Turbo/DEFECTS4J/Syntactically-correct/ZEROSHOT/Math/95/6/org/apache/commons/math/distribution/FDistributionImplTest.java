import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);
        
        // Test with x <= 0
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        
        // Test with x > 0
        assertEquals(0.8571, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.9502, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.9761, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);
        
        // Test with p = 0
        assertEquals(0.0, distribution.inverseCumulativeProbability(0), 0.0001);
        
        // Test with p = 1
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0.0001);
        
        // Test with p between 0 and 1
        assertEquals(0.425, distribution.inverseCumulativeProbability(0.2), 0.0001);
        assertEquals(1.4083, distribution.inverseCumulativeProbability(0.5), 0.0001);
        assertEquals(3.8937, distribution.inverseCumulativeProbability(0.9), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);
        
        assertEquals(0.0, distribution.getDomainLowerBound(0), 0.0001);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0001);
        assertEquals(0.0, distribution.getDomainLowerBound(1), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);
        
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0), 0.0001);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0001);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(1), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);

        assertEquals(2.0, distribution.getInitialDomain(0), 0.0001);
        assertEquals(2.0, distribution.getInitialDomain(0.5), 0.0001);
        assertEquals(2.0, distribution.getInitialDomain(1), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);

        distribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);

        assertEquals(1.0, distribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);

        distribution.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(1, 2);

        assertEquals(2.0, distribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}