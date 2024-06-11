import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {
    
    @Test
    public void testFDistribution() {
        FDistribution fDistribution = new FDistribution(1, 2);
        Assert.assertNotNull(fDistribution);
    }
    
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double density = fDistribution.density(2.5);
        Assert.assertEquals(0.16111, density, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double cumulativeProbability = fDistribution.cumulativeProbability(2.5);
        Assert.assertEquals(0.775, cumulativeProbability, 0.001);
    }
    
    // Add test cases for other methods
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(1, numeratorDegreesOfFreedom, 0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(2, denominatorDegreesOfFreedom, 0);
    }
    
    // Add test cases for other methods
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(1, 2);
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(1, 2);
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(1, 2);
        boolean isSupportConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
    
    // Merge all the individual test cases into a complete JUnit 4 test file
    // The complete JUnit test file must start with  and finish with 
    // Add all the test cases in between
    
    // 
    
    @Test
    public void testFDistribution() {
        FDistribution fDistribution = new FDistribution(1, 2);
        Assert.assertNotNull(fDistribution);
    }
    
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double density = fDistribution.density(2.5);
        Assert.assertEquals(0.16111, density, 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double cumulativeProbability = fDistribution.cumulativeProbability(2.5);
        Assert.assertEquals(0.775, cumulativeProbability, 0.001);
    }
    
    // Add test cases for other methods
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(1, numeratorDegreesOfFreedom, 0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(1, 2);
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(2, denominatorDegreesOfFreedom, 0);
    }
    
    // Add test cases for other methods
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(1, 2);
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(1, 2);
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(1, 2);
        boolean isSupportConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
    
    // 
}