package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.ExceptionUtils;
import org.apache.commons.math3.LinearAlgebraUtils;
import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructors() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{3.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());
        RealMatrix cov = dist.getCovariances();
        assertEquals(means.length, cov.getRowDimension());
        assertEquals(means.length, cov.getColumnDimension());
        assertArrayEquals(covariances, cov.getData());
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{0.0, 2.0}, {2.0, 0.0}};
        MultivariateNormalDistribution dist = null;
        try {
            dist = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            dist = (MultivariateNormalDistribution) e;
        }
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());
        RealMatrix cov = dist.getCovariances();
        assertEquals(means.length, cov.getRowDimension());
        assertEquals(means.length, cov.getColumnDimension());
        assertEquals(0, cov.getDeterminant());
        assertArrayEquals(cov.getEigenvectors().getColumnVector(0), LinearAlgebraUtils.ZERO_VECTOR);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{-1.0, 2.0}, {2.0, -1.0}};
        MultivariateNormalDistribution dist = null;
        try {
            dist = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            dist = (MultivariateNormalDistribution) e;
        }
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());
        RealMatrix cov = dist.getCovariances();
        assertEquals(means.length, cov.getRowDimension());
        assertEquals(means.length, cov.getColumnDimension());
        assertArrayEquals(cov.getEigenvalues(), new double[]{-1.0, -1.0});
    }

    @Test
    public void testDensity() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{3.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = new double[]{1.5, 2.5};
        double density = dist.density(values);
        double expected = FastMath.pow(2 * FastMath.PI, -means.length / 2)
                * FastMath.pow(1.0 / FastMath.SQRT_2_PI, means.length)
                * FastMath.pow(FastMath.E, -0.5 * LinearAlgebraUtils.dotProduct(values, dist.getCovariances().preMultiply(values))
                + FastMath.dot(values, dist.covarianceMatrixInverse.operate(values)) / 2);
        assertEquals(expected, density, 1e-12);
    }

    @Test
    public void testSample() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{3.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RandomGenerator rng = new Well19937c();
        double[] sample = dist.sample();
        double[] expectedMeans = new double[]{means[0], means[1]};
        double[] expectedSample = new double[means.length];
        for (int i = 0; i < means.length; i++) {
            expectedSample[i] = rng.nextGaussian() + means[i];
        }
        assertArrayEquals(expectedMeans, sample, 1e-12);
        expectedSample = dist.samplingMatrix.operate(expectedSample);
        assertArrayEquals(sample, expectedSample, 1e-12);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{3.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = dist.getStandardDeviations();
        double[] meansCopy = new double[means.length];
        System.arraycopy(means, 0, meansCopy, 0, means.length);
        double[] expectedStdDeviations = new double[means.length];
        for (int i = 0; i < means.length; i++) {
            expectedStdDeviations[i] = FastMath.sqrt(covariances[i][i]);
        }
        assertArrayEquals(expectedStdDeviations, stdDeviations, 1e-12);
    }

    @Test
    public void testGetMeans() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{3.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = new double[]{1.5, 2.5};
        double density = dist.density(values);
        RealMatrix cov = dist.getCovariances();
        RealVector mu = dist.getMeans();
        double[] sample = dist.sample();
        double[] expectedSample = new double[means.length];
        System.arraycopy(means, 0, expectedSample, 0, means.length);
        for (int i = 0; i < means.length; i++) {
            expectedSample[i] += dist.getStandardDeviations()[i] * RandomUtils.nextGaussian();
        }
        assertEquals(expectedSample, sample, 1e-12);
        assertEquals(expectedSample, LinearAlgebraUtils.add(mu, LinearAlgebraUtils.multiply(cov.getInverse(),
                LinearAlgebraUtils.multiply(samplingMatrix(means.length), LinearAlgebraUtils.multiply(new Array2DRowRealMatrix(new double[][]{
                        {1.0, 0.0}, {0.0, 1.0}}), RealVectorUtils.createColumnVector(values))))));
        assertEquals(mu.getDimension(), means.length);
        assertEquals(mu.getEntry(0), means[0]);
        assertEquals(mu.getEntry(1), means[1]);
    }

    @Test
    public void testGetCovariances() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{3.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix cov = dist.getCovariances();
        RealMatrix expectedCov = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCov.getRowDimension(), means.length);
        assertEquals(expectedCov.getColumnDimension(), means.length);
        for (int i = 0; i < means.length; i++) {
            for (int j = 0; j < means.length; j++) {
                assertEquals(cov.getEntry(i, j), covariances[i][j], 1e-12);
            }
        }
    }

    private static RealMatrix samplingMatrix(int dim) {
        RealMatrix eigenvectors = new Array2DRowRealMatrix(dim, dim);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(new IdentityMatrix(dim));
        eigenvectors = eigenDecomposition.getEigenvectors().transpose();
        RealMatrix invSqrtEigenvectors = new Array2DRowRealMatrix(dim, dim);
        for (int i = 0; i < dim; i++) {
            double factor = FastMath.sqrt(eigenvectors.getEigenvalue(i));
            for (int j = 0; j < dim; j++) {
                invSqrtEigenvectors.setEntry(i, j, factor);
            }
        }
        return eigenvectors.multiply(invSqrtEigenvectors);
    }

    @Test
    public void testToString() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{3.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals("MultivariateNormalDistribution(means: [1.0, 2.0], covariances:\n" +
                " [3.0, 2.0],\n" +
                " [2.0, 3.0])", dist.toString());
    }
}