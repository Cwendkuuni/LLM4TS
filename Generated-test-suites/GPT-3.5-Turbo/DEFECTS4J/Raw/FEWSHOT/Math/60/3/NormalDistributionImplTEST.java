
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.junit.BeforeClass;
import org.junit.Test;

public class NormalDistributionImplTest {

    private static final double DELTA = 0.000000001;
    private static NormalDistributionImpl distribution;

    @BeforeClass
    public static void setUp() {
        distribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, distribution.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, distribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, distribution.density(0), DELTA);
        assertEquals(0.24197072451914337, distribution.density(-1), DELTA);
        assertEquals(0.24197072451914337, distribution.density(1), DELTA);
        assertEquals(0.05399096651318806, distribution.density(-2), DELTA);
        assertEquals(0.05399096651318806, distribution.density(2), DELTA);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0), DELTA);
        assertEquals(0.15865525393145707, distribution.cumulativeProbability(-1), DELTA);
        assertEquals(0.8413447460685429, distribution.cumulativeProbability(1), DELTA);
        assertEquals(0.02275013194817921, distribution.cumulativeProbability(-2), DELTA);
        assertEquals(0.9772498680518208, distribution.cumulativeProbability(2), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, distribution.inverseCumulativeProbability(0), DELTA);
        assertEquals(-1.6448536269514722, distribution.inverseCumulativeProbability(0.05), DELTA);
        assertEquals(-1.2815515655446004, distribution.inverseCumulativeProbability(0.1), DELTA);
        assertEquals(-0.6744897501960817, distribution.inverseCumulativeProbability(0.25), DELTA);
        assertEquals(0.6744897501960817, distribution.inverseCumulativeProbability(0.75), DELTA);
        assertEquals(1.2815515655446004, distribution.inverseCumulativeProbability(0.9), DELTA);
        assertEquals(1.6448536269514722, distribution.inverseCumulativeProbability(0.95), DELTA);
        assertEquals(0, distribution.inverseCumulativeProbability(1), DELTA);
    }

    @Test
    public void testSample() throws MathException {
        // Sample from the distribution and check if it falls within a reasonable range
        double sample = distribution.sample();
        assertEquals(true, sample >= -10 && sample <= 10);
    }
}
