package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Checks if the constructor with mean vector and covariance matrix works correctly.
     */
    @Test
    public void testConstructorWithMeanAndCovarianceMatrix() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.1}, {0.1, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, dist.getMeans(), 0.001);
        RealMatrix covariance = dist.getCovariances();
        Assert.assertArrayEquals(covariances[0], covariance.getRowVector(0), 0.001);
        Assert.assertArrayEquals(covariances[1], covariance.getRowVector(1), 0.001);
    }

    /**
     * Checks if the density method works correctly for some given values.
     */
    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.1}, {0.1, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] testValues = {1.0, 1.0};
        double density = dist.density(testValues);
        double expectedDensity = FastMath.exp(-1.0) * FastMath.sqrt(2.0 * FastMath.PI) / 2.0;
        Assert.assertEquals(expectedDensity, density, 0.001);

        testValues = new double[]{-1.0, -1.0};
        density = dist.density(testValues);
        expectedDensity = FastMath.exp(-3.0) * FastMath.sqrt(2.0 * FastMath.PI) / 2.0;
        Assert.assertEquals(expectedDensity, density, 0.001);
    }

    /**
     * Checks if the sample method works correctly and generates samples from the distribution.
     */
    @Test
    public void testSample() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.1}, {0.1, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] sampleValues = dist.sample();
        double[] expectedMeans = dist.getMeans();
        double[] expectedCovariance = dist.getCovariances().getData()[0];

        for (int i = 0; i < sampleValues.length; i++) {
            Assert.assertTrue(sampleValues[i] > expectedMeans[i] - 0.5 && sampleValues[i] < expectedMeans[i] + 0.5);
        }

        RandomGenerator generatingDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(10, sampleValues.length);
        for (int i = 0; i < 10; i++) {
            RealVector sample = generatingDist.sample();
            for (int j = 0; j < sampleValues.length; j++) {
                covarianceMatrix.setEntry(i, j, covarianceMatrix.getEntry(i, j) + (sample.getEntry(j) - expectedMeans[j]) * (sample.getEntry(j) - expectedMeans[j]));
            }
        }
        RealMatrix calculatedCovariance = covarianceMatrix.transpose().multiply(covarianceMatrix).divide(10.0);
        Covariance cov = new Covariance();
        RealMatrix expectedCovarianceMatrix = cov.covariance(dist.sample(1000));
        RealMatrix difference = calculatedCovariance.subtract(expectedCovarianceMatrix);
        double maxDifference = 0;
        for (int i = 0; i < difference.getRowDimension(); i++) {
            for (int j = 0; j < difference.getColumnDimension(); j++) {
                double d = difference.getEntry(i, j);
                if (Math.abs(d) > maxDifference) {
                    maxDifference = Math.abs(d);
                }
            }
        }
        Assert.assertEquals(0.0, maxDifference, 0.01);
    }

    /**
     * Checks if the constructor with mean vector and covariance matrix throws an exception when the arrays have
     * different lengths.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMeanAndCovarianceMatrixDifferentLengths() {
        double[] means = {0.0};
        double[][] covariances = {{1.0}, {1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Checks if the constructor with mean vector and covariance matrix throws an exception when the covariance
     * matrix is not square.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMeanAndCovarianceMatrixNotSquare() {
        double[] means = {0.0};
        double[][] covariances = {{1.0}, {1.0, 1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Checks if the constructor with mean vector and covariance matrix throws an exception when the covariance
     * matrix is not positive definite.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithMeanAndCovarianceMatrixNotPositiveDefinite() {
        double[] means = {0.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Checks if the constructor with mean vector and covariance matrix throws an exception when the eigenvalue
     * decomposition cannot be performed on the provided covariance matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithMeanAndCovarianceMatrixSingular() {
        double[] means = {0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }
}