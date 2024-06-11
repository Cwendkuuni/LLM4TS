package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test cases for {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test creation of F distribution given valid degrees of freedom.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testFDistributionCreation() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        assertNotNull(fDist);
        assertEquals(2.5, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test creation of F distribution with invalid degrees of freedom.
     * @throws Exception if an error occurs during test.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationInvalidDegreesOfFreedom() throws Exception {
        new FDistribution(-1.0, 2.5);
    }

    /**
     * Test calculation of density for F distribution.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testFDensity() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double[] expectedDensities = new double[]{0.35375971, 0.36877152, 0.31558331, 0.21961237, 0.11857344};
        double x = 0.5;
        for (int i = 0; i < expectedDensities.length; i++) {
            double density = fDist.density(x + i / 10.0);
            assertEquals(expectedDensities[i], density, 1e-6);
        }
    }

    /**
     * Test calculation of cumulative probability for F distribution.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testFCumulativeProbability() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double[] expectedProbabilities = new double[]{0.0, 0.05357863, 0.15273686, 0.31243924, 0.48484243, 0.67139752, 0.85630994, 0.97283645};
        for (int i = 0; i < expectedProbabilities.length; i++) {
            double probability = fDist.cumulativeProbability(i / 10.0);
            assertEquals(expectedProbabilities[i], probability, 1e-6);
        }
    }

    /**
     * Test calculation of mean for F distribution.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testFMean() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double expectedMean = 1.5;
        double mean = fDist.getNumericalMean();
        assertEquals(expectedMean, mean, 1e-6);
    }

    /**
     * Test calculation of variance for F distribution.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testFVariance() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double expectedVariance = 2.85;
        double variance = fDist.getNumericalVariance();
        assertEquals(expectedVariance, variance, 1e-6);
    }

    /**
     * Test calculation of inverse cumulative probability for F distribution.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testFInverseCumulativeProbability() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double[] expectedValues = new double[]{1.22514941, 1.55234735, 2.03132207, 3.07654551, 5.22426897, 7.42931358, 10.29535351, 15.11815327};
        for (int i = 0; i < expectedValues.length; i++) {
            double p = i / 10.0;
            double inverseCdf = fDist.inverseCumulativeProbability(p);
            assertEquals(expectedValues[i], inverseCdf, 1e-6);
        }
    }

    /**
     * Test generation of sample values for F distribution.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testFSample() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        RandomGenerator rng = new Well19937c();
        FDistribution fDistGen = new FDistribution(rng, 2.5, 3.0);
        double[] expectedValues = new double[]{0.86872697, 1.35281414, 2.09831693, 2.74033761, 3.58334434, 4.86003870, 6.30384226, 8.29268974};
        for (int i = 0; i < expectedValues.length; i++) {
            double sample = fDistGen.sample();
            assertEquals(expectedValues[i], sample, 1e-6);
        }
    }

    /**
     * Test calculation of gamma function.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testGammaFunction() throws Exception {
        double[] xValues = new double[]{0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
        double[] expectedGammaValues = new double[]{0.88622692, 0.54030230, 1.64872127, 1.72933518, 2.67895610, 3.62883189, 362880.0};
        for (int i = 0; i < xValues.length; i++) {
            double gamma = Gamma.gamma(xValues[i]);
            assertEquals(expectedGammaValues[i], gamma, 1e-6);
        }
    }

    /**
     * Test calculation of beta function.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testBetaFunction() throws Exception {
        double[] xValues = new double[]{0.5, 1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = new double[]{0.5, 0.5, 1.0, 1.77545666, 3.04344898, 5.38935445};
        for (int i = 0; i < xValues.length; i++) {
            double beta = Beta.beta(xValues[i], yValues[i]);
            assertEquals(yValues[i], beta, 1e-6);
        }
    }

    /**
     * Test calculation of inverse beta function.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testInverseBetaFunction() throws Exception {
        double[] pValues = new double[]{0.1, 0.5, 0.9};
        double[] expectedValues = new double[]{2.52978949, 2.0, 1.11925323};
        for (int i = 0; i < pValues.length; i++) {
            double inverseBeta = Beta.regularizedBeta(pValues[i], 1.0, 1.0);
            assertEquals(expectedValues[i], inverseBeta, 1e-6);
        }
    }

    /**
     * Test calculation of inverse F distribution.
     * @throws Exception if an error occurs during test.
     */
    @Test
    public void testInverseFDistribution() throws Exception {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double[] pValues = new double[]{0.05, 0.5, 0.95};
        double[] expectedValues = new double[]{1.43882835, 1.5, 2.67430257};
        for (int i = 0; i < pValues.length; i++) {
            double inverseCdf = fDist.inverseCumulativeProbability(pValues[i]);
            assertEquals(expectedValues[i], inverseCdf, 1e-6);
        }
    }
}