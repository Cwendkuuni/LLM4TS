package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArrayRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    MultivariateNormalDistribution dist;
    RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
    }

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidConstruction() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
            assertNotNull(dist.getMeans());
            assertNotNull(dist.getCovariances());
            assertEquals(means.length, dist.getDimension());
        } catch (DimensionMismatchException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (SingularMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstruction() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0, 3.0}};
        new MultivariateNormalDistribution(rng, means, covariances);
    }

    /**
     * Test density method with valid input.
     */
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
            double[] testValues = {1.5, 2.5};
            double density = dist.density(testValues);
            double expectedDensity = FastMath.pow(FastMath.PI * 2, -2)
                    * FastMath.pow(FastMath.E, -((1.5 - means[0]) * (1.5 - means[0])
                            / 1.0 + (2.5 - means[1]) * (2.5 - means[1]) / 2.0));
            assertEquals(expectedDensity, density, 0.001);
        } catch (DimensionMismatchException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (SingularMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test density method with invalid input.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
            double[] testValues = {1.5, 2.5, 3.5};
            dist.density(testValues);
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    /**
     * Test getMeans method.
     */
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
            double[] result = dist.getMeans();
            assertArrayEquals(means, result);
        } catch (DimensionMismatchException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (SingularMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test getCovariances method.
     */
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
            RealMatrix result = dist.getCovariances();
            RealMatrix expected = new Array2DRowRealMatrix(covariances);
            assertArrayEquals(expected.getData(), result.getData());
        } catch (DimensionMismatchException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (SingularMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
            double[] result = dist.sample();
            // TODO: Test the result against expected values.
            assertEquals(means.length, result.length);
        } catch (DimensionMismatchException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (SingularMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test sample method with invalid input.
     */
    @Test
    public void testSampleInvalidInput() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
            dist.sample(10);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}