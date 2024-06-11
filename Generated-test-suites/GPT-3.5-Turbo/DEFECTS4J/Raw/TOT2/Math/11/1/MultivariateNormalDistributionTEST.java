import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    // Test public methods in MultivariateNormalDistribution class

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(means, mnd.getMeans(), 1e-15);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(covariances, mnd.getCovariances().getData(), 1e-15);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 2.0};
        assertEquals(0.15915494309189535, mnd.density(values), 1e-15);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] std = {Math.sqrt(2.0), Math.sqrt(1.0)};
        assertArrayEquals(std, mnd.getStandardDeviations(), 1e-15);
    }

    // Additional edge cases and error scenarios to be tested
    // Test case for SingularMatrixException
    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    // Test case for NonPositiveDefiniteMatrixException
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, -0.5}, {-0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    // Test case for dimension mismatch in density method
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchExceptionDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 2.0, 3.0}; // Dimension mismatch here
        mnd.density(values);
    }

    // Test actual sample generation
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] sample = mnd.sample();
        assertNotNull(sample);
        assertEquals(2, sample.length);
    }
}