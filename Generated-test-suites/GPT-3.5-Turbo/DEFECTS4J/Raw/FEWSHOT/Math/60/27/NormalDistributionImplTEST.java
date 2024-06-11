
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setup() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), 0.000001);
        assertEquals(0.09973557010048504, normalDistribution.density(-1), 0.000001);
        assertEquals(0.24197072451914337, normalDistribution.density(1), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.000001);
        assertEquals(0.9772498680518208, normalDistribution.cumulativeProbability(2), 0.000001);
        assertEquals(0.022750131948179195, normalDistribution.cumulativeProbability(-2), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 0.000001);
        assertEquals(0.6744897501960817, normalDistribution.inverseCumulativeProbability(0.75), 0.000001);
        assertEquals(-0.6744897501960817, normalDistribution.inverseCumulativeProbability(0.25), 0.000001);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertNotNull(sample);
    }
}
