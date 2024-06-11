import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructorAndGetters() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            assertArrayEquals(means, distribution.getMeans(), 0.0001);
            assertTrue(distribution.getCovariances().getEntry(0, 0) == 1.0);
            assertTrue(distribution.getCovariances().getEntry(1, 1) == 1.0);
            assertTrue(distribution.getCovariances().getEntry(2, 2) == 1.0);
        } catch (Exception e) {
            fail("Exception thrown in constructor or getter methods");
        }
    }

    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1, 0.5}, {0.5, 1}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            double density = distribution.density(new double[]{0.5, 0.5});

            assertTrue(density > 0.0);
        } catch (Exception e) {
            fail("Exception thrown in density method");
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithDimensionMismatch() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1, 0.5}, {0.5, 1}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        distribution.density(new double[]{0.5});
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}