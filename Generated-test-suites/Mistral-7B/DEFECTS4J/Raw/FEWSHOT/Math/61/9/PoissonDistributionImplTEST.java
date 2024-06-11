package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerators;

import org.junit.Test;

public class PoissonDistributionImplTest {
    @Test
    public void testConstructionWithPositiveMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testConstructionWithZeroMean() {
        assertThrows(MathRuntimeException.class, () -> new PoissonDistributionImpl(0.0));
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        assertEquals(0.0, dist.probability(-1), Precision.SAFE_EPSILON);
        assertEquals(0.073895605, dist.probability(0), Precision.SAFE_EPSILON);
        assertEquals(0.135335283, dist.probability(1), Precision.SAFE_EPSILON);
        assertEquals(0.180459369, dist.probability(2), Precision.SAFE_EPSILON);
        assertEquals(0.180459369, dist.probability(3), Precision.SAFE_EPSILON);
        assertEquals(0.154325226, dist.probability(4), Precision.SAFE_EPSILON);
        assertEquals(0.116286316, dist.probability(5), Precision.SAFE_EPSILON);

        PoissonDistribution pd = new PoissonDistribution(2.5);
        assertEquals(dist.probability(2), pd.probability(2), Precision.SAFE_EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        assertEquals(0.0, dist.cumulativeProbability(Integer.MIN_VALUE), Precision.SAFE_EPSILON);
        assertEquals(0.073895605, dist.cumulativeProbability(0), Precision.SAFE_EPSILON);
        assertEquals(0.258567853, dist.cumulativeProbability(1), Precision.SAFE_EPSILON);
        assertEquals(0.472390452, dist.cumulativeProbability(2), Precision.SAFE_EPSILON);
        assertEquals(0.684896431, dist.cumulativeProbability(3), Precision.SAFE_EPSILON);
        assertEquals(0.853823682, dist.cumulativeProbability(4), Precision.SAFE_EPSILON);
        assertEquals(0.978840823, dist.cumulativeProbability(5), Precision.SAFE_EPSILON);
        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), Precision.SAFE_EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);

        int[] xValues = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        double[] expectedProbs = { 0.04555744, 0.09559482, 0.13533528, 0.15432523, 0.13986856, 0.10563556, 0.06611482, 0.03981257, 0.02093327, 0.00982154, 0.00376253 };

        for (int i = 0; i < xValues.length; i++) {
            double prob = dist.normalApproximateProbability(xValues[i]);
            assertEquals(expectedProbs[i], prob, Precision.SAFE_EPSILON);
        }
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        RandomDataGenerators rdg = new RandomDataGenerators();

        for (int i = 0; i < 1000; i++) {
            int sample = dist.sample();
            assertEquals(Integer.MIN_VALUE, sample, i < 10);
            assertEquals(Integer.MAX_VALUE, sample, i >= 999);
            assertEquals(Math.round(dist.cumulativeProbability(sample) * 1000.0), i, Precision.SAFE_EPSILON);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        double[] probValues = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.99, 0.999 };
        int[] expectedValues = { 1, 2, 2, 3, 4, 4, 5, 6, 6, 9, 11, 13 };

        for (int i = 0; i < probValues.length; i++) {
            int value = dist.inverseCumulativeProbability(probValues[i]);
            assertEquals(expectedValues[i], value);
        }
    }
}