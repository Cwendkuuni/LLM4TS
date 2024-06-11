import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            assertNotNull(distribution);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);

            double[] values = {1.0, 1.0};
            double density = distribution.density(values);

            assertEquals(0.05854983152431917, density, 1e-10);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Add more test cases for other methods

}