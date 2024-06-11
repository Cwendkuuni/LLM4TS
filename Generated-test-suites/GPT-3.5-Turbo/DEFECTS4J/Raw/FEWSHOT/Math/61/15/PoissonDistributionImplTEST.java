package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    private static final double EPSILON = 1e-6;
    private static final int MAX_ITERATIONS = 10000000;
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, dist.getMean(), EPSILON);
        
        dist = new PoissonDistributionImpl(0.0);
        assertEquals(0.0, dist.getMean(), EPSILON);
        
        dist = new PoissonDistributionImpl(10.12345);
        assertEquals(10.12345, dist.getMean(), EPSILON);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(0.14936120510359185, dist.probability(0), EPSILON);
        assertEquals(0.22304180765538772, dist.probability(1), EPSILON);
        assertEquals(0.1672813557415408, dist.probability(2), EPSILON);
        assertEquals(0.10036881344492439, dist.probability(3), EPSILON);
        assertEquals(0.04593984430803102, dist.probability(4), EPSILON);
        assertEquals(0.016783593803743524, dist.probability(5), EPSILON);
        assertEquals(0.004402190044992716, dist.probability(6), EPSILON);
        assertEquals(0.0008464815240913906, dist.probability(7), EPSILON);
        assertEquals(0.00012106950344162726, dist.probability(8), EPSILON);
        assertEquals(1.2879791303778254e-05, dist.probability(9), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(0.049787068367863944, dist.cumulativeProbability(-1), EPSILON);
        assertEquals(0.1991482734714558, dist.cumulativeProbability(0), EPSILON);
        assertEquals(0.5161290322580645, dist.cumulativeProbability(1), EPSILON);
        assertEquals(0.8152633007619044, dist.cumulativeProbability(2), EPSILON);
        assertEquals(0.9645321004218073, dist.cumulativeProbability(3), EPSILON);
        assertEquals(0.9966642947261695, dist.cumulativeProbability(4), EPSILON);
        assertEquals(0.9994481106986232, dist.cumulativeProbability(5), EPSILON);
        assertEquals(0.9999783011725297, dist.cumulativeProbability(6), EPSILON);
        assertEquals(0.9999997793032719, dist.cumulativeProbability(7), EPSILON);
        assertEquals(0.9999999850106377, dist.cumulativeProbability(8), EPSILON);
        assertEquals(0.9999999987906077, dist.cumulativeProbability(9), EPSILON);
        assertEquals(0.9999999999997074, dist.cumulativeProbability(10), EPSILON);
    }
    
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        try {
            dist.normalApproximateProbability(-1);
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
        assertEquals(0.09175170898934373, dist.normalApproximateProbability(0), EPSILON);
        assertEquals(0.2930751867279758, dist.normalApproximateProbability(1), EPSILON);
        assertEquals(0.6138964638193458, dist.normalApproximateProbability(2), EPSILON);
        assertEquals(0.8588639641932449, dist.normalApproximateProbability(3), EPSILON);
        assertEquals(0.9726103107579127, dist.normalApproximateProbability(4), EPSILON);
        assertEquals(0.9977762702123771, dist.normalApproximateProbability(5), EPSILON);
        assertEquals(0.9999966032109436, dist.normalApproximateProbability(6), EPSILON);
        assertEquals(0.9999999837200209, dist.normalApproximateProbability(7), EPSILON);
        assertEquals(0.9999999996316195, dist.normalApproximateProbability(8), EPSILON);
        assertEquals(0.9999999999981152, dist.normalApproximateProbability(9), EPSILON);
    }
    
    @Test
    public void testSample() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= Double.MAX_VALUE);
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(0, dist.getDomainLowerBound(0.0), EPSILON);
        assertEquals(0, dist.getDomainLowerBound(0.5), EPSILON);
        assertEquals(0, dist.getDomainLowerBound(1.0), EPSILON);
        assertEquals(0, dist.getDomainLowerBound(2.5), EPSILON);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.0), EPSILON);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5), EPSILON);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(1.0), EPSILON);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(2.5), EPSILON);
    }
}