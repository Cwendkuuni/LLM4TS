import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {
    
    private FDistributionImpl fDistribution;
    
    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(5, 10);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double cumulativeProb = fDistribution.cumulativeProbability(3);
        double expected = Beta.regularizedBeta((5 * 3) / (10 + 5 * 3), 0.5 * 5, 0.5 * 10);
        Assert.assertEquals(expected, cumulativeProb, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double invCumulativeProb = fDistribution.inverseCumulativeProbability(0.7);
        Assert.assertNotEquals(0, invCumulativeProb);
        Assert.assertNotEquals(Double.POSITIVE_INFINITY, invCumulativeProb);

        double invCumulativeProb_zero = fDistribution.inverseCumulativeProbability(0);
        Assert.assertEquals(0, invCumulativeProb_zero, 0.0001);
        
        double invCumulativeProb_one = fDistribution.inverseCumulativeProbability(1);
        Assert.assertEquals(Double.POSITIVE_INFINITY, invCumulativeProb_one, 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double domainLowerBound = fDistribution.getDomainLowerBound(0.5);
        Assert.assertEquals(0, domainLowerBound, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double domainUpperBound = fDistribution.getDomainUpperBound(0.5);
        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        double initialDomain = fDistribution.getInitialDomain(0.5);
        double expected = 10 / (10 - 2.0);
        Assert.assertEquals(expected, initialDomain, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negative() {
        fDistribution.setNumeratorDegreesOfFreedom(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_zero() {
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }
}