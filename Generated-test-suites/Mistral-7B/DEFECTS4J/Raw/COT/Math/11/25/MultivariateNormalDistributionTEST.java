package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;

        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
            assertEquals(means.length, mvnd.getDimension());
            assertArrayEquals(means, mvnd.getMeans());
        } catch (DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionConstructorInvalidMeansLength() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionConstructorInvalidCovariancesRows() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionConstructorInvalidCovariancesColumns() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        double[] sample;

        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
            sample = mvnd.sample();
            assertEquals(means.length, sample.length);
            for (int i = 0; i < sample.length; i++) {
                assertNotSame(means[i], sample[i]);
            }
        } catch (DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultivariateNormalDistributionDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        double[] testValues = {1.5, 2.5};
        double density;

        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
            density = mvnd.density(testValues);
            double expected = FastMath.pow(2 * FastMath.PI, -means.length / 2)
                    * FastMath.pow(mvnd.getCovariances().determinant(), -0.5)
                    * mvnd.getExponentTerm(testValues);
            assertEquals(expected, density, 1e-10);
        } catch (DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionDensityInvalidValuesLength() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        double[] testValues = {1.5, 2.5, 3.0};

        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
            mvnd.density(testValues);
            fail("Expected exception: " + DimensionMismatchException.class.getName());
        } catch (DimensionMismatchException e) {
        }
    }

    @Test
    public void testMultivariateNormalDistributionStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd;
        double[] stdDeviations;

        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
            stdDeviations = mvnd.getStandardDeviations();
            assertEquals(means.length, stdDeviations.length);
            for (int i = 0; i < stdDeviations.length; i++) {
                assertEquals(Math.sqrt(covariances[i][i]), stdDeviations[i], 1e-10);
            }
        } catch (DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (SingularMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}