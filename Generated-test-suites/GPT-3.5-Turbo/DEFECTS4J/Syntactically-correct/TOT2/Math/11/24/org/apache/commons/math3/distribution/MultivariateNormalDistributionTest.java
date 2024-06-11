import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            assertNotNull(distribution);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown while constructing MultivariateNormalDistribution");
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covars = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covars);
        
        double[] values = {1.5, 2.5};
        double density = distribution.density(values);
        assertEquals(0.15915494309189535, density, 1e-6);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covars = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covars);

        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(2, sample.length);
    }

    // Add more test cases here to cover edge cases and error scenarios

}