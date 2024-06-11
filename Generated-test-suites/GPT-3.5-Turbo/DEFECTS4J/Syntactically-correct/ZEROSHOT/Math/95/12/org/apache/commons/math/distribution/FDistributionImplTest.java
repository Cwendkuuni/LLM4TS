import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double x = 2.0;
        double expected = 0.873168225;
        double actual = fDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 1e-6);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double p = 0.9;
        double expected = 2.87680406;
        double actual = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 1e-6);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double p = 0.95;
        double expected = 0.0;
        double actual = fDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 1e-6);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double p = 0.95;
        double expected = Double.MAX_VALUE;
        double actual = fDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 1e-6);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double p = 0.95;
        double expected = 1.66666667;
        double actual = fDistribution.getInitialDomain(p);
        assertEquals(expected, actual, 1e-6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double degreesOfFreedom = -1.0;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedomValid() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double degreesOfFreedom = 3.0;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 1e-6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double degreesOfFreedom = -1.0;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedomValid() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double degreesOfFreedom = 3.0;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 1e-6);
    }
}