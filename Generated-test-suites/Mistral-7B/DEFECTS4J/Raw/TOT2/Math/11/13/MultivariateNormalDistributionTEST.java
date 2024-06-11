import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, distribution.getDimension());
        assertEquals(means, distribution.getMeans());
        RealMatrix covarianceMatrix = distribution.getCovariances();
        assertEquals(means.length, covarianceMatrix.getRowDimension());
        assertEquals(means.length, covarianceMatrix.getColumnDimension());
        Array2DRowRealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCovarianceMatrix, covarianceMatrix);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] std = distribution.getStandardDeviations();
        assertEquals(means.length, std.length);
        for (int i = 0; i < std.length; i++) {
            assertTrue(std[i] > 0);
        }
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertTrue(Double.isFinite(sample[i]));
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovarianceMatrix() {
        double[] means = {1.0, 1.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeCovarianceMatrix() {
        double[] means = {1.0, 1.0};
        double[][] covariances = {{1.0, -0.5}, {-0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInconsistentCovarianceMatrixDimensions() {
        double[] means = {1.0, 1.0};
        double[][] covariances = {{1.0}, {1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInconsistentMeansAndCovariancesLength() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] testVector = {1.5, 1.8};
        double density = distribution.density(testVector);
        double expectedDensity = 0.13417452335581398; // Calculated using MathWorld formulae
        assertEquals(expectedDensity, density, 0.000001);
    }

    @Test
    public void testDensityWithNonSquareMatrix() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0}, {1.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double density = distribution.density(new double[]{1.5, 1.8});
        assertEquals(Double.NaN, density);
    }

    @Test
    public void testDensityWithMeansAndCovariancesLengthMismatch() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double density = distribution.density(new double[]{1.5, 1.8});
        assertEquals(Double.NaN, density);
    }
}