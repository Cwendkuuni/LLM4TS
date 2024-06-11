import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        try {
            double[] means = {1.0, 2.0};
            double[][] covariances = {{2.0, 0.5}, {0.5, 3.0}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            assertEquals(2, distribution.getDimension());
        } catch (Exception e) {
            fail("Exception thrown during constructor test: " + e.getMessage());
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] point = {1.5, 2.5};
        double density = distribution.density(point);
        assertTrue(density > 0);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();
        assertEquals(2, sample.length);
    }

    @Test
    public void testInvalidConstructor() {
        try {
            double[] means = {1.0, 2.0};
            double[][] covariances = {{2.0, 0.5}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Exception not thrown for inconsistent arrays lengths");
        } catch (Exception e) {
            // Expected exception
        }
    }

    // Add more test methods for testing edge cases and error scenarios if needed

}