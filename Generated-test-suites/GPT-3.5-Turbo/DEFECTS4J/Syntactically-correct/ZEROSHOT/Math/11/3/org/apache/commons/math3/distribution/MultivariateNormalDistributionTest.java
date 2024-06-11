import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testDensity() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {0, 0};
            double density = distribution.density(vals);
            assertEquals(0.15915494309189535, density, EPSILON);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] std = distribution.getStandardDeviations();
            assertEquals(1, std[0], EPSILON);
            assertEquals(1, std[1], EPSILON);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSample() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] sample = distribution.sample();
            assertEquals(2, sample.length);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatchException() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {0};
            distribution.density(vals);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {0, 0};
        double[][] covariances = {{0, 0}, {0, 0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {0, 0};
        double[][] covariances = {{1, -1}, {-1, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
}