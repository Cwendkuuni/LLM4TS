package org.apache.commons.math3.distribution;
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, hd.getPopulationSize());
        assertEquals(5, hd.getNumberOfSuccesses());
        assertEquals(3, hd.getSampleSize());
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hd.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, hd.cumulativeProbability(10), 0.0001);
        assertEquals(0.343, hd.cumulativeProbability(5), 0.001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hd.probability(1), 0.0001);
        assertEquals(0.136, hd.probability(3), 0.001);
        assertEquals(0.002, hd.probability(5), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, hd.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.0, hd.upperCumulativeProbability(6), 0.0001);
        assertEquals(0.657, hd.upperCumulativeProbability(3), 0.001);
    }
    
    @Test
    public void testNumericalMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hd.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.642, hd.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, hd.getSupportLowerBound());
    }
    
    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hd.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        assertTrue(hd.isSupportConnected());
    }
}