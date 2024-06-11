import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;

public class MultivariateNormalDistributionTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        try {
            double[] means = {1, 2, 3};
            double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            assertEquals(means, distribution.getMeans());
            assertEquals(covariances, distribution.getCovariances().getData());
        } catch (Exception e) {
            fail("Constructor test failed: " + e.getMessage());
        }
    }

    // Test case for density method
    @Test
    public void testDensity() {
        try {
            double[] means = {0, 0};
            double[][] covariances = {{1, 0}, {0, 1}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] values = {0, 0};
            double density = distribution.density(values);
            assertEquals(0.15915494309189535, density, 0);
        } catch (Exception e) {
            fail("Density test failed: " + e.getMessage());
        }
    }

    // Custom test case for density method with invalid dimension
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidDimension() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {0};
        distribution.density(values);
    }

    // Test case for sample method
    @Test
    public void testSample() {
        try {
            double[] means = {0, 0};
            double[][] covariances = {{1, 0}, {0, 1}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] sample = distribution.sample();
            assertEquals(2, sample.length);
        } catch (Exception e) {
            fail("Sample test failed: " + e.getMessage());
        }
    }

    // Test case for getMeans method
    @Test
    public void testGetMeans() {
        try {
            double[] means = {1, 2, 3};
            double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] result = distribution.getMeans();
            assertEquals(means, result);
        } catch (Exception e) {
            fail("GetMeans test failed: " + e.getMessage());
        }
    }

    // Test case for getCovariances method
    @Test
    public void testGetCovariances() {
        try {
            double[] means = {1, 2, 3};
            double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[][] result = distribution.getCovariances().getData();
            assertEquals(covariances, result);
        } catch (Exception e) {
            fail("GetCovariances test failed: " + e.getMessage());
        }
    }

    // Test case for getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        try {
            double[] means = {1, 2, 3};
            double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] result = distribution.getStandardDeviations();
            double[] expected = {1, 1, 1};
            assertArrayEquals(expected, result, 0);
        } catch (Exception e) {
            fail("GetStandardDeviations test failed: " + e.getMessage());
        }
    }
    
    // Custom test case for constructor with invalid dimensions
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorInvalidDimensions() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    // Custom test case for constructor with negative eigenvalue
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNegativeEigenvalue() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    // Custom test case for constructor with singular matrix
    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 1, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

}