package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanZeroStdDevOne() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);

        double density = distribution.density(0.0);
        assertEquals(0.07978847127, density, 0.0001);

        double cumulativeProbability = 0.5;
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(0.0, inverseCumulativeProbability, 0.0001);

        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        double sample = normalDistribution.sample();
        assertEquals(0.0, sample, 0.01);

        cumulativeProbability = 0.999;
        inverseCumulativeProbability = distribution.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(3.09875537439, inverseCumulativeProbability, 0.001);
    }

    @Test
    public void testNormalDistributionImpl_MeanOneStdDevTwo() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);

        double density = distribution.density(3.0);
        assertEquals(0.11353994153, density, 0.0001);

        double cumulativeProbability = 0.95;
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(1.96412794815, inverseCumulativeProbability, 0.001);

        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double sample = normalDistribution.sample();
        double expectedSample = 1.0 + 2.0 * FastMath.random();
        assertTrue(Precision.isClose(sample, expectedSample, 0.1));

        cumulativeProbability = 0.001;
        inverseCumulativeProbability = distribution.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(-2.32634783153, inverseCumulativeProbability, 0.01);

        // Test exception handling
        try {
            NormalDistributionImpl distributionWithNegativeStdDev = new NormalDistributionImpl(1, -2);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Standard deviation must be positive", e.getMessage());
        }

        // Test inverse cumulative probability accuracy
        double inverseCumProbabilityAccuracy = 1e-6;
        NormalDistributionImpl distributionWithInverseCumProbAccuracy = new NormalDistributionImpl(1, 2, inverseCumProbabilityAccuracy);
        double inverseCumulativeProbabilityWithAccuracy = distributionWithInverseCumProbAccuracy.inverseCumulativeProbability(0.5);
        assertEquals(1.57615682667, inverseCumulativeProbabilityWithAccuracy, inverseCumProbabilityAccuracy);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_CumulativeProbabilityOutsideRange() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = 1.1;
        distribution.cumulativeProbability(cumulativeProbability);
    }
}