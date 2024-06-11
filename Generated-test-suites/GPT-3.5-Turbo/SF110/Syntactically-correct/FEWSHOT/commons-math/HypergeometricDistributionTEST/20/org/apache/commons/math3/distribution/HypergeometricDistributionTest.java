package org.apache.commons.math3.distribution;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.05263157894736842, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.2631578947368421, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.7368421052631579, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.047619047619047616, distribution.probability(0), 0.0001);
        assertEquals(0.4, distribution.probability(1), 0.0001);
        assertEquals(0.47619047619047616, distribution.probability(2), 0.0001);
        assertEquals(0.09523809523809523, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.9473684210526316, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.7368421052631579, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.2631578947368421, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.7875, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(true, distribution.isSupportConnected());
    }

}