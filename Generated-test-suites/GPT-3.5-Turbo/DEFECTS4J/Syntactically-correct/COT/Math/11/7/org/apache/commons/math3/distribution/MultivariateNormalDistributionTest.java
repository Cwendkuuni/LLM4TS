import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    // Test constructor with valid inputs
    @Test
    public void testConstructorValidInputs() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Constructor threw an exception with valid inputs");
        }
    }

    // Test constructor with invalid inputs
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorInvalidInputs() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
    }

    // Test density method
    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        double[] vals = {0.5, 0.5};
        double density = dist.density(vals);
        double expectedDensity = 0.09653235263005393;
        Assert.assertEquals(expectedDensity, density, 1e-9);
    }

    // Test getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        double[] standardDeviations = dist.getStandardDeviations();
        double[] expectedStandardDeviations = {1.0, 1.0};
        Assert.assertArrayEquals(expectedStandardDeviations, standardDeviations, 1e-9);
    }

    // Test sample method
    @Test
    public void testSample() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        double[] sample = dist.sample();
        Assert.assertEquals(2, sample.length);
    }

    // Test constructor with invalid input matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 2.0}, {2.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
    }
}