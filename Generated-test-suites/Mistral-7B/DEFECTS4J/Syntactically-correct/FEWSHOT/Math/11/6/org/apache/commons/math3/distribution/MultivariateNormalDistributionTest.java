package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.commons.math3.distribution.MultivariateNormalDistribution.Well19937c;
import org.apache.commons.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    @Test
    public void testDistributionCreation() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(2, dist.getDimension());
        assertEquals(means, dist.getMeans());
        assertNotNull(dist.getCovariances());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInconsistentArraysLength() {
        final double[] means = new double[]{0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMeansLengthMismatches() {
        final double[] means = new double[]{0.0, 0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        };

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testCovariancesRowsColumnsMismatch() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0},
                {0.0, 1.0}
        };

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        };

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {-1.0, 0.0},
                {0.0, -1.0}
        };

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testDistributionSample() throws Exception {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        assertArrayEquals(new double[]{2.0, 1.5}, sample);
    }

    @Test
    public void testDistributionGetStandardDeviations() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = dist.getStandardDeviations();
        assertEquals(1.0, stdDeviations[0], 0.001);
        assertEquals(1.0, stdDeviations[1], 0.001);
    }

    @Test
    public void testDistributionGetMeans() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] getsMeans = dist.getMeans();
        assertArrayEquals(means, getsMeans);
    }

    @Test
    public void testDistributionGetCovariances() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix getCovariances = dist.getCovariances();
        assertEquals(covariances, CovarianceMatrixEquals.assertMatricesEqual(getCovariances, new Array2DRowRealMatrix(covariances)));
    }

    @Test
    public void testDistributionDensity() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[]{0.5, 0.5};
        double density = dist.density(testValues);
        assertEquals(0.120466190213318, density, 0.0001);
    }

    private static class CovarianceMatrixEquals {
        public static boolean assertMatricesEqual(RealMatrix expected, RealMatrix actual) {
            if (expected.getRowDimension() != actual.getRowDimension() ||
                    expected.getColumnDimension() != actual.getColumnDimension()) {
                return false;
            }

            for (int row = 0; row < expected.getRowDimension(); row++) {
                for (int col = 0; col < expected.getColumnDimension(); col++) {
                    if (!expected.getEntry(row, col).equals(actual.getEntry(row, col))) {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}