import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] expectedMeans = {1.0, 2.0, 3.0};
            double[] actualMeans = distribution.getMeans();
            assertArrayEquals(expectedMeans, actualMeans, 0.0);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[][] expectedCovariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
            double[][] actualCovariances = distribution.getCovariances().getData();
            assertArrayEquals(expectedCovariances, actualCovariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] values = {1.0, 2.0, 3.0};
            double expectedDensity = 0.15915494309189535;
            double actualDensity = distribution.density(values);
            assertEquals(expectedDensity, actualDensity, 0.0);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] expectedStandardDeviations = {1.0, 1.0, 1.0};
            double[] actualStandardDeviations = distribution.getStandardDeviations();
            assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0.0);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] sample = distribution.sample();
            assertNotNull(sample);
            assertEquals(3, sample.length);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidCovarianceMatrix() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Exception not thrown");
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            // Exception expected
        }
    }

    @Test
    public void testInvalidDimension() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Exception not thrown");
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            // Exception expected
        }
    }
}