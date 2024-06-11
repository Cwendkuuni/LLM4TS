package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test constructor with valid input.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testValidConstructor() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(means, dist.getMeans());
        RealMatrix covMatrix = dist.getCovariances();
        assertEquals(covariances[0].length, covMatrix.getColumnDimension());
        assertEquals(covariances[0].length, covMatrix.getRowDimension());
        assertMatrixEquals(covariances, covMatrix.getData());
    }

    /**
     * Test constructor with invalid input.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorMeans() throws Exception {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorCovariancesRows() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0}, {0.5, 1.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorCovariancesCols() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}, {0.5, 1.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test method getStandardDeviations.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testGetStandardDeviations() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] std = dist.getStandardDeviations();
        assertEquals(means.length, std.length);
        for (int i = 0; i < std.length; i++) {
            assertTrue(std[i] > 0);
        }
    }

    /**
     * Test method density.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testDensity() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = {1.5, 2.5};
        double result = dist.density(testValues);
        double expected = FastMath.pow(2 * FastMath.PI, -dist.getDimension()) * FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) * dist.getExponentTerm(testValues);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test method sample.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testSample() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] result = dist.sample();
        double[] expected = new double[dist.getDimension()];
        for (int i = 0; i < dist.getDimension(); i++) {
            expected[i] = dist.means[i] + dist.samplingMatrix.operate(new double[]{dist.random.nextGaussian(), dist.random.nextGaussian()})[i];
        }
        assertArrayEquals(expected, result, 0.001);
    }

    /**
     * Test method getExponentTerm.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testGetExponentTerm() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = {1.5, 2.5};
        double result = dist.getExponentTerm(testValues);
        double[] centered = new double[testValues.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = testValues[i] - dist.means[i];
        }
        double[] preMultiplied = dist.covarianceMatrixInverse.preMultiply(centered);
        double expected = FastMath.exp(-0.5 * FastMath.dotProduct(preMultiplied, centered));
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test method getCovariances.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testGetCovariances() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        RealMatrix result = dist.getCovariances();
        assertMatrixEquals(covariances, result.getData());
    }

    /**
     * Test method getMeans.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testGetMeans() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] result = dist.getMeans();
        assertArrayEquals(means, result, 0.001);
    }

    /**
     * Test constructor with invalid covariance matrix.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidConstructorCovariances() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, -1.5}, {-1.5, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test method sample when matrix is singular.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = SingularMatrixException.class)
    public void testSampleSingular() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0}, {0, 0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        dist.sample();
    }

    /**
     * Compares two matrices for equality.
     * @param a First matrix.
     * @param b Second matrix.
     */
    private void assertMatrixEquals(double[][] a, double[][] b) {
        assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; i++) {
            assertEquals(a[i].length, b[i].length);
            assertArrayEquals(a[i], b[i], 0.001);
        }
    }

    /**
     * Compares two arrays for equality.
     * @param a First array.
     * @param b Second array.
     * @param epsilon allowed difference.
     */
    private void assertArrayEquals(double[] a, double[] b, double epsilon) {
        assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; i++) {
            assertEquals(a[i], b[i], epsilon);
        }
    }

    /**
     * Initializes a random number generator.
     */
    private void setUp() {
        random = new Well19937c();
    }

    /**
     * Initializes a random number generator and a distribution.
     * @throws Exception if an error occurs.
     */
    @org.junit.Before
    public void setUpBeforeEach() throws Exception {
        setUp();
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        distribution = new MultivariateNormalDistribution(random, means, covariances);
    }

    /**
     * The random number generator.
     */
    private RandomGenerator random;

    /**
     * The distribution instance.
     */
    private MultivariateNormalDistribution distribution;
}