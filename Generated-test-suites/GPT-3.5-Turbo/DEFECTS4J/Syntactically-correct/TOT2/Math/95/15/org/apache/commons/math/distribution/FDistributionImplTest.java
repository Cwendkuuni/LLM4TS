import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        try {
            double result = fd.cumulativeProbability(2.0);
            assertEquals(0.40771870158, result, 1e-10);
        } catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        try {
            double result = fd.inverseCumulativeProbability(0.7);
            assertEquals(0.5722167903246, result, 1e-10);
        } catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        double result = fd.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 1e-10);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        double result = fd.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 1e-10);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        double result = fd.getInitialDomain(0.5);
        assertEquals(2.0, result, 1e-10);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 1e-10);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, fd.getDenominatorDegreesOfFreedom(), 1e-10);
    }
}