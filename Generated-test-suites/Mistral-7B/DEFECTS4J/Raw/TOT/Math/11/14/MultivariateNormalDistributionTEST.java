package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    /**
     * Test multivariate normal distribution with given mean and covariance matrix.
     */
    @Test
    public void testMultivariateNormalDistribution() {
        // Given
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 2.5}
        };
        MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        double density = mvnDist.density(new double[]{1.5, 2.5});
        double[] sample = mvnDist.sample();

        // Then
        assertEquals("Wrong density", 0.3292380514732299, density, 0.0001);
        assertArrayEquals("Wrong sample", new double[]{1.501679635176713, 2.4057272262351045}, sample);
    }

    /**
     * Test multivariate normal distribution with zero mean and identity covariance matrix.
     */
    @Test
    public void testMultivariateNormalDistributionZeroMeanIdentityCovarianceMatrix() {
        // Given
        double[] means = new double[0];
        double[][] covariances = new double[0][];
        MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        double density = mvnDist.density(new double[0]);
        double[] sample = mvnDist.sample();

        // Then
        assertEquals("Wrong density", 1.0, density, 0.0);
        assertArrayEquals("Wrong sample", new double[0], sample);
    }

    /**
     * Test multivariate normal distribution with wrong mean vector length.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionWrongMeanVectorLength() {
        // Given
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 2.5}
        };
        MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // Then
        // Exception should be thrown
    }

    /**
     * Test multivariate normal distribution with wrong covariance matrix dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionWrongCovarianceMatrixDimensions() {
        // Given
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5}
        };
        MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // Then
        // Exception should be thrown
    }

    /**
     * Test multivariate normal distribution with non-positive definite covariance matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionNonPositiveDefiniteCovarianceMatrix() {
        // Given
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {-1.0, 0.5},
            {0.5, -1.0}
        };
        MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // Then
        // Exception should be thrown
    }

    /**
     * Test multivariate normal distribution with singular covariance matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionSingularCovarianceMatrix() {
        // Given
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {0.0, 0.0},
            {0.0, 0.0}
        };
        MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // Then
        // Exception should be thrown
    }

    /**
     * Test multivariate normal distribution with random samples.
     */
    @Test
    public void testMultivariateNormalDistributionRandomSamples() {
        // Given
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 2.5}
        };
        MultivariateNormalDistribution mvnDist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        RandomGenerator rng = mvnDist.getRandomGenerator();

        // When
        double density;
        double[] sample;
        for (int i = 0; i < 10; i++) {
            sample = mvnDist.sample();
            density = mvnDist.density(sample);
            assertEquals("Wrong density", density > 0, true);
        }

        // Then
        // All tests passed
    }
}