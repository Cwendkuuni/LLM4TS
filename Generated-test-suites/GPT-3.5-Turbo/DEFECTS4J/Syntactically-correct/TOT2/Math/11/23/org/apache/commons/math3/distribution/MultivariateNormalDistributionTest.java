package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class MultivariateNormalDistributionTest {
    private double[] means = {0.0, 0.0};
    private double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
    private RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);

    @Test
    public void testDensity() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {0.5, 0.5};
        double density = distribution.density(values);
        assertEquals(0.0585498, density, 1e-5);
    }

    @Test
    public void testGetMeans() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.getMeans();
        assertTrue(MathArrays.equals(means, result, 1e-15));
    }

    @Test
    public void testGetCovariances() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        RealMatrix result = distribution.getCovariances();
        assertTrue(covarianceMatrix.subtract(result).getNorm() < 1e-15);
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] std = distribution.getStandardDeviations();
        double[] expectedStd = {1.0, 1.0};
        assertTrue(MathArrays.equals(expectedStd, std, 1e-15));
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertEquals(2, sample.length);
    }

    @Test
    public void testInvalidDensityDimension() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {0.5};
        assertThrows(DimensionMismatchException.class, () -> distribution.density(values));
    }

    @Test
    public void testNegativeEigenvalue() {
        double[][] covariancesWithNegativeEigenvalue = {{1.0, -0.5}, {-0.5, 1.0}};
        assertThrows(NonPositiveDefiniteMatrixException.class, () ->
                new MultivariateNormalDistribution(means, covariancesWithNegativeEigenvalue));
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        double[][] nonPositiveDefiniteMatrix = {{1.0, 2.0}, {2.0, 4.0}};
        assertThrows(NonPositiveDefiniteMatrixException.class, () ->
                new MultivariateNormalDistribution(means, nonPositiveDefiniteMatrix));
    }
}