import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.EmpiricalDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0.5}, {0.5, 2}};
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDensity() {
        // Typical use case
        double[] values = {0, 0};
        double density = distribution.density(values);
        assertEquals(0.07957747154594767, density, 1e-15);

        // Edge case with dimension mismatch
        try {
            double[] wrongValues = {0};
            distribution.density(wrongValues);
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testSample() {
        // Typical use case
        double[] sample = distribution.sample();
        assertEquals(2, sample.length);

        // Edge case with invalid covariance matrix
        double[] invalidMeans = {0, 0};
        double[][] invalidCovariances = {{-1, 0}, {0, 2}};
        try {
            distribution = new MultivariateNormalDistribution(invalidMeans, invalidCovariances);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetStandardDeviations() {
        // Typical use case
        double[] stdDeviations = distribution.getStandardDeviations();
        assertEquals(1, stdDeviations[0], 1e-15);
        assertEquals(Math.sqrt(2), stdDeviations[1], 1e-15);
    }

    // Add more test cases for other methods as needed

}