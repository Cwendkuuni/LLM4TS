package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

/**
 * Test cases for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        final double[] means = new double[]{0.0, 1.0, 2.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}
        };

        final MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(3, mvn.getDimension());
        assertArrayEquals(means, mvn.getMeans());

        final RealMatrix covariance = mvn.getCovariances();
        assertArrayEquals(covariances[0].length, covariance.getColumnDimension());
        assertArrayEquals(covariances.length, covariance.getRowDimension());
        for (int i = 0; i < covariance.getRowDimension(); i++) {
            for (int j = 0; j < covariance.getColumnDimension(); j++) {
                assertEquals(covariances[i][j], covariance.getEntry(i, j), 1e-14);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionDimensionMismatch() {
        final double[] means = new double[]{0.0, 1.0, 2.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 3.0}
        };

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionSingularMatrix() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0}, {0.0, 0.0}
        };

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionNonPositiveDefiniteMatrix() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{
                {-1.0, 0.0}, {0.0, -1.0}
        };

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testMultivariateNormalDistributionDensity() {
        final double[] means = new double[]{0.0, 1.0, 2.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}
        };
        final MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] values = new double[3];
        double density;

        values = new double[]{0.0, 0.0, 0.0};
        density = mvn.density(values);
        assertEquals(Double.NEGATIVE_INFINITY, density, 1e-14);

        values = new double[]{0.0, 0.0, 1.0};
        density = mvn.density(values);
        assertEquals(0.0, density, 1e-14);

        values[2] = 0.5;
        density = mvn.density(values);
        assertEquals(1.0, density, 1e-14);

        values[2] = 2.0;
        density = mvn.density(values);
        assertEquals(0.0, density, 1e-14);
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        final double[] means = new double[]{0.0, 1.0, 2.0};
        final double[][] covariances = new double[][]{
                {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}
        };
        final MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        final double[] sample = mvn.sample();
        assertArrayEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertTrue(isFinite(sample[i]));
        }

        double[] values = new double[3];
        for (int i = 0; i < 10000; i++) {
            values = mvn.sample();
            assertArrayEquals(means.length, values.length);
            for (int j = 0; j < values.length; j++) {
                assertTrue(isFinite(values[j]));
            }
        }
    }
}