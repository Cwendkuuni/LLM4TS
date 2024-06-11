package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultivariateNormalDistributionTest {

    // Test constructor with valid inputs
    @Test
    public void testConstructor_ValidInputs() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.2}, {0.5, 0.2, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            assertNotNull(distribution);
            assertArrayEquals(means, distribution.getMeans(), 0.0);
            assertArrayEquals(covariances, distribution.getCovariances().getData(), 0.0);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    // Test constructor with invalid inputs (DimensionMismatchException)
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_InvalidInputs_DimensionMismatchException() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.2}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test constructor with invalid inputs (SingularMatrixException)
    @Test(expected = SingularMatrixException.class)
    public void testConstructor_InvalidInputs_SingularMatrixException() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.2}, {0.5, 0.2, 0.5}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test constructor with invalid inputs (NonPositiveDefiniteMatrixException)
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_InvalidInputs_NonPositiveDefiniteMatrixException() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{-2.0, 1.0, 0.5}, {1.0, 3.0, 0.2}, {0.5, 0.2, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test density method with valid inputs
    @Test
    public void testDensity_ValidInputs() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.2}, {0.5, 0.2, 1.0}};
        double[] values = {1.5, 2.5, 3.5};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double density = distribution.density(values);
            assertEquals(0.0327521529567313, density, 0.0);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    // Test getStandardDeviations method with valid inputs
    @Test
    public void testGetStandardDeviations_ValidInputs() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.2}, {0.5, 0.2, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] stdDeviations = distribution.getStandardDeviations();
            assertArrayEquals(new double[]{1.4142135623730951, 1.7320508075688772, 1.0}, stdDeviations, 0.0);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    // Test sample method with valid inputs
    @Test
    public void testSample_ValidInputs() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.2}, {0.5, 0.2, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] sample = distribution.sample();
            assertNotNull(sample);
            assertEquals(3, sample.length);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

}