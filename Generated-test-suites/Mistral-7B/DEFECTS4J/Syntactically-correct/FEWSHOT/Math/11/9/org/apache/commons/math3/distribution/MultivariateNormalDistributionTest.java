package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowAccessor;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {
    @Test
    public void testMultivariateNormalDistribution() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            final MultivariateNormalDistribution dist =
                    new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            assertEquals(means.length, dist.getDimension());
            assertArrayEquals(means, dist.getMeans());
            assertEquals(Arrays.determineRank(covariances[0]), dist.getCovariances().getRank());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testGetStandardDeviations() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            final MultivariateNormalDistribution dist =
                    new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            RealVector stdDevs = dist.getStandardDeviations();
            assertEquals(means.length, stdDevs.getDimension());
            assertEquals(1.0, stdDevs.getEntry(0), 1e-6);
            assertEquals(1.0, stdDevs.getEntry(1), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testDensity() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            final MultivariateNormalDistribution dist =
                    new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] values = new double[means.length];
            double density;

            // Test with values equal to means
            Arrays.fill(values, 1.0);
            density = dist.density(values);
            assertEquals(0.159297, density, 1e-6);

            // Test with values far from means
            values[0] = 5.0;
            values[1] = 5.0;
            density = dist.density(values);
            assertEquals(0.000136, density, 1e-6);

            // Test with values close to means
            values[0] = 1.5;
            values[1] = 2.5;
            density = dist.density(values);
            assertTrue(density > 0.01);

        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSample() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            final MultivariateNormalDistribution dist =
                    new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] sample;

            // Test with size equal to dimension
            sample = dist.sample();
            assertEquals(means.length, sample.length);
            for (int i = 0; i < sample.length; i++) {
                assertTrue(sample[i] >= -3 && sample[i] <= 3);
            }

            // Test with size greater than dimension
            sample = new double[3];
            dist.sample(sample);
            assertEquals(means.length, sample.length);
            for (int i = 0; i < sample.length; i++) {
                assertTrue(sample[i] >= -3 && sample[i] <= 3);
            }

        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testGetCovariances() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            final MultivariateNormalDistribution dist =
                    new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            RealMatrix covMatrix = dist.getCovariances();
            assertEquals(means.length, covMatrix.getRowDimension());
            assertEquals(means.length, covMatrix.getColumnDimension());
            RealArray2DRowAccessor covAccess = (RealArray2DRowAccessor) covMatrix;
            for (int i = 0; i < covMatrix.getRowDimension(); i++) {
                for (int j = 0; j < covMatrix.getColumnDimension(); j++) {
                    assertEquals(covariances[i][j], covAccess.getEntry(i, j), 1e-6);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}