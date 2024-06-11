import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.random.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(2, dist.getSampleSize());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativePopulationSize() {
        new HypergeometricDistribution(-10, 5, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 20);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.00001);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.00001);
        assertEquals(0.25, dist.cumulativeProbability(1), 0.00001);
        assertEquals(1.0, dist.cumulativeProbability(2), 0.00001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.00001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int[] domain = dist.getDomain(10, 5, 2);
        assertEquals(0, domain[0]);
        assertEquals(2, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int lowerDomain = dist.getLowerDomain(10, 5, 2);
        assertEquals(3, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(5, dist.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(10, dist.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(2, dist.getSampleSize());
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int upperDomain = dist.getUpperDomain(5, 2);
        assertEquals(2, upperDomain);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(0.0, dist.probability(-1), 0.00001);
        assertEquals(0.0, dist.probability(0), 0.00001);
        assertEquals(0.25, dist.probability(1), 0.00001);
        assertEquals(0.5, dist.probability(2), 0.00001);
        assertEquals(0.25, dist.probability(3), 0.00001);
        assertEquals(0.0, dist.probability(4), 0.00001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.00001);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.00001);
        assertEquals(0.75, dist.upperCumulativeProbability(1), 0.00001);
        assertEquals(0.5, dist.upperCumulativeProbability(2), 0.00001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.00001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        double result = dist.innerCumulativeProbability(2, 0, -1);
        assertEquals(0.25, result, 0.00001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(1.0, dist.getNumericalMean(), 0.00001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        double variance = dist.getNumericalVariance();
        assertEquals(0.22222, variance, 0.00001);
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        double variance = dist.calculateNumericalVariance();
        assertEquals(0.22222, variance, 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(2, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertEquals(2, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        assertTrue(dist.isSupportConnected());
    }

}