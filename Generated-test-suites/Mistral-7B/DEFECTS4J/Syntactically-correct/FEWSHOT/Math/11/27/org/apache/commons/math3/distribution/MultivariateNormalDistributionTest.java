package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealArray2DRowRelation;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.statistics.DescriptiveStatistics;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        assertEquals(2, mvn.getDimension());
        assertArrayEquals(means, mvn.getMeans());
        assertArrayEquals(covariances, mvn.getCovariances().getData());

        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 1000; i++) {
            double[] samples = mvn.sample();
            assertEquals(2, samples.length);
            assertEquals(means[0], samples[0], 1e-6);
            assertEquals(means[1], samples[1], 1e-6);
        }

        RealMatrix covariance = mvn.getCovariances();
        EigenDecomposition eig = new EigenDecomposition(covariance);
        RealArray2RowRelation eigenvectors = eig.getV();
        RealVector eigenvalues = eig.getEigenvalues();
        assertEquals(2.0, eigenvalues.getEntry(0), 1e-6);
        assertEquals(2.0, eigenvalues.getEntry(1), 1e-6);

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 0; i < 1000; i++) {
            double[] sample = mvn.sample();
            stats.addValue(sample[0]);
            stats.addValue(sample[1]);
        }
        assertEquals(means[0], stats.getMean(), 1e-6);
        assertEquals(means[1], stats.getMean(), 1e-6);
        assertEquals(1.0, stats.getStandardDeviation(), 1e-3);
        assertEquals(1.0, stats.getStandardDeviation(), 1e-3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMeanLength() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesRows() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 0.5}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesColumns() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 0.5}, {0.5, 0.5}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteCovariance() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovariance() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{0.0, 0.5}, {0.5, 0.0}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }
}