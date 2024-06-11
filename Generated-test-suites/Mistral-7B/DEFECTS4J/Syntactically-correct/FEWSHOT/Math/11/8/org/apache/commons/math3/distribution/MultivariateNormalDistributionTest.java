package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test cases for MultivariateNormalDistribution class
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            assertEquals(means.length, mvnd.getDimension());
            assertEquals(means, mvnd.getMeans());
            RealMatrix covarianceMatrix = mvnd.getCovariances();
            Array2DRowRealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
            assertEquals(expectedCovarianceMatrix, covarianceMatrix);
        } catch (DimensionMismatchException e) {
            fail("Exception should not be thrown");
        } catch (SingularMatrixException e) {
            fail("Exception should not be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }

        // Test with null input
        try {
            new MultivariateNormalDistribution(null, null, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        try {
            mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception should not be thrown");
        }

        double[] testPoint = {1.5, 2.5};
        double density;
        try {
            density = mvnd.density(testPoint);
            double expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(mvnd.covarianceMatrixDeterminant, -0.5) * mvnd.getExponentTerm(testPoint);
            assertEquals(expectedDensity, density, 0.001);
        } catch (DimensionMismatchException e) {
            fail("Exception should not be thrown");
        }

        double[] wrongTestPoint = {3.0, 2.0};
        try {
            density = mvnd.density(wrongTestPoint);
            assertEquals(0.0, density, 0.001);
        } catch (DimensionMismatchException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        try {
            mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        double[] sample;
        try {
            sample = mvnd.sample();
            for (int i = 0; i < sample.length; i++) {
                assertEquals(means[i], sample[i], 0.01);
            }
        } catch (DimensionMismatchException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        try {
            mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        double[] standardDeviations = mvnd.getStandardDeviations();
        assertEquals(means.length, standardDeviations.length);
        for (int i = 0; i < standardDeviations.length; i++) {
            assertTrue(standardDeviations[i] > 0);
        }
    }
}