package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.StatUtils;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionCreation() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        assertNotNull(mvnd.getCovarianceMatrix());
        assertEquals(2, mvnd.getDimension());
        assertArrayEquals(means, mvnd.getMeans());
        assertArrayEquals(covariances, mvnd.getCovariances().getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionCreationWithInvalidMeans() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        final double[] invalidMeans = new double[]{1.0, 0.0};
        MultivariateNormalDistribution mvndWithInvalidMeans = new MultivariateNormalDistribution(invalidMeans, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionCreationWithInvalidCovariances() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        final double[][] invalidCovariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mvndWithInvalidCovariances = new MultivariateNormalDistribution(means, invalidCovariances);
    }

    @Test
    public void testMultivariateNormalDistributionGetStandardDeviations() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] stdDeviations = mvnd.getStandardDeviations();
        assertEquals(2.0, stdDeviations[0], 0.001);
        assertEquals(0.70710678118655, stdDeviations[1], 0.001);
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        final int sampleSize = 10000;

        double[][] samples = new double[sampleSize][];
        for (int i = 0; i < sampleSize; i++) {
            samples[i] = mvnd.sample();
        }

        RealMatrix covariancesMatrix = new Array2DRowRealMatrix(covariances);
        RealMatrix samplesMatrix = new Array2DRowRealMatrix(samples);

        RealMatrix expectedCovariances = covariancesMatrix.multiply(MatrixUtils.TRANSPOSE.multiply(covariancesMatrix));
        RealMatrix actualCovariances = samplesMatrix.covariance();

        assertEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testMultivariateNormalDistributionDensity() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        final double epsilon = 1e-6;

        double[] values1 = new double[]{0.0, 0.0};
        double density1 = mvnd.density(values1);
        assertEquals(1.0 / Math.sqrt(2 * Math.PI * 2.0), density1, epsilon);

        double[] values2 = new double[]{1.0, 1.0};
        double density2 = mvnd.density(values2);
        assertEquals(0.13595459511619, density2, epsilon);

        double[] values3 = new double[]{-1.0, -1.0};
        double density3 = mvnd.density(values3);
        assertEquals(0.13595459511619, density3, epsilon);
    }

    @Test
    public void testMultivariateNormalDistributionDensityWithInvalidValues() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] invalidValues = new double[]{3.0, 3.0};
        try {
            mvnd.density(invalidValues);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }
}