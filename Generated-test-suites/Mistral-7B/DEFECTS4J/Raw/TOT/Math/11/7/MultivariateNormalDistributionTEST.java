package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-12;
    private static final double[] MEANS = {0.0, 1.0};
    private static final double[] COVARIANCES_1 = {1.0, 1.0};
    private static final double[] COVARIANCES_2 = {1.0, 1.0, 1.0, 1.0};
    private static final double[][] COVARIANCES_3 = {{1.0, 0.0}, {0.0, 1.0}};
    private MultivariateNormalDistribution distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
    }

    /**
     * Test constructor with valid input data.
     */
    @Test
    public void testValidInput() {
        try {
            distribution = new MultivariateNormalDistribution(MEANS, COVARIANCES_1);
            Assert.assertArrayEquals(MEANS, distribution.getMeans());
            Assert.assertEquals(1, distribution.getDimension());
            RealMatrix covarianceMatrix = distribution.getCovariances();
            Assert.assertEquals(1, covarianceMatrix.getRowDimension());
            Assert.assertEquals(1, covarianceMatrix.getColumnDimension());
            Assert.assertArrayEquals(COVARIANCES_1, covarianceMatrix.getData()[0]);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Failed to instantiate distribution with valid input.");
        }

        try {
            distribution = new MultivariateNormalDistribution(rng, MEANS, COVARIANCES_2);
            Assert.assertArrayEquals(MEANS, distribution.getMeans());
            Assert.assertEquals(2, distribution.getDimension());
            RealMatrix covarianceMatrix = distribution.getCovariances();
            Assert.assertEquals(2, covarianceMatrix.getRowDimension());
            Assert.assertEquals(2, covarianceMatrix.getColumnDimension());
            Assert.assertArrayEquals(COVARIANCES_2, covarianceMatrix.getData(), EPSILON);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Failed to instantiate distribution with valid input.");
        }

        try {
            distribution = new MultivariateNormalDistribution(rng, MEANS, COVARIANCES_3);
            Assert.assertArrayEquals(MEANS, distribution.getMeans());
            Assert.assertEquals(2, distribution.getDimension());
            RealMatrix covarianceMatrix = distribution.getCovariances();
            Assert.assertEquals(2, covarianceMatrix.getRowDimension());
            Assert.assertEquals(2, covarianceMatrix.getColumnDimension());
            RealVector eigenvectors = covarianceMatrix.getEigenvectors();
            Assert.assertEquals(2, eigenvectors.getDimension());
            Assert.assertTrue(Precision.isClose(1.0, eigenvectors.getEntry(0, 0), 1e-12));
            Assert.assertTrue(Precision.isClose(1.0, eigenvectors.getEntry(1, 1), 1e-12));
            RealVector eigenvalues = covarianceMatrix.getEigenvalues();
            Assert.assertTrue(Precision.isClose(1.0, eigenvalues.getEntry(0), 1e-12));
            Assert.assertTrue(Precision.isClose(1.0, eigenvalues.getEntry(1), 1e-12));
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Failed to instantiate distribution with valid input.");
        }
    }

    /**
     * Test constructor with invalid input data.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidInput() {
        new MultivariateNormalDistribution(MEANS, COVARIANCES_1);
    }

    /**
     * Test getMeans method.
     */
    @Test
    public void testGetMeans() {
        Assert.assertArrayEquals(MEANS, distribution.getMeans());
    }

    /**
     * Test getCovariances method.
     */
    @Test
    public void testGetCovariances() {
        RealMatrix covarianceMatrix = distribution.getCovariances();
        Assert.assertEquals(2, covarianceMatrix.getRowDimension());
        Assert.assertEquals(2, covarianceMatrix.getColumnDimension());
        Assert.assertArrayEquals(COVARIANCES_1, covarianceMatrix.getData()[0]);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[]{0.0, 0.0};
        double density = distribution.density(testValues);
        double expectedDensity = FastMath.exp(-0.5) * FastMath.sqrt(FastMath.PI) / 2;
        Assert.assertEquals(expectedDensity, density, 1e-12);

        testValues = new double[]{1.0, 1.0};
        density = distribution.density(testValues);
        expectedDensity = FastMath.exp(-0.5) * FastMath.sqrt(FastMath.PI) / (Math.sqrt(2 * FastMath.PI) * 2);
        Assert.assertEquals(expectedDensity, density, 1e-12);

        testValues = new double[]{2.0, 2.0};
        density = distribution.density(testValues);
        expectedDensity = FastMath.exp(-0.5 * 4) / (Math.sqrt(2 * FastMath.PI) * 2 * Math.sqrt(2 * FastMath.PI));
        Assert.assertEquals(expectedDensity, density, 1e-12);

        testValues = new double[]{100.0, 100.0};
        density = distribution.density(testValues);
        Assert.assertEquals(0.0, density, EPSILON);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertEquals(2, sample.length);
        for (int i = 0; i < 2; i++) {
            Assert.assertTrue(sample[i] >= -10 * Math.sqrt(2));
            Assert.assertTrue(sample[i] <= 10 * Math.sqrt(2));
        }
    }

    /**
     * Test sample method multiple times.
     */
    @Test
    public void testSampleMultipleTimes() {
        int N = 100_000;
        double[][] samples = new double[N][2];
        for (int i = 0; i < N; i++) {
            samples[i] = distribution.sample();
        }
        RealMatrix covarianceMatrix = distribution.getCovariances();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 2; j++) {
                Assert.assertTrue(samples[i][j] >= -10 * Math.sqrt(covarianceMatrix.getData()[j][j]));
                Assert.assertTrue(samples[i][j] <= 10 * Math.sqrt(covarianceMatrix.getData()[j][j]));
            }
        }

        double[] means = distribution.getMeans();
        double[] standardDeviations = distribution.getStandardDeviations();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 2; j++) {
                Assert.assertTrue(Math.abs(samples[i][j] - means[j]) <= 3 * standardDeviations[j]);
            }
        }
    }
}