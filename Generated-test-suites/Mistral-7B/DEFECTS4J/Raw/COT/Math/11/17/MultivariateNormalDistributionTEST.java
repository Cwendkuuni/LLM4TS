package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructWithVectorAndMatrix() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(means, dist.getMeans());
        RealMatrix covMatrix = dist.getCovariances();
        RealMatrix expectedCovMatrix = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCovMatrix, covMatrix);

        // Test method density
        double[] values = {1.5, 2.5};
        assertEquals(1.0, dist.density(values), 1e-10);

        // Test method sample
        double[] sample = dist.sample();
        assertArrayEquals(means, sample, 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructWithInconsistentArrays() throws Exception {
        double[] means = {1.0};
        double[][] covariances = {1.0, 1.0, 1.0};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructWithSingularCovarianceMatrix() throws Exception {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructWithNegativeEigenvalues() throws Exception {
        double[] means = {1.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetStandardDeviations() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{3.0, 2.0}, {2.0, 4.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] std = dist.getStandardDeviations();
        assertArrayEquals(new double[]{Math.sqrt(3.0), Math.sqrt(4.0)}, std, 1e-10);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[0][0]);
        assertArrayEquals(means, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{3.0, 2.0}, {2.0, 4.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covMatrix = dist.getCovariances();
        RealMatrix expectedCovMatrix = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCovMatrix, covMatrix);
    }

    @Test
    public void testDensity1D() throws Exception {
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, new double[]{0.0}, new double[][]{
                {1.0}
        });
        double[] values = {0.0};
        double density = dist.density(values);
        double expectedDensity = FastMath.sqrt(2.0 / FastMath.PI) * FastMath.exp(-0.5 * Math.pow(values[0], 2));
        assertEquals(expectedDensity, density, 1e-10);
    }

    @Test
    public void testDensityMultiD() throws Exception {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        double[] values = {1.5, 2.5};
        double density = dist.density(values);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -means.length / 2) * FastMath.pow(dist.getCovariances().determinant(), -0.5) * dist.getExponentTerm(values);
        assertEquals(expectedDensity, density, 1e-10);
    }

    @Test
    public void testSample1D() throws Exception {
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, new double[]{0.0}, new double[][]{
                {1.0}
        });
        double[] sample = dist.sample();
        assertEquals(1.0, sample.length);
        double expectedSample = rng.nextGaussian();
        assertEquals(expectedSample, sample[0], 1e-10);
    }

    @Test
    public void testSampleMultiD() throws Exception {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        double[] sample = dist.sample();
        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            double expectedSample = rng.nextGaussian();
            assertEquals(expectedSample, sample[i], 1e-10);
        }
    }
}