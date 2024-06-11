import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        // Test case for getting the mean vector
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] result = distribution.getMeans();
        assertArrayEquals(means, result, 0.0);
    }

    @Test
    public void testDensity() {
        // Test case for calculating density at given values
        double[] means = {0.0, 0.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double result = distribution.density(new double[]{0.5, 0.5});
        assertEquals(0.07365482304005679, result, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        // Test case for getting the standard deviations
        double[] means = {0.0, 0.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 4.0}});
        double[] result = distribution.getStandardDeviations();
        assertArrayEquals(new double[]{1.0, 2.0}, result, 0.0);
    }

    @Test
    public void testSample() {
        // Test case for generating a sample from the distribution
        double[] means = {0.0, 0.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] sample = distribution.sample();
        assertNotNull(sample);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDensity() {
        // Test case for invalid input dimension in density calculation
        double[] means = {0.0, 0.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 4.0}});
        distribution.density(new double[]{0.5});
    }

}