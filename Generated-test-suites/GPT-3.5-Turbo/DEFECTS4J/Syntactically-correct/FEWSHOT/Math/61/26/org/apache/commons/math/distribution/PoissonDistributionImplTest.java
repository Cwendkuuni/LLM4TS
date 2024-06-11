package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    private PoissonDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new PoissonDistributionImpl(5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_illegalArgument() {
        new PoissonDistributionImpl(-5.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(5.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.03368973499542734, distribution.probability(2), 0.000001);
        assertEquals(0.03368973499542734, distribution.probability(3), 0.000001);
        assertEquals(0.07326255555493672, distribution.probability(5), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0);
        assertEquals(0.025235098906982444, distribution.cumulativeProbability(3), 0.000001);
        assertEquals(0.26502591529736186, distribution.cumulativeProbability(6), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.272481779538805, distribution.normalApproximateProbability(6), 0.000001);
        assertEquals(0.011816657870894862, distribution.normalApproximateProbability(2), 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

}