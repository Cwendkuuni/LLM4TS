package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(2, covarianceMatrix.getRowDimension());
        assertEquals(2, covarianceMatrix.getColumnDimension());
        assertEquals(1.0, covarianceMatrix.getEntry(0, 0), 1e-10);
        assertEquals(0.5, covarianceMatrix.getEntry(0, 1), 1e-10);
        assertEquals(0.5, covarianceMatrix.getEntry(1, 0), 1e-10);
        assertEquals(2.5, covarianceMatrix.getEntry(1, 1), 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMeansLength() {
        double[] means = {1.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesRows() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5},
                {1.0, 2.5}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesColumns() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5},
                {1.0, 2.5, 3.0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[][]{});
        double[] result = dist.getMeans();
        assertArrayEquals(means, result);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix result = dist.getCovariances();
        assertEquals(covariances, result.getData());
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {2.0, 3.0};
        double expected = FastMath.pow(2 * FastMath.PI, -2 / 2) *
                FastMath.pow(dist.getCovariances().getDeterminant(), -0.5) *
                dist.getExponentTerm(testValues);
        double result = dist.density(testValues);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        assertArrayEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertEquals(Double.class, sample[i].getClass());
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] result = dist.getStandardDeviations();
        assertEquals(2, result.length);
        assertEquals(1.0, result[0], 1e-10);
        assertEquals(1.5811388300841898, result[1], 1e-10);
    }
}