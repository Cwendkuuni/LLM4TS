package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            MultivariateNormalDistribution mvnDistribution = new MultivariateNormalDistribution(means, covariances);
            assertEquals(means.length, mvnDistribution.getDimension());
            assertEquals(means, mvnDistribution.getMeans());
            RealMatrix covarianceMatrix = mvnDistribution.getCovariances();
            assertEquals(covariances, covarianceMatrix.getData());
        } catch (NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnDistribution = null;
        try {
            mvnDistribution = new MultivariateNormalDistribution(means, covariances);
        } catch (NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            fail("Exception should not be thrown.");
        }

        double[] testValues = {1.0, 1.0};
        double density = mvnDistribution.density(testValues);

        double expectedDensity = 0.039894228040143275;
        assertEquals(expectedDensity, density, 0.0001);
    }

    @Test
    public void testSample() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnDistribution = null;
        try {
            mvnDistribution = new MultivariateNormalDistribution(means, covariances);
        } catch (NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            fail("Exception should not be thrown.");
        }

        double[] sample = mvnDistribution.sample();
        assertEquals(sample.length, 2);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnDistribution = null;
        try {
            mvnDistribution = new MultivariateNormalDistribution(means, covariances);
        } catch (NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            fail("Exception should not be thrown.");
        }

        double[] standardDeviations = mvnDistribution.getStandardDeviations();
        assertEquals(standardDeviations.length, 2);
        assertEquals(Math.sqrt(1.0), standardDeviations[0], 0.0001);
        assertEquals(Math.sqrt(1.0), standardDeviations[1], 0.0001);
    }

    @Test
    public void testDensityNegativeCovarianceMatrix() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};

        try {
            MultivariateNormalDistribution mvnDistribution = new MultivariateNormalDistribution(means, covariances);
            fail("Exception should be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testDensityIncorrectDimensions() {
        double[] means = {0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            MultivariateNormalDistribution mvnDistribution = new MultivariateNormalDistribution(means, covariances);
            fail("Exception should be thrown.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testSampleWithIncorrectDimensions() {
        double[] means = {0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            MultivariateNormalDistribution mvnDistribution = new MultivariateNormalDistribution(means, covariances);
            fail("Exception should be thrown.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }
}