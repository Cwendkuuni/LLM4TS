package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution.Well19937c;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
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
    public void testConstructionWithValidMeansAndCovariances() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, mvnDist.getDimension());
        assertEquals(means, mvnDist.getMeans());

        final RealMatrix covarianceMatrix = mvnDist.getCovariances();
        assertArrayEquals(covariances, covarianceMatrix.getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructionWithInconsistentMeansAndCovariances() {
        final double[] means = new double[]{1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0, 0.5}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructionWithInconsistentCovariancesRows() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0, 0.5}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructionWithInconsistentCovariancesColumns() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructionWithSingularCovarianceMatrix() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{0.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructionWithNegativeEigenvalues() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);

        final double[] testValues = new double[]{1.5, 2.5};
        final double expected = FastMath.pow(FastMath.E, -1.0) * FastMath.pow(2.0 * FastMath.PI, -2.0) * FastMath.exp(-0.25 * testValues[0] * testValues[0] - 0.25 * testValues[1] * testValues[1] + 0.5 * testValues[0] * testValues[1] + 0.5 * testValues[1] * testValues[1] + 0.5 * testValues[0] * testValues[0]);
        final double actual = mvnDist.density(testValues);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testSample() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);

        final double[] sample = mvnDist.sample();
        assertArrayEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assert(FastMath.isFinite(sample[i]));
        }
    }

    @Test
    public void testGetStandardDeviations() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);

        final double[] expectedStdDev = new double[]{FastMath.sqrt(1.0), FastMath.sqrt(1.0)};
        assertArrayEquals(expectedStdDev, mvnDist.getStandardDeviations());
    }

    @Test
    public void testGetMeans() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);

        final double[] expectedMeans = new double[]{1.0, 2.0};
        assertArrayEquals(expectedMeans, mvnDist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);

        final RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        assertArrayEquals(expectedCovariances.getData(), mvnDist.getCovariances().getData());
    }

    @Test
    public void testGetExponentTerm() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);

        final double[] sample = new double[]{1.5, 2.5};
        final double expectedExponentTerm = FastMath.exp(-0.25 * Math.pow(sample[0] - means[0], 2.0) - 0.25 * Math.pow(sample[1] - means[1], 2.0) + 0.5 * (sample[0] - means[0]) * (sample[1] - means[1]));
        final double actualExponentTerm = mvnDist.getExponentTerm(sample);
        assertEquals(expectedExponentTerm, actualExponentTerm, 0.001);
    }

    @Test
    public void testSampleUsingMockRandomGenerator() {
        final double[] means = new double[]{1.0, 2.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(means, covariances);
        final RandomGenerator rng = mock(Well19937c.class);

        when(rng.nextGaussian()).thenCallRealMethod();

        final double[] sample = mvnDist.sample(rng);
        assertArrayEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assert(FastMath.isFinite(sample[i]));
        }
    }
}