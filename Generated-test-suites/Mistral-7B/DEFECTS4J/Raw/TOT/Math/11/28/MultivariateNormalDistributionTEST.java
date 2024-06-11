package org.apache.commons.math3.distribution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.descript.StatUtils;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {
    private static final int NUM_SAMPLES = 10000;
    private static final int DIM = 2;
    private RandomGenerator rng;
    private MultivariateNormalDistribution dist;

    @Before
    public void setUp() {
        rng = new Well19937c();
        double[] means = new double[] {0.0, 0.0};
        double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Failed to initialize distribution", e);
        }
    }

    /**
     * Test method <code>getMeans()</code>.
     */
    @Test
    public void testGetMeans() {
        Assert.assertArrayEquals(means, dist.getMeans(), 1e-12);
    }

    /**
     * Test method <code>getCovariances()</code>.
     */
    @Test
    public void testgetCovariances() {
        RealMatrix covariance = dist.getCovariances();
        Assert.assertArrayEquals(covariances[0], covariance.getRowVector(0), 1e-12);
        Assert.assertArrayEquals(covariances[1], covariance.getRowVector(1), 1e-12);
        Assert.assertEquals(covariance.getEntry(0, 1), covariances[0][1], 1e-12);
        Assert.assertEquals(covariance.getEntry(1, 0), covariances[0][1], 1e-12);
    }

    /**
     * Test method <code>density(double[])</code>.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[DIM];
        for (int i = 0; i < NUM_SAMPLES; i++) {
            testValues = dist.sample();
            double result = dist.density(testValues);
            // Check if we are within the margin of error
            double expected = StatUtils.pdf(testValues, dist.getMeans(), dist.getCovariances());
            Assert.assertTrue(Precision.abs(result, expected) <= 1e-5);
        }
    }

    /**
     * Test method <code>sample()</code>.
     */
    @Test
    public void testSample() {
        double[] testValues = new double[DIM];
        double[] mean = dist.getMeans();
        double[] standardDeviations = dist.getStandardDeviations();
        for (int i = 0; i < NUM_SAMPLES; i++) {
            dist.sample(testValues);
            // Check if samples are within the margin of error of mean and standard deviation
            for (int j = 0; j < DIM; j++) {
                Assert.assertTrue(Precision.abs(testValues[j] - mean[j]) <= standardDeviations[j] * 3);
            }
        }
    }

    /**
     * Test method <code>sample(RealVector)</code>.
     */
    @Test
    public void testSampleRealVector() {
        RealVector testValues = new Array2DRowRealMatrix(NUM_SAMPLES, DIM).transpose();
        for (int i = 0; i < NUM_SAMPLES; i++) {
            dist.sample(testValues.getColumnVector(i));
            // Check if samples are within the margin of error of mean and standard deviation
            for (int j = 0; j < DIM; j++) {
                Assert.assertTrue(Precision.abs(testValues.getEntry(i, j) - mean[j]) <= standardDeviations[j] * 3);
            }
        }
    }

    /**
     * Test method <code>sample(RealArray2DSample)</code>.
     */
    @Test
    public void testSampleRealArray2DSample() {
        RealArray2DSample testValues = new RealArray2DSample(DIM, NUM_SAMPLES, rng);
        dist.sample(testValues);
        RealMatrix result = testValues.getMatrix();
        // Check if samples are within the margin of error of mean and standard deviation
        for (int i = 0; i < NUM_SAMPLES; i++) {
            for (int j = 0; j < DIM; j++) {
                Assert.assertTrue(Precision.abs(result.getEntry(i, j) - mean[j]) <= standardDeviations[j] * 3);
            }
        }
    }
}