import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        try {
            double result = fd.cumulativeProbability(2);
            assertEquals(0.8571428571428571, result, 0.0001);
        } catch (MathException e) {
            fail("Exception thrown:" + e);
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        try {
            double result = fd.inverseCumulativeProbability(0.8571428571428571);
            assertEquals(2, result, 0.0001);
        } catch (MathException e) {
            fail("Exception thrown:" + e);
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        double result = fd.getDomainLowerBound(0.6);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        double result = fd.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        double result = fd.getInitialDomain(0.6);
        assertEquals(1.3333333333333333, result, 0.0001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        fd.setNumeratorDegreesOfFreedom(5);
        assertEquals(5, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        double result = fd.getNumeratorDegreesOfFreedom();
        assertEquals(3, result, 0.0001);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        fd.setDenominatorDegreesOfFreedom(6);
        assertEquals(6, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        double result = fd.getDenominatorDegreesOfFreedom();
        assertEquals(4, result, 0.0001);
    }
}