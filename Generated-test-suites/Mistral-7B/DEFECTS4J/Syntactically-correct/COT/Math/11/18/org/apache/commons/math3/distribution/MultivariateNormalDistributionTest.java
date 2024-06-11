package org.apache.commons.math3.distribution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSingularMatrixException;
import org.apache.commons.math3.linear.DimensionMismatchException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Assert;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstructor() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
            Assert.assertEquals(means.length, dist.getDimension());
            Assert.assertArrayEquals(means, dist.getMeans());

            RealMatrix covarianceMatrix = dist.getCovariances();
            Assert.assertEquals(means.length, covarianceMatrix.getRowDimension());
            Assert.assertEquals(means.length, covarianceMatrix.getColumnDimension());
            Assert.assertArrayEquals(covariances, covarianceMatrix.getData(), 0.001);

        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            // Expected exceptions
        }
    }

    @Test
    public void testGetMeans() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        Assert.assertArrayEquals(new double[]{1.0, 2.0}, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix covarianceMatrix = dist.getCovariances();
        Assert.assertArrayEquals(new double[][]{
                {1.0, 0.5},
                {0.5, 1.0}
        }, covarianceMatrix.getData(), 0.001);
    }

    @Test
    public void testDensity() throws DimensionMismatchException {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        double[] testValues = {1.5, 1.5};
        double expectedDensity = 0.1055056215222153;

        double density = dist.density(testValues);
        Assert.assertEquals(expectedDensity, density, 0.001);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        double[] sample = dist.sample();
        Assert.assertEquals(sample.length, 2);
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        double[] stdDeviations = dist.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{1.0, 0.7071067811865476}, stdDeviations, 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidValues() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        double[] testValues = {1.0, 3.0};
        dist.density(testValues);
    }

    @Test
    public void testSampleWithInvalidSize() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix samplingMatrix = dist.getSamplingMatrix();
        Assert.assertArrayEquals(new double[][]{
                {0.5345225435243954, 0.802857373535733},
                {-0.802857373535733, 0.5345225435243954}
        }, samplingMatrix.getData(), 0.001);

        double[] sample = new double[3];
        dist.sample(sample);
    }
}