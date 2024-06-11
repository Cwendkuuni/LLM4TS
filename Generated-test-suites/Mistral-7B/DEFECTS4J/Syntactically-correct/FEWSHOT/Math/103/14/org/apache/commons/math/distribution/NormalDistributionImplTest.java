package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);

        NormalDistribution distZeroMean = new NormalDistributionImpl();
        assertEquals(0.0, distZeroMean.getMean(), 0.001);
        assertEquals(1.0, distZeroMean.getStandardDeviation(), 0.001);

        assertThrows(IllegalArgumentException.class, () -> new NormalDistributionImpl(-1.0, 0.5));
        assertThrows(IllegalArgumentException.class, () -> new NormalDistributionImpl(0.0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> new NormalDistributionImpl(-1.0, -1.0));
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        double[] values = {-3.0, -1.5, -0.5, 0.0, 0.5, 1.5, 3.0};
        double[] expectedCumProbs = {0.0000308572183948, 0.0797884717664, 0.682689492137, 0.5, 0.5, 0.920295666255, 0.997368421053};

        for (int i = 0; i < values.length; i++) {
            double cumProb = dist.cumulativeProbability(values[i]);
            assertEquals(expectedCumProbs[i], cumProb, 0.001);
        }

        // Test edge cases
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double epsilon = 0.0001;

        assertEquals(0.0, dist.cumulativeProbability(lowerBound), epsilon);
        assertEquals(0.5, dist.cumulativeProbability(getMean(dist)), epsilon);
        assertEquals(1.0, dist.cumulativeProbability(upperBound), epsilon);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        double[] probabilities = {0.0, 0.1, 0.5, 0.9};
        double[] expectedValues = {-1.282122683385, -0.450509485896, 0.0, 0.631465562932};

        for (int i = 0; i < probabilities.length; i++) {
            double value = dist.inverseCumulativeProbability(probabilities[i]);
            assertEquals(expectedValues[i], value, 0.001);
        }

        // Test edge cases
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(0.0));
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.0));
    }

    // Helper method to get the mean of a NormalDistribution
    private double getMean(NormalDistribution dist) {
        return dist.getMean();
    }
}