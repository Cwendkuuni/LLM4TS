import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testConstructor() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        assertNotNull(fDistribution);
    }

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.cumulativeProbability(0.5);
        assertEquals(0.172484, result, 1e-6);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 1e-6);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 1e-6);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(3.0, result, 1e-6);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        fDistribution.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 1e-6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        fDistribution.setNumeratorDegreesOfFreedom(0.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        fDistribution.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDistribution.getDenominatorDegreesOfFreedom(), 1e-6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        fDistribution.setDenominatorDegreesOfFreedom(0.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(2.0, result, 1e-6);
    }
}