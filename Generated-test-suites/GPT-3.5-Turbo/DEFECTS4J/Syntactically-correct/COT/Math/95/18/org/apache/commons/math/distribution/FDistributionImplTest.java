
import org.junit.Test;
import org.apache.commons.math.MathException;

import static org.junit.Assert.*;

public class FDistributionImplTest {

    private static final double EPSILON = 1e-10;
    
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 2.0);
        
        // Test for x <= 0
        assertEquals(0.0, distribution.cumulativeProbability(-5.0), EPSILON);
        assertEquals(0.0, distribution.cumulativeProbability(0.0), EPSILON);
        
        // Test for x > 0
        assertEquals(0.68695090, distribution.cumulativeProbability(0.5), EPSILON);
        assertEquals(0.18126925, distribution.cumulativeProbability(1.0), EPSILON);
        assertEquals(0.03882137, distribution.cumulativeProbability(1.5), EPSILON);
        assertEquals(0.54085205, distribution.cumulativeProbability(2.0), EPSILON);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 2.0);
        
        // Test for p = 0
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), EPSILON);
        
        // Test for p = 1
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), EPSILON);
        
        // Test for 0 < p < 1
        assertEquals(0.10303359, distribution.inverseCumulativeProbability(0.1), EPSILON);
        assertEquals(0.39701544, distribution.inverseCumulativeProbability(0.2), EPSILON);
        assertEquals(0.71116435, distribution.inverseCumulativeProbability(0.3), EPSILON);
        assertEquals(1.17972657, distribution.inverseCumulativeProbability(0.4), EPSILON);
        assertEquals(1.84774379, distribution.inverseCumulativeProbability(0.6), EPSILON);
        assertEquals(5.78675268, distribution.inverseCumulativeProbability(0.9), EPSILON);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 2.0);
        
        assertEquals(0.0, distribution.getDomainLowerBound(0.0), EPSILON);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), EPSILON);
        assertEquals(0.0, distribution.getDomainLowerBound(1.0), EPSILON);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 2.0);
        
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.0), EPSILON);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), EPSILON);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(1.0), EPSILON);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 2.0);
        
        // Test for p = 0
        assertEquals(2.0, distribution.getInitialDomain(0.0), EPSILON);
        
        // Test for p = 0.5
        assertEquals(2.0, distribution.getInitialDomain(0.5), EPSILON);
        
        // Test for p = 1
        assertEquals(2.0, distribution.getInitialDomain(1.0), EPSILON);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 2.0);
        distribution.setNumeratorDegreesOfFreedom(0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 2.0);
        distribution.setDenominatorDegreesOfFreedom(0.0);
    }
}
