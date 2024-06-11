package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.stat.descript.moment.Mean;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.apache.commons.math3.stat.prob.DistributionProbability;
import org.junit.Test;

/**
 * Testing {@link PoissonDistributionImpl} class.
 */
public class PoissonDistributionImplTest {

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double)} constructor.
     */
    @Test
    public void testPoissonDistributionImplConstructor() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, pd.getMean(), 0.0);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#probability(int)} method.
     */
    @Test
    public void testProbability() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double probability = pd.probability(i);
            double expectedProbability = Gamma.regularizedGammaP(i + 1, 1.0);
            assertEquals(expectedProbability, probability, 1e-8);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#cumulativeProbability(int)} method.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = pd.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, 1.0);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-8);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#normalApproximateProbability(int)} method.
     */
    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        NormalDistributionImpl normalDist = new NormalDistributionImpl(1.0, 1.0);
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = pd.normalApproximateProbability(i);
            double expectedNormalApproximateProbability = 1.0 - normalDist.cumulativeProbability(i - 0.5);
            assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 1e-8);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#sample()} method.
     */
    @Test
    public void testSample() throws Exception {
        PoissonDistribution pd = new PoissonDistributionImpl(3.5);
        int sample = pd.sample();
        int expectedSample = (int) Math.round(3.5 * Math.random());
        assertEquals(expectedSample, sample);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#getMean()} method.
     */
    @Test
    public void testGetMean() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double mean = pd.getMean();
        assertEquals(1.0, mean, 0.0);
    }

    /**
     * Test method for distribution properties.
     */
    @Test
    public void testDistributionProperties() throws Exception {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);

        double mean = new Mean().value(pd);
        assertEquals(1.0, mean, 0.0);

        double stdDev = new StandardDeviation().value(pd);
        assertEquals(1.0, stdDev, 0.0);

        DistributionProbability probability = new DistributionProbability();
        double[] probabilities = probability.probabilityDensityFunction(pd, 10000);
        double sum = 0;
        for (double p : probabilities) {
            sum += p;
        }
        assertEquals(1.0, sum, 1e-8);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)} constructor.
     */
    @Test
    public void testPoissonDistributionImplConstructorWithParameters() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0, 1e-12, 1000000);
        assertEquals(1.0, pd.getMean(), 0.0);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double)} constructor.
     */
    @Test
    public void testPoissonDistributionImplConstructorWithEpsilon() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0, 1e-12);
        assertEquals(1.0, pd.getMean(), 0.0);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, int)} constructor.
     */
    @Test
    public void testPoissonDistributionImplConstructorWithMaxIterations() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0, 1000000);
        assertEquals(1.0, pd.getMean(), 0.0);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)} constructor with invalid mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionImplConstructorWithInvalidMean() {
        new PoissonDistributionImpl(-1.0, 1e-12, 1000000);
    }
}