package org.apache.commons.math3.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution.CovarianceMatrixException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test for constructor with Vector of means and covariance matrix.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testConstructorVectorMeansAndCovariances() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertNotNull(distribution);
        assertArrayEquals(means, distribution.getMeans());
        RealMatrix covarianceMatrix = distribution.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        assertArrayEquals(expectedCovarianceMatrix.getData(), covarianceMatrix.getData());
    }

    /**
     * Test for constructor with RandomGenerator, Vector of means, and covariance matrix.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testConstructorRandomGeneratorVectorMeansAndCovariances() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}};
        RandomGenerator randomGenerator = new Well19937c();
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(randomGenerator, means, covariances);
        assertNotNull(distribution);
        assertArrayEquals(means, distribution.getMeans());
        RealMatrix covarianceMatrix = distribution.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        assertArrayEquals(expectedCovarianceMatrix.getData(), covarianceMatrix.getData());
    }

    /**
     * Test for method getStandardDeviations().
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGetStandardDeviations() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = distribution.getStandardDeviations();
        assertArrayEquals(new double[] {1.0, 1.0}, standardDeviations);
    }

    /**
     * Test for method sample().
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testSample() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}};
        RandomGenerator randomGenerator = new Well19937c();
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(randomGenerator, means, covariances);
        double[] sample = distribution.sample();
        assertArrayEquals(new double[] {1.64069791, 1.93333554}, sample);
    }

    /**
     * Test for method density() with valid input.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testDensityValidInput() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {1.5, 1.8};
        double density = distribution.density(testValues);
        double expectedDensity = Math.exp(-0.5 * 0.5) * Math.pow(2 * Math.PI, -2 / 2) / Math.sqrt(2 * Math.PI);
        expectedDensity *= Math.exp(-0.5 * ((testValues[0] - means[0]) * ((testValues[0] - means[0])) + (testValues[1] - means[1]) * ((testValues[1] - means[1]))));
        assertEquals(Precision.round(expectedDensity, 10), density, 1e-10);
    }

    /**
     * Test for method density() with dimension mismatch.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testDensityDimensionMismatch() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {1.5, 1.8, 3.0};
        assertThrows(DimensionMismatchException.class, () -> distribution.density(testValues));
    }

    /**
     * Test for constructor with invalid covariance matrix.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testConstructorInvalidCovarianceMatrix() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, -1.0}};
        RandomGenerator randomGenerator = new Well19937c();
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new MultivariateNormalDistribution(randomGenerator, means, covariances));
    }

    /**
     * Test for constructor with invalid dimension of covariance matrix.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testConstructorInvalidDimensionCovarianceMatrix() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}, {1.0, 1.0}};
        RandomGenerator randomGenerator = new Well19937c();
        assertThrows(DimensionMismatchException.class, () -> new MultivariateNormalDistribution(randomGenerator, means, covariances));
    }

    /**
     * Test for constructor with invalid mean vector length.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testConstructorInvalidMeanVectorLength() throws Exception {
        double[] means = new double[3];
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}};
        RandomGenerator randomGenerator = new Well19937c();
        assertThrows(DimensionMismatchException.class, () -> new MultivariateNormalDistribution(randomGenerator, means, covariances));
    }

    /**
     * Test for constructor with invalid covariance matrix length.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testConstructorInvalidCovarianceMatrixLength() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.0}, {1.0, 1.0}};
        RandomGenerator randomGenerator = new Well19937c();
        assertThrows(DimensionMismatchException.class, () -> new MultivariateNormalDistribution(randomGenerator, means, covariances));
    }
}