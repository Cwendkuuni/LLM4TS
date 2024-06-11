import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(-5), 0.0001);
        assertEquals(0.14285714285714285, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.5714285714285714, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(100), 0.0001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.16666666666666666, dist.probability(0), 0.0001);
        assertEquals(0.047619047619047616, dist.probability(1), 0.0001);
        assertEquals(0.35714285714285715, dist.probability(2), 0.0001);
        assertEquals(0.42857142857142855, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(-5), 0.0001);
        assertEquals(0.0, dist.probability(10), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.8571428571428571, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.42857142857142855, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.14285714285714285, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(-5), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(100), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
         HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
         assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
         HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
         assertEquals(0.6428571428571429, dist.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
         HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
         assertEquals(0, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
         HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
         assertEquals(3, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
         HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
         assertTrue(dist.isSupportConnected());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
    }
    
}