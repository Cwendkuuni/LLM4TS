package org.apache.commons.math3.distribution;

import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        // Test case for getting mean vector
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[3][3]);
        assertArrayEquals(means, distribution.getMeans());
    }

    @Test
    public void testGetCovariances() {
        // Test case for getting covariance matrix
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(covariances, distribution.getCovariances().getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatchException() {
        // Test case for DimensionMismatchException on density calculation
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 2.0}; // Dimension mismatch
        distribution.density(values);
    }

    // Add more test cases for other methods as per requirements and edge cases

}