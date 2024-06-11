import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution distribution;
    
    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(50, 25, 10);
    }
    
    @Test
    public void testCumulativeProbability() {
        Assert.assertEquals(0.34166355043514443, distribution.cumulativeProbability(5), 0.000001);
        Assert.assertEquals(0.8901285403050115, distribution.cumulativeProbability(15), 0.000001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(25), 0.0);
    }
    
    @Test
    public void testProbability() {
        Assert.assertEquals(0.05403146313747004, distribution.probability(5), 0.000001);
        Assert.assertEquals(0.011804384485920658, distribution.probability(15), 0.000001);
        Assert.assertEquals(0.0, distribution.probability(30), 0.0);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(5), 0.0);
        Assert.assertEquals(0.6549235522664044, distribution.upperCumulativeProbability(15), 0.000001);
        Assert.assertEquals(0.10987145917901032, distribution.upperCumulativeProbability(30), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(5.0, distribution.getNumericalMean(), 0.0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(1.3076923076923077, distribution.getNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(10, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(distribution.isSupportConnected());
    }
}