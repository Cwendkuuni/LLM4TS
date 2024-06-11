import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistributionImpl;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        // Test case 1: x <= 0.0, should return 0.0
        FDistributionImpl fdistribution1 = new FDistributionImpl(1.0, 2.0);
        assertEquals(0.0, fdistribution1.cumulativeProbability(0.0), 0.000001);
        
        // Test case 2: x > 0.0
        FDistributionImpl fdistribution2 = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.42220134807184305, fdistribution2.cumulativeProbability(1.0), 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Test case 1: p = 0, should return 0.0
        FDistributionImpl fdistribution1 = new FDistributionImpl(1.0, 2.0);
        assertEquals(0.0, fdistribution1.inverseCumulativeProbability(0.0), 0.000001);
        
        // Test case 2: p = 1, should return Double.POSITIVE_INFINITY
        FDistributionImpl fdistribution2 = new FDistributionImpl(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, fdistribution2.inverseCumulativeProbability(1.0), 0.000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        // Test case 1: p = 0, should return 0.0
        FDistributionImpl fdistribution = new FDistributionImpl(1.0, 2.0);
        assertEquals(0.0, fdistribution.getDomainLowerBound(0.0), 0.000001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        // Test case 1: p = 0, should return Double.MAX_VALUE
        FDistributionImpl fdistribution = new FDistributionImpl(1.0, 2.0);
        assertEquals(Double.MAX_VALUE, fdistribution.getDomainUpperBound(0.0), 0.000001);
    }
    
    @Test
    public void testGetInitialDomain() {
        // Test case 1: p = 0, should return d / (d - 2.0)
        FDistributionImpl fdistribution = new FDistributionImpl(1.0, 2.0);
        assertEquals(0.0, fdistribution.getInitialDomain(0.0), 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        // Test case 1: degreesOfFreedom <= 0, should throw IllegalArgumentException
        FDistributionImpl fdistribution = new FDistributionImpl(1.0, 2.0);
        fdistribution.setNumeratorDegreesOfFreedom(0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        // Test case 1: degreesOfFreedom <= 0, should throw IllegalArgumentException
        FDistributionImpl fdistribution = new FDistributionImpl(1.0, 2.0);
        fdistribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}