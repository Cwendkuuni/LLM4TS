
import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        double result = fDistribution.cumulativeProbability(1.5);
        
        Assert.assertEquals(0.314117, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        double result = fDistribution.inverseCumulativeProbability(0.75);
        
        Assert.assertEquals(1.443635, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability_zero() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        double result = fDistribution.inverseCumulativeProbability(0);
        
        Assert.assertEquals(0, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability_one() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        double result = fDistribution.inverseCumulativeProbability(1);
        
        Assert.assertEquals(Double.POSITIVE_INFINITY, result, 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negative() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_zero() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }
}
