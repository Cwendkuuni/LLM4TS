package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    
    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException
                | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
        double[] actualMeans = distribution.getMeans();
        assertTrue(MathArrays.equals(means, actualMeans));
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException
                | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        RealMatrix actualCovariances = distribution.getCovariances();
        assertTrue(expectedCovariances.equals(actualCovariances));
    }
    
    @Test
    public void testDensity() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException
                | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
        double[] values = {1, 1, 1};
        double expectedDensity = 0.06349363593424098;
        try {
            double actualDensity = distribution.density(values);
            assertEquals(expectedDensity, actualDensity, 1e-10);
        } catch (DimensionMismatchException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException
                | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
        double[] sample = distribution.sample();
        assertEquals(means.length, sample.length);
    }
}