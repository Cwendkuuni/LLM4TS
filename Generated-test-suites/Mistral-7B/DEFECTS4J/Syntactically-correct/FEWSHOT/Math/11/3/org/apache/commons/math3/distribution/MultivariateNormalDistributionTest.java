package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution.CovarianceType;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstruction() {
        double[] means = new double[]{0.0, 1.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution mvnd1 = new MultivariateNormalDistribution(means, covariances);

        assertEquals(means.length, mvnd1.getDimension());
        assertArrayEquals(means, mvnd1.getMeans());
        assertEquals(covariances.length, mvnd1.getCovariances().getRowDimension());
        assertEquals(covariances[0].length, mvnd1.getCovariances().getColumnDimension());
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = new double[]{0.0, 1.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 0.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = new double[]{0.0, 1.0};
        double[][] covariances = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[]{0.5, 1.5};

        double density = mvnd.density(testValues);
        assertEquals(1.0, density, 1e-15);

        testValues = new double[]{3.0, 2.0};
        density = mvnd.density(testValues);
        assertEquals(0.13533528323060103, density, 1e-15);
    }

    @Test
    public void testSample() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        double[] sample = mvnd.sample();

        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertTrue(Double.isFinite(sample[i]));
        }

        double[] testValues = new double[]{0.5, 1.5};
        double density = mvnd.density(testValues);
        double[] generatedSample = mvnd.sample();

        for (int i = 0; i < sample.length; i++) {
            assertEquals(testValues[i], generatedSample[i], 1e-15);
        }

        assertEquals(density, mvnd.density(generatedSample), 1e-15);
    }

    @Test
    public void testGetStandardDeviations() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = mvnd.getStandardDeviations();

        assertEquals(standardDeviations.length, mvnd.getDimension());
        for (int i = 0; i < standardDeviations.length; i++) {
            assertEquals(Math.sqrt(covariances[i][i]), standardDeviations[i], 1e-15);
        }
    }

    @Test
    public void testSampleWithDifferentCovarianceType() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances, CovarianceType.COVARIANCE);
        double[] sample1 = mvnd.sample();

        MultivariateNormalDistribution mvnd2 = new MultivariateNormalDistribution(means, covariances, CovarianceType.CORRELATION);
        double[] sample2 = mvnd2.sample();

        for (int i = 0; i < sample1.length; i++) {
            assertEquals(sample1[i], sample2[i], 1e-15);
        }
    }

    @Test
    public void testSampleMatrix() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        final int sampleSize = 10;

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        RandomGenerator rng = mvnd.getRandomGenerator();
        RealMatrix sampleMatrix = new Array2DRowRealMatrix(sampleSize, mvnd.getDimension());

        for (int i = 0; i < sampleSize; i++) {
            sampleMatrix.setRow(i, mvnd.sample());
        }

        RealMatrix generatedSampleMatrix = mvnd.sample(sampleSize, rng);

        for (int i = 0; i < sampleSize; i++) {
            for (int j = 0; j < mvnd.getDimension(); j++) {
                assertEquals(sampleMatrix.getEntry(i, j), generatedSampleMatrix.getEntry(i, j), 1e-15);
            }
        }
    }
}