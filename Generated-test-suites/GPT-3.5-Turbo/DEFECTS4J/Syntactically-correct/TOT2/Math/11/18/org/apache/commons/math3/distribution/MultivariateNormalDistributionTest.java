import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor()
    {
        // Test constructor with valid input
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            assertNotNull(mnd);
        } catch (Exception e) {
            fail("Constructor should not throw any exceptions with valid input");
        }

        // Test constructor with invalid input
        double[] invalidMeans = {1.0, 2.0};
        double[][] invalidCovariances = {{1.0, 0.0}, {0.0, 2.0}};
        try {
            MultivariateNormalDistribution mndInvalid = new MultivariateNormalDistribution(invalidMeans, invalidCovariances);
            fail("Constructor should throw DimensionMismatchException with invalid input");
        } catch (Exception e) {
            assertTrue(e instanceof DimensionMismatchException);
        }
    }

    @Test
    public void testDensity() {
        // Test density calculation with valid input
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {1.5, 2.5};
            double density = mnd.density(vals);
            assertEquals(0.04202444626086197, density, 1e-15);
        } catch (Exception e) {
            fail("Density calculation should not throw any exceptions with valid input");
        }

        // Test density calculation with invalid input
        double[] invalidVals = {1.5};
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double density = mnd.density(invalidVals);
            fail("Density calculation should throw DimensionMismatchException with invalid input");
        } catch (Exception e) {
            assertTrue(e instanceof DimensionMismatchException);
        }
    }

    @Test
    public void testSample() {
        // Test sample generation
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] sample = mnd.sample();
            assertNotNull(sample);
            assertEquals(2, sample.length);
        } catch (Exception e) {
            fail("Sample generation should not throw any exceptions with valid input");
        }
    }

}