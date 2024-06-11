import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        // Test case for getting the mean vector
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        assertArrayEquals(means, mvn.getMeans(), 0.001);
    }

    @Test
    public void testGetCovariances() {
        // Test case for getting the covariance matrix
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        assertArrayEquals(covariances, mvn.getCovariances().getData(), 0.001);
    }

    @Test
    public void testDensity() {
        // Test case for calculating the density
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        double[] values = {0.5, 0.5};
        double density = mvn.density(values);

        assertEquals(0.15915494309189535, density, 0.001);
    }

    @Test
    public void testGetStandardDeviations() {
        // Test case for getting the standard deviations
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        double[] stdDeviations = mvn.getStandardDeviations();

        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, stdDeviations, 0.001);
    }

    @Test
    public void testSample() {
        // Test case for sampling from the distribution
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        double[] sample = mvn.sample();

        assertNotNull(sample);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatchException() {
        // Test case for dimension mismatch exception in density calculation
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        double[] values = {0.5, 0.5, 0.5}; // Incorrect dimensions
        mvn.density(values);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeEigenvaluesException() {
        // Test case for negative eigenvalues exception
        double[] means = {1.0, 1.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, -1.0}}; // Negative eigenvalue
        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
    }
}