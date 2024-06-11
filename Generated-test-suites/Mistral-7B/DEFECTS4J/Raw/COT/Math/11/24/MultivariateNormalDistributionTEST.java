package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Test class for the MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test constructor with valid input.
     * @throws Exception if the constructor fails.
     */
    @org.junit.Test
    public void testValidConstructor() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(2, dist.getDimension());
        assertEquals(means, dist.getMeans());
        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(covariances[0].length, covarianceMatrix.getRowDimension());
        assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        assertArrayEquals(covariances, covarianceMatrix.getData(), 0.001);
    }

    /**
     * Test constructor with invalid input - dimensions do not match.
     * @throws Exception if the constructor fails.
     */
    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorWithDifferentDimensions() throws Exception {
        double[] means = new double[] {1.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5, 0.1}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test constructor with invalid input - non-square matrix.
     * @throws Exception if the constructor fails.
     */
    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorWithNonSquareMatrix() throws Exception {
        double[] means = new double[] {1.0};
        double[][] covariances = new double[][] {
            {0.5},
            {0.3, 0.5, 0.1}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test method density.
     * @throws Exception if the method fails.
     */
    @org.junit.Test
    public void testDensity() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {1.5, 2.5};
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2 / 2) *
                FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) *
                dist.getExponentTerm(testValues);
        double actualDensity = dist.density(testValues);
        assertEquals(expectedDensity, actualDensity, 0.001);
    }

    /**
     * Test method density with negative input.
     * @throws Exception if the method fails.
     */
    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testDensityWithNegativeInput() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {-1.0, 2.0};
        dist.density(testValues);
    }

    /**
     * Test method sample.
     * @throws Exception if the method fails.
     */
    @org.junit.Test
    public void testSample() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5}
        };
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        double[] sample = dist.sample();
        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i], sample[i], 0.001);
        }
    }

    /**
     * Test method sample with null random number generator.
     * @throws Exception if the method fails.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSampleWithNullRng() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        dist.sample(null);
    }

    /**
     * Test method getExponentTerm.
     * @throws Exception if the method fails.
     */
    @org.junit.Test
    public void testGetExponentTerm() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {1.5, 2.5};
        double expectedExponentTerm = FastMath.exp(-0.5 * (dist.covarianceMatrixInverse.multiply(testValues).dotProduct(testValues)));
        double actualExponentTerm = dist.getExponentTerm(testValues);
        assertEquals(expectedExponentTerm, actualExponentTerm, 0.001);
    }

    /**
     * Test method getExponentTerm with null input.
     * @throws Exception if the method fails.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetExponentTermWithNullInput() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {0.5, 0.3},
            {0.3, 0.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        dist.getExponentTerm(null);
    }
}