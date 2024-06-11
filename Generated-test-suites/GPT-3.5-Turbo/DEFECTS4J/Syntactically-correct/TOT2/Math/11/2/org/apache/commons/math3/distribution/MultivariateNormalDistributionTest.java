import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        // Test case for getting the mean vector
        // Testing for a valid mean vector
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        double[] result = dist.getMeans();
        assertArrayEquals(means, result, 0.0);
    }

    @Test
    public void testGetCovariances() {
        // Test case for getting the covariance matrix
        // Testing for a valid covariance matrix
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix result = dist.getCovariances();
        assertTrue(Equals(covariances, result.getData()));
    }

    @Test
    public void testDensity() {
        // Test case for computing density at given values
        // Testing for a valid density result
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 1.0};
        double result = dist.density(values);
        assertEquals(0.05854983152431917, result, 1e-15);
    }

    @Test
    public void testSample() {
        // Test case for generating a sample
        // Testing for a valid sample with correct dimensions
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        assertEquals(2, sample.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatch() {
        // Test case for dimension mismatch in density computation
        // Testing for a DimensionMismatchException
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0};
        dist.density(values);
    }

    // Add more test cases for edge cases and error scenarios

}