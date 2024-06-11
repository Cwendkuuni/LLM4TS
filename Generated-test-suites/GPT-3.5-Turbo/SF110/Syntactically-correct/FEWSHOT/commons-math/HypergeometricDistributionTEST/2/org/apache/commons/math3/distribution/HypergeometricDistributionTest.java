package org.apache.commons.math3.distribution;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor1() {
        new HypergeometricDistribution(-1, 5, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor2() {
        new HypergeometricDistribution(10, -1, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor3() {
        new HypergeometricDistribution(10, 5, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor4() {
        new HypergeometricDistribution(10, 11, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor5() {
        new HypergeometricDistribution(10, 5, 11);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.75, dist.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.371, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.809, dist.cumulativeProbability(1), 0.001);
        assertEquals(0.956, dist.cumulativeProbability(2), 0.001);
        assertEquals(0.995, dist.cumulativeProbability(3), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.001);
        assertEquals(0.629, dist.upperCumulativeProbability(1), 0.001);
        assertEquals(0.194, dist.upperCumulativeProbability(2), 0.001);
        assertEquals(0.041, dist.upperCumulativeProbability(3), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.009, dist.probability(0), 0.001);
        assertEquals(0.206, dist.probability(1), 0.001);
        assertEquals(0.706, dist.probability(2), 0.001);
        assertEquals(0.068, dist.probability(3), 0.001);
        assertEquals(0.0, dist.probability(4), 0.001);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
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
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, dist.getLowerDomain(10, 5, 3));
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getUpperDomain(5, 3));
    }
}