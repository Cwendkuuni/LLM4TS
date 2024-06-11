
import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        // Test a valid value of x within the range
        double x = 1.5;
        double expected = 0.7849797035566999;
        double delta = 1e-15;
        double actual = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, delta);
        
        // Test x = 0
        x = 0;
        expected = 0;
        actual = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, delta);
        
        // Test x < 0
        x = -1;
        expected = 0;
        actual = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, delta);

        // TODO: Add additional test cases for edge values and exceptions
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        // Test a valid value of p within the range
        double p = 0.7;
        double expected = 1.0851474416797772;
        double delta = 1e-15;
        double actual = fDistribution.inverseCumulativeProbability(p);
        Assert.assertEquals(expected, actual, delta);
        
        // Test p = 0
        p = 0;
        expected = 0;
        actual = fDistribution.inverseCumulativeProbability(p);
        Assert.assertEquals(expected, actual, delta);
        
        // Test p = 1
        p = 1;
        expected = Double.POSITIVE_INFINITY;
        actual = fDistribution.inverseCumulativeProbability(p);
        Assert.assertEquals(expected, actual, delta);
        
        // TODO: Add additional test cases for edge values and exceptions
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        // Test a valid value of p
        double p = 0.1;
        double expected = 0.0;
        double actual = fDistribution.getDomainLowerBound(p);
        Assert.assertEquals(expected, actual, 0);
        
        // Test p = 0
        p = 0;
        expected = 0.0;
        actual = fDistribution.getDomainLowerBound(p);
        Assert.assertEquals(expected, actual, 0);
        
        // TODO: Add additional test cases for edge values and exceptions
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        // Test a valid value of p
        double p = 0.9;
        double expected = Double.MAX_VALUE;
        double actual = fDistribution.getDomainUpperBound(p);
        Assert.assertEquals(expected, actual, 0);
        
        // Test p = 1
        p = 1;
        expected = Double.MAX_VALUE;
        actual = fDistribution.getDomainUpperBound(p);
        Assert.assertEquals(expected, actual, 0);
        
        // TODO: Add additional test cases for edge values and exceptions
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        // Test a valid value of p
        double p = 0.5;
        double expected = 5.0;
        double actual = fDistribution.getInitialDomain(p);
        Assert.assertEquals(expected, actual, 0);
        
        // Test when degrees of freedom is 2
        fDistribution.setDenominatorDegreesOfFreedom(2);
        expected = Double.POSITIVE_INFINITY;
        actual = fDistribution.getInitialDomain(p);
        Assert.assertEquals(expected, actual, 0);
        
        // TODO: Add additional test cases for edge values and exceptions
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        // Test a valid value of degreesOfFreedom
        double degreesOfFreedom = 2;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        
        // Test an invalid value of degreesOfFreedom
        degreesOfFreedom = 0;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        
        // Test a valid value of degreesOfFreedom
        double degreesOfFreedom = 4;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        
        // Test an invalid value of degreesOfFreedom
        degreesOfFreedom = 0;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
    }
}

