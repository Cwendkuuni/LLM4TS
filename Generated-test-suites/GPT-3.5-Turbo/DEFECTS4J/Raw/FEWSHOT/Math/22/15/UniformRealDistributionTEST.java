
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {
    
    private static final double EPSILON = 1e-9;
    
    @Test
    public void testConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(1, distribution.getSupportUpperBound(), EPSILON);
        
        distribution = new UniformRealDistribution(0, 2);
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(2, distribution.getSupportUpperBound(), EPSILON);
        
        distribution = new UniformRealDistribution(2, 0);
        assertEquals(2, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(0, distribution.getSupportUpperBound(), EPSILON);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(2, 1);
    }
    
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        
        assertEquals(1.0, distribution.density(0.5), EPSILON);
        assertEquals(1.0, distribution.density(1), EPSILON);
        assertEquals(0.0, distribution.density(-0.5), EPSILON);
        assertEquals(0.0, distribution.density(1.5), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        
        assertEquals(0.0, distribution.cumulativeProbability(-0.5), EPSILON);
        assertEquals(0.0, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(1.5), EPSILON);
    }
    
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 2);
        assertEquals(1, distribution.getNumericalMean(), EPSILON);
    }
    
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 2);
        assertEquals(1d/3d, distribution.getNumericalVariance(), EPSILON);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 4);
        assertEquals(2, distribution.getSupportLowerBound(), EPSILON);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 4);
        assertEquals(4, distribution.getSupportUpperBound(), EPSILON);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 4);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 4);
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 4);
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 2);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 2);
    }
    
}
