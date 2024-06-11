import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.density(1.0), 0.175327465, 0.000000001);
        Assert.assertEquals(fDistribution.density(2.0), 0.126298715, 0.000000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.cumulativeProbability(1.0), 0.684685972, 0.000000001);
        Assert.assertEquals(fDistribution.cumulativeProbability(2.0), 0.852850144, 0.000000001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumeratorDF() {
        FDistribution fDistribution = new FDistribution(-5, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDenominatorDF() {
        FDistribution fDistribution = new FDistribution(5, -10);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.getNumeratorDegreesOfFreedom(), 5, 0.000000001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.getDenominatorDegreesOfFreedom(), 10, 0.000000001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.getNumericalMean(), 1.25, 0.000000001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.getNumericalVariance(), 1.25, 0.000000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.getSupportLowerBound(), 0, 0.000000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertEquals(fDistribution.getSupportUpperBound(), Double.POSITIVE_INFINITY, 0.000000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        Assert.assertTrue(fDistribution.isSupportConnected());
    }
}