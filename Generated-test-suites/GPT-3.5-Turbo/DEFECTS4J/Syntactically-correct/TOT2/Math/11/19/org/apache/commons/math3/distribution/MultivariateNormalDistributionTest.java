package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        // Test case for getting the mean vector
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        assertArrayEquals(means, distribution.getMeans(), 0.0001);
    }

    @Test
    public void testGetCovariances() {
        // Test case for getting the covariance matrix
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2, 1}, {1, 2}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(covariances, distribution.getCovariances().getData(), 0.0001);
    }

    @Test
    public void testDensity() {
        // Test case for calculating the density at given values
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2, 1}, {1, 2}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double density = distribution.density(new double[]{1.0, 2.0});
        assertEquals(0.07957747154594767, density, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        // Test case for getting the standard deviations of the distribution
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2, 1}, {1, 2}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = distribution.getStandardDeviations();
        double[] expectedStdDeviations = {1.4142135623730951, 1.4142135623730951};
        assertArrayEquals(expectedStdDeviations, stdDeviations, 0.0001);
    }

    @Test
    public void testSample() {
        // Test case for sampling values from the distribution
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2, 1}, {1, 2}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(2, sample.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testWrongDimensionDensity() {
        // Test case for calculating density with wrong dimension values
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2, 1}, {1, 2}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        distribution.density(new double[]{1.0}); // This should throw DimensionMismatchException
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeEigenvalue() {
        // Test case for providing a covariance matrix with negative eigenvalue
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1, 0}, {0, -1}}; // This matrix has a negative eigenvalue
        new MultivariateNormalDistribution(means, covariances); // This should throw NonPositiveDefiniteMatrixException
    }

}