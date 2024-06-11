import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplConstruction() {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean(), 0.001);

        mean = 0.5;
        poissonDistribution = new PoissonDistributionImpl(mean, 0.001, 1000000);
        assertEquals(mean, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplNegativeMean() {
        double mean = -2.0;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability() {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double probability = poissonDistribution.probability(i);
            double expectedProbability = Gamma.regularizedGammaP(i + 1, mean);
            assertEquals(expectedProbability, probability, 0.0001);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, mean);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double expectedProbability = NormalDistributionImpl.getDefaultInstance().cumulativeProbability(i - mean + 0.5);
            assertEquals(expectedProbability, normalApproximateProbability, 0.0001);
        }
    }

    @Test
    public void testSample() throws MathException {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i < 10; i++) {
            int sample = poissonDistribution.sample();
            assertEquals(i, sample);
        }

        int sample = poissonDistribution.sample();
        int maxValue = (int) (1000 * mean);
        assertTrue(sample <= maxValue);
    }

    @Test
    public void testGetDomainLowerBound() {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(0, poissonDistribution.getDomainLowerBound(0.0));
    }

    @Test
    public void testGetDomainUpperBound() {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            int actual = poissonDistribution.inverseCumulativeProbability(p);
            int expected = (int) Math.round(Gamma.regularizedGammaQInv(p, mean, false, 1000000));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testSampleDistribution() throws MathException {
        double mean = 3.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        int[] samples = new int[1000];
        for (int i = 0; i < 1000; i++) {
            samples[i] = poissonDistribution.sample();
        }

        double expectedMean = mean;
        double expectedVariance = mean;
        for (int sample : samples) {
            expectedMean += sample;
            expectedVariance += Math.pow(sample, 2);
        }
        expectedMean /= 1000.0;
        expectedVariance /= 1000.0;

        double actualMean = Precision.round(poissonDistribution.getMean(), 4);
        assertEquals(expectedMean, actualMean, 0.0001);

        double actualVariance = poissonDistribution.getVariance();
        assertEquals(expectedVariance, actualVariance, 0.0001);
    }

    @Test
    public void testSampleDistributionWithLargeMean() throws MathException {
        double mean = 1000.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean, 0.01);

        int[] samples = new int[100];
        for (int i = 0; i < 100; i++) {
            samples[i] = poissonDistribution.sample();
        }

        double expectedMean = mean;
        double expectedVariance = mean;
        for (int sample : samples) {
            expectedMean += sample;
            expectedVariance += Math.pow(sample, 2);
        }
        expectedMean /= 100.0;
        expectedVariance /= 100.0;

        double actualMean = Precision.round(poissonDistribution.getMean(), 4);
        assertEquals(expectedMean, actualMean, 0.0001);

        double actualVariance = poissonDistribution.getVariance();
        assertEquals(expectedVariance, actualVariance, 0.0001);
    }
}