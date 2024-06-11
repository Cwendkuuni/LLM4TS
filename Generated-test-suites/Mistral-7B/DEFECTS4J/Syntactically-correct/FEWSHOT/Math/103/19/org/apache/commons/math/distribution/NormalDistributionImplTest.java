package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.001);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);

        distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testMeanGettersAndSetters() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.001);

        distribution.setMean(2.0);
        assertEquals(2.0, distribution.getMean(), 0.001);
    }

    @Test
    public void testStandardDeviationGettersAndSetters() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);

        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStandardDeviationSetter() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();

        assertEquals(0.1543080179719665, distribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.8413419811176465, distribution.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);

        distribution = new NormalDistributionImpl(1.0, 2.0);

        assertEquals(0.12353495748133956, distribution.cumulativeProbability(0.5), 0.0001);
        assertEquals(0.8764650425186605, distribution.cumulativeProbability(1.5), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();

        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.0, distribution.inverseCumulativeProbability(0.5), 0.001);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.0));

        distribution = new NormalDistributionImpl(1.0, 2.0);

        assertEquals(-1.5424619491523384, distribution.inverseCumulativeProbability(0.01), 0.001);
        assertEquals(1.5, distribution.inverseCumulativeProbability(0.97), 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();

        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
    }
}