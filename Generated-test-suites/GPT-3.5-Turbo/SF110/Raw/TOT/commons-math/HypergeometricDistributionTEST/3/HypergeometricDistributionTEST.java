import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.random.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor_validArguments() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
            Assert.assertNotNull(distribution);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativePopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-10, 50, 20);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_zeroPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(0, 50, 20);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativeNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, -10, 20);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativeSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, -20);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_numberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 150, 20);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_sampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 120);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(50, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(100, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(20, distribution.getSampleSize());
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(10.0, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(4.2857, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(30, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(20, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testProbability_withinDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0.023647, distribution.probability(10), 0.0001);
        Assert.assertEquals(0.057151, distribution.probability(15), 0.0001);
        Assert.assertEquals(0.0, distribution.probability(25), 0.0001);
    }
    
    @Test
    public void testProbability_outsideDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0.0, distribution.probability(5), 0.0001);
        Assert.assertEquals(0.0, distribution.probability(30), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability_withinDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0.087154, distribution.cumulativeProbability(10), 0.0001);
        Assert.assertEquals(0.734809, distribution.cumulativeProbability(15), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(25), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability_outsideDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(5), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(30), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability_withinDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(10), 0.0001);
        Assert.assertEquals(0.912845, distribution.upperCumulativeProbability(15), 0.0001);
        Assert.assertEquals(0.45061, distribution.upperCumulativeProbability(25), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability_outsideDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(5), 0.0001);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(30), 0.0001);
    }
}