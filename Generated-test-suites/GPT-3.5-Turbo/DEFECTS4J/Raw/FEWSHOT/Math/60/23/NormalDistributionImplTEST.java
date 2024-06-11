package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0);
        
        distribution = new NormalDistributionImpl(-5, 3);
        assertEquals(-5, distribution.getMean(), 0);
        
        distribution = new NormalDistributionImpl(10, 0.5);
        assertEquals(10, distribution.getMean(), 0);   
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
        
        distribution = new NormalDistributionImpl(-5, 3);
        assertEquals(3, distribution.getStandardDeviation(), 0);
        
        distribution = new NormalDistributionImpl(10, 0.5);
        assertEquals(0.5, distribution.getStandardDeviation(), 0);   
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 0.000001);
        assertEquals(0.24197072451914337, distribution.density(-1), 0.000001);
        assertEquals(0.24197072451914337, distribution.density(1), 0.000001);
        
        distribution = new NormalDistributionImpl(-5, 3);
        assertEquals(0.06687782157683045, distribution.density(-5), 0.000001);
        assertEquals(0.0956308289405515, distribution.density(-2), 0.000001);
        assertEquals(0.10648266850745075, distribution.density(-1), 0.000001);
        
        distribution = new NormalDistributionImpl(10, 0.5);
        assertEquals(0.03989422804014327, distribution.density(10), 0.000001);
        assertEquals(0.38940039153599753, distribution.density(11.5), 0.000001);
        assertEquals(0.019383262051887672, distribution.density(9.5), 0.000001);  
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.cumulativeProbability(-5), 0.000001);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.000001);
        assertEquals(1, distribution.cumulativeProbability(5), 0.000001);
        
        distribution = new NormalDistributionImpl(-5, 3);
        assertEquals(0, distribution.cumulativeProbability(-10), 0.000001);
        assertEquals(0.25854369132368564, distribution.cumulativeProbability(-7), 0.000001);
        assertEquals(0.5, distribution.cumulativeProbability(-5), 0.000001);
        
        distribution = new NormalDistributionImpl(10, 0.5);
        assertEquals(0, distribution.cumulativeProbability(9.5), 0.000001);
        assertEquals(0.5, distribution.cumulativeProbability(10), 0.000001);
        assertEquals(0.9999683287581669, distribution.cumulativeProbability(11.2), 0.000001); 
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1.2815515655446004, distribution.inverseCumulativeProbability(0.1), 0.000001);
        assertEquals(0, distribution.inverseCumulativeProbability(0.5), 0.000001);
        assertEquals(1.2815515655446004, distribution.inverseCumulativeProbability(0.9), 0.000001);
        
        distribution = new NormalDistributionImpl(-5, 3);
        assertEquals(-9.844894807030178, distribution.inverseCumulativeProbability(0.1), 0.000001);
        assertEquals(-5, distribution.inverseCumulativeProbability(0.5), 0.000001);
        assertEquals(-0.15510519296982217, distribution.inverseCumulativeProbability(0.9), 0.000001);
        
        distribution = new NormalDistributionImpl(10, 0.5);
        assertEquals(9.02060903379293, distribution.inverseCumulativeProbability(0.1), 0.000001);
        assertEquals(10, distribution.inverseCumulativeProbability(0.5), 0.000001);
        assertEquals(10.97939096620707, distribution.inverseCumulativeProbability(0.9), 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        
        for (int i = 0; i < 100; i++) {
            sample = distribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        }
    }
}