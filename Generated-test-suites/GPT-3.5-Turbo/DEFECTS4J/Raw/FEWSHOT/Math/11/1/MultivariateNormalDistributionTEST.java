package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;


public class MultivariateNormalDistributionTest {

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMeansLength() {
        // Invalid means array length
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovarianceMatrixLength() {
        // Invalid covariance matrix dimensions
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testInvalidCovarianceMatrix() {
        // Invalid covariance matrix (singular)
        double[] means = {1, 2};
        double[][] covariances = {{1, 1}, {1, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeEigenvalue() {
        // Negative eigenvalue
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, -1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testValidDistribution() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        assertNotNull(mnd);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(means, mnd.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        RealMatrix expectedCovarianceMatrix = MatrixUtils.createRealMatrix(covariances);
        assertEquals(expectedCovarianceMatrix, mnd.getCovariances());
    }

    @Test
    public void testDensity() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] values = {0, 0};
        assertEquals(0.15915494309189535, mnd.density(values), 0.000001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] expectedStandardDeviations = {1, 1, 1};
        assertArrayEquals(expectedStandardDeviations, mnd.getStandardDeviations(), 0.0);
    }

    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] sample = mnd.sample();

        assertNotNull(sample);
        assertEquals(means.length, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] values = {0, 0};
        assertEquals(1.0, mnd.getExponentTerm(values), 0.000001);
    }

}