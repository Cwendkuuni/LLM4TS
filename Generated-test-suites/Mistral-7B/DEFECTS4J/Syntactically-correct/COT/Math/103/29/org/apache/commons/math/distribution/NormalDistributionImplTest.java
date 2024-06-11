package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_Mean0Stddev1() {
        NormalDistribution distribution = new NormalDistributionImpl();
        NormalDistribution expected = new NormalDistribution(0.0, 1.0);

        assertEquals(expected, distribution);
    }

    @Test
    public void testNormalDistributionImpl_Mean_Set() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double mean = 3.0;
        distribution.setMean(mean);

        NormalDistribution expected = new NormalDistribution(mean, 1.0);
        assertEquals(expected, distribution);
    }

    @Test
    public void testNormalDistributionImpl_Stddev_Set() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double stddev = 2.5;
        distribution.setStandardDeviation(stddev);

        NormalDistribution expected = new NormalDistribution(0.0, stddev);
        assertEquals(expected, distribution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImpl_InvalidStddev() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability_ValidInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = 0.0;
        double p = distribution.cumulativeProbability(x);

        double expected = 0.5;
        assertEquals(expected, p, 0.001);
    }

    @Test
    public void testCumulativeProbability_X_More_Than_20_StandardDeviations_From_Mean() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = 50.0;
        double p = distribution.cumulativeProbability(x);

        double expected = 0.0;
        assertEquals(expected, p, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_X_Out_Of_Domain() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = Double.NaN;
        distribution.cumulativeProbability(x);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testCumulativeProbability_X_Out_Of_Domain_M_A_X() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = Double.MAX_VALUE;
        distribution.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability_ValidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = 0.3456;
        double x = distribution.inverseCumulativeProbability(p);

        double mean = 0.0;
        double stddev = 1.0;
        NormalDistribution expected = new NormalDistribution(mean, stddev);
        double expectedX = StatUtils.percentPointNormal(100, 1.0, p);

        assertEquals(expected.getCumulativeProbability(expectedX), p, 0.001);
        assertEquals(expectedX, x, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_InvalidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = 1.1;
        distribution.inverseCumulativeProbability(p);
    }

    @Test
    public void testInverseCumulativeProbability_Probability_Zero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = 0.0;
        double x = distribution.inverseCumulativeProbability(p);

        assertEquals(Double.NEGATIVE_INFINITY, x);
    }

    @Test
    public void testInverseCumulativeProbability_Probability_One() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = 1.0;
        double x = distribution.inverseCumulativeProbability(p);

        assertEquals(Double.POSITIVE_INFINITY, x);
    }
}