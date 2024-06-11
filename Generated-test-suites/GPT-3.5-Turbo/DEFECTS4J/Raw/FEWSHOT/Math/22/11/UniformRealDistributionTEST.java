
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UniformRealDistributionTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test values within range
        assertEquals(1, distribution.density(0.5), EPSILON);
        assertEquals(1, distribution.density(0.1), EPSILON);
        assertEquals(1, distribution.density(0.9), EPSILON);

        // Test values outside range
        assertEquals(0, distribution.density(-0.1), EPSILON);
        assertEquals(0, distribution.density(1.1), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test values within range
        assertEquals(0, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), EPSILON);
        assertEquals(1, distribution.cumulativeProbability(1), EPSILON);

        // Test values outside range
        assertEquals(0, distribution.cumulativeProbability(-0.1), EPSILON);
        assertEquals(1, distribution.cumulativeProbability(1.1), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0 / 12.0, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 1);
    }
}
