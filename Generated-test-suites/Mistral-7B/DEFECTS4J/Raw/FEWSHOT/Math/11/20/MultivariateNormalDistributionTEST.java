package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {

    @Test
    public void testCreation() {
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
            assertEquals(means.length, dist.getDimension());
            assertEquals(means.length, dist.getMeans().length);
            assertEquals(means.length * means.length, dist.getCovariances().getRowDimension());
            assertEquals(means.length * means.length, dist.getCovariances().getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (SingularMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (DimensionMismatchException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testGetMeans() {
        final double[] means = {0.0, 0.0};
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[0][0]);
        assertArrayEquals(means, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covariancesCopy = dist.getCovariances().copy();
        assertArrayEquals(covariances, covariancesCopy.getData());
    }

    @Test
    public void testSample() {
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        final RandomGenerator rng = new Well19937c();
        final double[] sample = dist.sample();
        assertTrue(sample.length == dist.getDimension());
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i] + Math.random() * Math.sqrt(covariances[i][i]), sample[i], 0.001);
        }
    }

    @Test
    public void testDensity() {
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        final double[] sample = {0.5, 0.5};
        final double density = dist.density(sample);
        assertEquals(0.120461921501998, density, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(new double[]{1.0, 1.0}, dist.getStandardDeviations());
    }
}