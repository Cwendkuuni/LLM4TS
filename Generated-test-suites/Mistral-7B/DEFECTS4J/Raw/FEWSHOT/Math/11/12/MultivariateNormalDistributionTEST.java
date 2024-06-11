package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}};

        try {
            MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            assertEquals(means.length, mvnDist.getDimension());
            assertEquals(means, mvnDist.getMeans());
            RealMatrix covMatrix = mvnDist.getCovariances();
            assertTrue(covMatrix.isSymmetric());
            assertEquals(covariances, covMatrix.getData());
            assertEquals(1.0 / Math.sqrt(2.0 * Math.PI), mvnDist.density(new double[]{0.0, 0.0}), 0.001);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}};
        MultivariateNormalDistribution mvnDist = null;
        try {
            mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] testValues = {1.5, 2.5, 3.5};
        double expectedDensity = 1.0 / (Math.sqrt(2.0 * Math.PI) * Math.sqrt(3.0)) * Math.exp(-0.5 * (Math.pow(testValues[0] - means[0], 2.0) / 3.0 + Math.pow(testValues[1] - means[1], 2.0) / 4.0 + Math.pow(testValues[2] - means[2], 2.0) / 9.0));
        assertEquals(expectedDensity, mvnDist.density(testValues), 0.001);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}};
        MultivariateNormalDistribution mvnDist = null;
        try {
            mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] sampleValues = mvnDist.sample();
        for (int i = 0; i < sampleValues.length; i++) {
            assertTrue(sampleValues[i] > -1000.0 && sampleValues[i] < 1000.0);
        }
        assertEquals(means.length, sampleValues.length);
    }

    @Test
    public void testStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}};
        MultivariateNormalDistribution mvnDist = null;
        try {
            mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] standardDeviations = mvnDist.getStandardDeviations();
        assertEquals(3.0, standardDeviations[0], 0.001);
        assertEquals(Math.sqrt(2.0), standardDeviations[1], 0.001);
        assertEquals(Math.sqrt(3.0), standardDeviations[2], 0.001);
    }
}