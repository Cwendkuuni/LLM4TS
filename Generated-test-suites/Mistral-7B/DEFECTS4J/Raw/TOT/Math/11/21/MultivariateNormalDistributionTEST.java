package org.apache.commons.math3.distribution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpectralFactorization;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private static final int DIM = 3;
    private static final double[] MEANS = new double[]{1.0, 2.0, 3.0};
    private static final double[][] COVARIANCES = new double[][]{{1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
    private static final double[] STANDARD_DEVIATIONS = new double[]{1.0, 1.0, 1.0};

    private MultivariateNormalDistribution distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        try {
            distribution = new MultivariateNormalDistribution(rng, MEANS, COVARIANCES);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Failed to initialize distribution");
        }
    }

    @After
    public void tearDown() {
        distribution = null;
        rng = null;
    }

    @Test
    public void testMeans() {
        final double[] means = distribution.getMeans();
        assertArrayEquals(MEANS, means);
    }

    @Test
    public void testCovariances() {
        final RealMatrix covariances = distribution.getCovariances();
        RealMatrix expected = new Array2DRowRealMatrix(COVARIANCES);
        assertMatrixEquals(expected, covariances);
    }

    @Test
    public void testGetStandardDeviations() {
        final double[] std = distribution.getStandardDeviations();
        assertArrayEquals(STANDARD_DEVIATIONS, std);
    }

    @Test
    public void testDensitySinglePoint() {
        final double[] point = new double[]{0.0, 0.0, 0.0};
        final double density = distribution.density(point);
        // According to formula, cannot be computed directly.
        // Instead, we test that the method returns a positive value.
        assertTrue(density > 0);
    }

    @Test
    public void testDensityMultiplePoints() {
        final int sampleSize = 10000;
        final double[][] points = new double[sampleSize][DIM];
        for (int i = 0; i < sampleSize; i++) {
            for (int j = 0; j < DIM; j++) {
                points[i][j] = rng.nextGaussian();
            }
        }

        final double[] densities = new double[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            densities[i] = distribution.density(points[i]);
        }

        final double expectedMean = StatUtils.mean(densities);
        final double expectedStdDev = StatUtils.stdev(densities);

        // According to the distribution, expected mean is 1 and stddev is sqrt(det(CovarianceMatrix))
        assertEquals(1.0, Precision.round(expectedMean, 12), 1e-12);
        assertEquals(Math.pow(Math.PI * Math.pow(2, DIM / 2), -0.5) * Math.pow(distribution.getCovariances().determinant(), -0.5),
                Precision.round(expectedStdDev, 12), 1e-12);
    }

    @Test
    public void testSampleSinglePoint() {
        final double[] sample = distribution.sample();
        assertArrayEquals(MEANS, sample, 1e-12);
    }

    @Test
    public void testSampleMultiple() {
        final int sampleSize = 1000;
        final double[][] samples = new double[sampleSize][DIM];
        for (int i = 0; i < sampleSize; i++) {
            samples[i] = distribution.sample();
        }

        final RealMatrix covMat = distribution.getCovariances();
        final RealArray2DSpectralFactorization svd = covMat.decompose(RealMatrix.Decomposition.SVDS);
        final RealMatrix eigenVectors = svd.getV();
        final double[] eigenValues = svd.getSingularValues();

        for (int i = 0; i < sampleSize; i++) {
            final double[] sample = samples[i];
            final double[] deviations = new double[DIM];
            for (int j = 0; j < DIM; j++) {
                deviations[j] = sample[j] - MEANS[j];
            }
            final double[] transformed = eigenVectors.operate(deviations);
            for (int j = 0; j < DIM; j++) {
                final double std = STANDARD_DEVIATIONS[j];
                final double expectedValue = rng.nextGaussian() * std;
                assertArrayEquals(new double[]{expectedValue}, transformed, 1e-12);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSampleInvalidSampleSize() {
        final int sampleSize = -1;
        distribution.sample(sampleSize);
    }

}