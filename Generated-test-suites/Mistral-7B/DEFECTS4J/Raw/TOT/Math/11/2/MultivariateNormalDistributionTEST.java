package org.apache.commons.math3.distribution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test the constructor with valid input.
     */
    @Test
    public void testValidConstruction() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            Assert.fail("Invalid construction with valid input: " + e.getMessage());
        }
        Assert.assertNotNull(mnd);
        Assert.assertEquals(means.length, mnd.getDimension());
        Assert.assertArrayEquals(means, mnd.getMeans());
        RealMatrix covariance = mnd.getCovariances();
        Assert.assertEquals(means.length, covariance.getRowDimension());
        Assert.assertEquals(means.length, covariance.getColumnDimension());
        Assert.assertArrayEquals(covariances, covariance.getData(), 0.0);
    }

    /**
     * Test the constructor with incorrect input size.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInconsistentInputSize() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the constructor with non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the method density with valid input.
     */
    @Test
    public void testDensity() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            Assert.fail("Invalid construction with valid input: " + e.getMessage());
        }

        final double[][] testData = new double[][]{{0.0, 0.5}, {1.0, 1.5}, {-1.0, 1.5}};
        for (double[] values : testData) {
            double density = 0.0;
            try {
                density = mnd.density(values);
            } catch (DimensionMismatchException e) {
                Assert.fail("Unexpected exception: " + e.getMessage());
            }
            Assert.assertTrue("Unexpected density value: " + density, density > 0.0);
        }
    }

    /**
     * Test the method density with edge cases.
     */
    @Test
    public void testDensityEdgeCases() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            Assert.fail("Invalid construction with valid input: " + e.getMessage());
        }

        final double[][] testData = new double[][]{{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                {Double.NEGATIVE_INFINITY, 0.0},
                {0.0, Double.NEGATIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, 0.0},
                {0.0, Double.POSITIVE_INFINITY}};
        double[] densities = new double[testData.length];
        for (int i = 0; i < testData.length; i++) {
            try {
                densities[i] = mnd.density(testData[i]);
            } catch (Exception e) {
                Assert.fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Check against the expected values
        double[] expectedDensities = new double[]{Double.NEGATIVE_INFINITY,
                0.0, 0.0, Double.POSITIVE_INFINITY, 0.0, 0.0};
        for (int i = 0; i < densities.length; i++) {
            Assert.assertEquals("Unexpected density value: " + densities[i] +
                    " at " + Arrays.toString(testData[i]) +
                    " expected " + expectedDensities[i],
                    expectedDensities[i], densities[i]);
        }
    }

    /**
     * Test the method density with error scenarios.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityError() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            Assert.fail("Invalid construction with valid input: " + e.getMessage());
        }

        final double[] invalidData = new double[]{1.0, 1.1};
        mnd.density(invalidData);
    }

    /**
     * Test the method sample with valid input.
     */
    @Test
    public void testSample() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            Assert.fail("Invalid construction with valid input: " + e.getMessage());
        }

        final int sampleCount = 100;
        double[][] samples = new double[sampleCount][mnd.getDimension()];
        for (int i = 0; i < sampleCount; i++) {
            samples[i] = mnd.sample();
        }

        // Check that all samples are valid
        for (double[] sample : samples) {
            try {
                mnd.density(sample);
            } catch (DimensionMismatchException e) {
                Assert.fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Check that the mean and covariance of the samples match the expected
        double[] sampleMeans = new double[mnd.getDimension()];
        double[] sampleCovariances = new double[mnd.getDimension()][mnd.getDimension()];
        for (double[] sample : samples) {
            for (int j = 0; j < sample.length; j++) {
                sampleMeans[j] += sample[j];
            }
        }
        for (int i = 0; i < mnd.getDimension(); i++) {
            sampleMeans[i] /= sampleCount;
        }
        RealMatrix sampleCovariance = new Array2DRowRealMatrix(sampleCovariances);
        for (int i = 0; i < sampleCount; i++) {
            double[] sampleDifferences = new double[mnd.getDimension()];
            for (int j = 0; j < sampleDifferences.length; j++) {
                sampleDifferences[j] = samples[i][j] - sampleMeans[j];
            }
            sampleCovariance.addToEntry(i, i, FastMath.pow(FastMath.E, -0.5) *
                    FastMath.dot(sampleCovarianceInverse(sampleCovariance), sampleDifferences));
        }
        RealMatrix expectedCovariance = mnd.getCovariances();
        Assert.assertEquals(expectedCovariance, sampleCovariance);
    }

    /**
     * Helper method for computing the inverse of a covariance matrix.
     */
    private RealMatrix sampleCovarianceInverse(RealMatrix covariance) {
        EigenDecomposition covMatDec = new EigenDecomposition(covariance);
        return covMatDec.getSolver().getInverse();
    }
}