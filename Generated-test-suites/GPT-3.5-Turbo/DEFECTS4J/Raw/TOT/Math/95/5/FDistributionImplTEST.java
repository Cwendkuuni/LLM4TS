import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double x = 1.5;
        double result = fDistribution.cumulativeProbability(x);
        assertEquals(0.05177, result, 0.00001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double p = 0.95;
        double result = fDistribution.inverseCumulativeProbability(p);
        assertEquals(6.94495, result, 0.00001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double p = 0.05;
        double result = fDistribution.getDomainLowerBound(p);
        assertEquals(0.0, result, 0.00001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double p = 0.95;
        double result = fDistribution.getDomainUpperBound(p);
        assertEquals(Double.MAX_VALUE, result, 0.00001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double p = 0.05;
        double result = fDistribution.getInitialDomain(p);
        assertEquals(1.66667, result, 0.00001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setNumeratorDegreesOfFreedom(-1.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}