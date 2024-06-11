package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        RandomGenerator rng = new Well19937c();

        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(rng, means, covariances);
            assertEquals(2, mvnd.getDimension());
            assertEquals(means, mvnd.getMeans());
            assertEquals(covariances, mvnd.getCovariances().getData());
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testDensity() {
        RandomGenerator rng = new Well19937c();

        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        try {
            mvnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Should not throw exception");
        }

        double[] testValues = {1.5, 2.5};
        double expectedResult = 0.1413718225374372;
        double result = mvnd.density(testValues);
        assertEquals(expectedResult, result, 0.00001);

        testValues = new double[]{-1.0, 1.5};
        expectedResult = 0.013222746853164065;
        result = mvnd.density(testValues);
        assertEquals(expectedResult, result, 0.00001);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();

        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        try {
            mvnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Should not throw exception");
        }

        double[] sample = mvnd.sample();
        assertEquals(2, sample.length);

        Array2DRowRealMatrix covarianceMatrix = mvnd.getCovariances();
        RealMatrix samplingMatrix = mvnd.getSamplingMatrix();

        for (int i = 0; i < sample.length; i++) {
            double expectedMean = means[i];
            double actualMean = sample[i];
            double tolerance = 0.001;
            assertTrue("Expected mean value " + expectedMean + " but got " + actualMean, Math.abs(expectedMean - actualMean) <= tolerance);
        }

        double[] normalVals = new double[sample.length];
        for (int i = 0; i < sample.length; i++) {
            normalVals[i] = rng.nextGaussian();
        }

        double[] centered = new double[sample.length];
        for (int i = 0; i < sample.length; i++) {
            centered[i] = sample[i] - means[i];
        }

        double[] preMultiplied = covarianceMatrixInverse.operate(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }

        double expectedExponentTerm = Math.exp(-0.5 * sum);
        double resultExponentTerm = mvnd.getExponentTerm(sample);
        assertEquals(expectedExponentTerm, resultExponentTerm, 0.00001);
    }
}