package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpectralDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test method for {@link MultivariateNormalDistribution#MultivariateNormalDistribution(double[], double[][])}.
     */
    @Test
    public void testMultivariateNormalDistribution() {
        final double[] means = {0.0, 1.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        try {
            final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            Assert.assertArrayEquals(means, dist.getMeans(), 0.0);
            Assert.assertEquals(2, dist.getDimension());
            final RealMatrix covariance = dist.getCovariances();
            Assert.assertEquals(covariances[0].length, covariance.getRowDimension());
            Assert.assertEquals(covariances.length, covariance.getColumnDimension());
            final RealArray2DSpectralDecomposition decomp = new RealArray2DSpectralDecomposition(covariance);
            Assert.assertEquals(2, decomp.getRealEigenvalues().length);
            for (int i = 0; i < 2; i++) {
                Assert.assertGreater(decomp.getRealEigenvalues()[i], 0.0);
            }
            final double[] stddevs = dist.getStandardDeviations();
            Assert.assertArrayEquals(new double[]{1.0, 0.7071067811865476}, stddevs, 0.001);
            final double[] sample = dist.sample();
            for (int i = 0; i < sample.length; i++) {
                Assert.assertNotNaN(sample[i]);
            }
            final double density = dist.density(sample);
            final double expectedDensity = FastMath.pow(2 * FastMath.PI, -2 / 2) * FastMath.pow(decomp.getDeterminant(), -0.5) * dist.getExponentTerm(sample);
            Assert.assertEquals(expectedDensity, density, 1e-5);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            // Expected exceptions
        }
    }

    /**
     * Test method for {@link MultivariateNormalDistribution#MultivariateNormalDistribution(double[], double[][])}.
     */
    @Test
    public void testMultivariateNormalDistributionEmptyMeans() {
        final double[] means = {};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            Assert.fail("An empty mean vector should not be allowed.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    /**
     * Test method for {@link MultivariateNormalDistribution#MultivariateNormalDistribution(double[], double[][])}.
     */
    @Test
    public void testMultivariateNormalDistributionEmptyCovariances() {
        final double[] means = {0.0, 1.0};
        final double[][] covariances = {};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            Assert.fail("An empty covariance matrix should not be allowed.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    /**
     * Test method for {@link MultivariateNormalDistribution#MultivariateNormalDistribution(double[], double[][])}.
     */
    @Test
    public void testMultivariateNormalDistributionSingularMatrix() {
        final double[] means = {0.0, 1.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            Assert.fail("A singular covariance matrix should not be allowed.");
        } catch (SingularMatrixException e) {
            // Expected exception
        }
    }

    /**
     * Test method for {@link MultivariateNormalDistribution#MultivariateNormalDistribution(double[], double[][])}.
     */
    @Test
    public void testMultivariateNormalDistributionNonPositiveDefiniteMatrix() {
        final double[] means = {0.0, 1.0};
        final double[][] covariances = {{-1.0, 0.0}, {0.0, -1.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            Assert.fail("A non-positive definite covariance matrix should not be allowed.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    /**
     * Test method for {@link MultivariateNormalDistribution#MultivariateNormalDistribution(RandomGenerator, double[], double[][])}.
     */
    @Test
    public void testMultivariateNormalDistributionRandomGenerator() {
        final double[] means = {0.0, 1.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            Assert.assertNotNaN(sample[i]);
        }
        final double density = dist.density(sample);
        final double expectedDensity = FastMath.pow(2 * FastMath.PI, -2 / 2) * FastMath.pow(dist.getCovariances().determinant(), -0.5) * dist.getExponentTerm(sample);
        Assert.assertEquals(expectedDensity, density, 1e-5);
    }
}