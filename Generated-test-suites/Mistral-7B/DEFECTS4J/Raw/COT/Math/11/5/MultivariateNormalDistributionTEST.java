package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DimensionException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

/**
 * Test suite for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidConstruction() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertNotNull(dist.getMeans());
        assertArrayEquals(means, dist.getMeans());
        assertNotNull(dist.getCovariances());
        RealMatrix covMatrix = dist.getCovariances().copy();
        assertArrayEquals(covariances[0], covMatrix.getRowVector(0).toArray());
        assertArrayEquals(covariances[1], covMatrix.getRowVector(1).toArray());
    }

    /**
     * Test constructor with invalid input (dimension mismatch).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructionWithDimensionMismatch() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test constructor with invalid input (non-positive definite matrix).
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidConstructionWithNonPositiveDefiniteMatrix() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{-1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test method getMeans().
     */
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[2][2]);
        assertNotNull(dist.getMeans());
        assertArrayEquals(means, dist.getMeans());
    }

    /**
     * Test method getCovariances().
     */
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertNotNull(dist.getCovariances());
        RealMatrix covMatrix = dist.getCovariances().copy();
        assertArrayEquals(covariances[0], covMatrix.getRowVector(0).toArray());
        assertArrayEquals(covariances[1], covMatrix.getRowVector(1).toArray());
    }

    /**
     * Test method density().
     */
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {1.5, 2.5};
        double expectedDensity = 0.14681369134444841;
        assertEquals(expectedDensity, dist.density(testValues), 0.0001);
    }

    /**
     * Test method density() with invalid input (dimension mismatch).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidInputDimensionMismatch() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {1.5, 2.5, 3.0};
        dist.density(testValues);
    }

    /**
     * Test method sample().
     */
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sampleValues = dist.sample();
        assertArrayEquals(means.length, sampleValues.length);
        for (int i = 0; i < sampleValues.length; i++) {
            assertTrue(sampleValues[i] >= means[i] - 3.0);
            assertTrue(sampleValues[i] <= means[i] + 3.0);
        }
    }

    /**
     * Test method sample() with SingularMatrixException.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSampleWithSingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{0.0, 1.0}, {1.0, 0.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        dist.sample();
    }

    /**
     * Test method sample() with NonPositiveDefiniteMatrixException.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSampleWithNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{-1.0, 1.0}, {1.0, -1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        dist.sample();
    }

    /**
     * Test method getStandardDeviations().
     */
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = dist.getStandardDeviations();
        assertEquals(Math.sqrt(0.5), stdDeviations[0], 0.0001);
        assertEquals(Math.sqrt(0.5), stdDeviations[1], 0.0001);
    }
}