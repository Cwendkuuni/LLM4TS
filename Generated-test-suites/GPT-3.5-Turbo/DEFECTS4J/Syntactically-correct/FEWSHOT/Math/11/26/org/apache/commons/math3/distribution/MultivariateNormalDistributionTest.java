import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testCreateMultivariateNormalDistribution() {
        double[] means = {0.0, 1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            assertNotNull(distribution);
        } catch (DimensionMismatchException | NotSymmetricMatrixException |
                NotPositiveDefiniteMatrixException | SingularMatrixException e) {
            // Handle exception
        }
    }

    @Test
    public void testGetMeans() {
        double[] means = {0.0, 1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] actualMeans = distribution.getMeans();

            assertEquals(0.0, actualMeans[0], 0.0);
            assertEquals(1.0, actualMeans[1], 0.0);
            assertEquals(2.0, actualMeans[2], 0.0);
        } catch (DimensionMismatchException | NotSymmetricMatrixException |
                NotPositiveDefiniteMatrixException | SingularMatrixException e) {
            // Handle exception
        }
    }

    @Test
    public void testGetCovariances() {
        double[] means = {0.0, 1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[][] actualCovariances = distribution.getCovariances().getData();

            assertEquals(1.0, actualCovariances[0][0], 0.0);
            assertEquals(0.0, actualCovariances[0][1], 0.0);
            assertEquals(0.0, actualCovariances[0][2], 0.0);
            assertEquals(0.0, actualCovariances[1][0], 0.0);
            assertEquals(1.0, actualCovariances[1][1], 0.0);
            assertEquals(0.0, actualCovariances[1][2], 0.0);
            assertEquals(0.0, actualCovariances[2][0], 0.0);
            assertEquals(0.0, actualCovariances[2][1], 0.0);
            assertEquals(1.0, actualCovariances[2][2], 0.0);
        } catch (DimensionMismatchException | NotSymmetricMatrixException |
                NotPositiveDefiniteMatrixException | SingularMatrixException e) {
            // Handle exception
        }
    }

    @Test
    public void testDensity() {
        double[] means = {0.0, 1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        double[] values = {0.5, 1.5, 2.5};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double density = distribution.density(values);

            assertEquals(0.029559057198442393, density, 0.0);
        } catch (DimensionMismatchException | NotSymmetricMatrixException |
                NotPositiveDefiniteMatrixException | SingularMatrixException e) {
            // Handle exception
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {0.0, 1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] standardDeviations = distribution.getStandardDeviations();

            assertEquals(1.0, standardDeviations[0], 0.0);
            assertEquals(1.0, standardDeviations[1], 0.0);
            assertEquals(1.0, standardDeviations[2], 0.0);
        } catch (DimensionMismatchException | NotSymmetricMatrixException |
                NotPositiveDefiniteMatrixException | SingularMatrixException e) {
            // Handle exception
        }
    }

    @Test
    public void testSample() {
        double[] means = {0.0, 1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] sample = distribution.sample();

            assertNotNull(sample);
        } catch (DimensionMismatchException | NotSymmetricMatrixException |
                NotPositiveDefiniteMatrixException | SingularMatrixException e) {
            // Handle exception
        }
    }
}