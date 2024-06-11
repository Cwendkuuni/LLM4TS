import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_ValidInputs() {
        // Typical case
        HypergeometricDistribution hd = new HypergeometricDistribution(100, 50, 10);
        Assert.assertNotNull(hd);
        
        // Lower bound edge case
        hd = new HypergeometricDistribution(1, 0, 0);
        Assert.assertNotNull(hd);
        
        // Upper bound edge case
        hd = new HypergeometricDistribution(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Assert.assertNotNull(hd);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(0, 50, 10);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        HypergeometricDistribution hd = new HypergeometricDistribution(100, -1, 10);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidSampleSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(100, 50, -1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(100, 150, 10);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(100, 50, 150);
    }

    @Test
    public void testCumulativeProbability_ValidInput() {
        HypergeometricDistribution hd = new HypergeometricDistribution(100, 50, 10);
        
        // Typical case
        double result = hd.cumulativeProbability(5);
        Assert.assertEquals(0.8474, result, 0.0001);
        
        // Lower bound edge case
        result = hd.cumulativeProbability(0);
        Assert.assertEquals(0.0013, result, 0.0001);
        
        // Upper bound edge case
        result = hd.cumulativeProbability(10);
        Assert.assertEquals(0.9999, result, 0.0001);
        
        // Exceptional case - x < lower domain
        result = hd.cumulativeProbability(-1);
        Assert.assertEquals(0.0, result, 0.0001);
        
        // Exceptional case - x >= upper domain
        result = hd.cumulativeProbability(11);
        Assert.assertEquals(1.0, result, 0.0001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(100, 50, 10);
        
        // Typical case
        int[] domain = hd.getDomain(100, 50, 10);
        Assert.assertArrayEquals(new int[] {40, 10}, domain);
        
        // Lower bound edge case
        domain = hd.getDomain(1, 0, 0);
        Assert.assertArrayEquals(new int[] {0, 0}, domain);
        
        // Upper bound edge case
        domain = hd.getDomain(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        // Since MAX_VALUE is too large, the actual domain would be [0, MAX_VALUE]
        Assert.assertArrayEquals(new int[] {0, Integer.MAX_VALUE}, domain);
    }
    
    // ... continue with similar tests for other methods ...
    
}