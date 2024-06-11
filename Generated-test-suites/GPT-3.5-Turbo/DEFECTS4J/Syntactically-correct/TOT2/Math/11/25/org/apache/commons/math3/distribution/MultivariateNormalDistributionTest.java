import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    // Test case for constructor with correct input
    @Test
    public void testConstructor() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            assertNotNull(distribution);
        } catch (Exception e) {
            fail("Constructor threw an exception");
        }
    }

    // Test case for constructor with incorrect input (dimension mismatch)
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}, {1.0, 0.5}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test case for density calculation
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] point = {1.5, 2.5};
        double density = distribution.density(point);

        assertEquals(0.05520157, density, 1e-6);
    }

    // Test case for sample generation
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();
        assertEquals(2, sample.length);
    }
}