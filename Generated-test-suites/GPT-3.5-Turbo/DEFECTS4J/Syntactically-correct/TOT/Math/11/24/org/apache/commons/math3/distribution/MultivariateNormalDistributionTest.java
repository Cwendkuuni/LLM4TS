import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultivariateNormalDistributionTest {

    // Test the constructor with valid input
    @Test
    public void testConstructor_ValidInput() {
        double[] means = {1.5, 2.5, 3.5};
        double[][] covariances = {{1.0, 0.5, 0.1}, {0.5, 2.0, 0.3}, {0.1, 0.3, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.assertEquals(means, distribution.getMeans());
            Assert.assertEquals(covariances, distribution.getCovariances().getData());
            Assert.assertArrayEquals(Math.sqrt(distribution.getCovariances().getData()[0][0]),
                    distribution.getStandardDeviations(), 1e-9);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test the constructor with invalid input where dimension of means is not equal to dimension of covariance matrix
    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_InvalidInput_DimensionMismatch() {
        double[] means = {1.5, 2.5, 3.5};
        double[][] covariances = {{1.0, 0.5, 0.1}, {0.5, 2.0, 0.3}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with invalid input where the covariance matrix is not positive definite
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_InvalidInput_NonPositiveDefiniteMatrix() {
        double[] means = {1.5, 2.5, 3.5};
        double[][] covariances = {{1.0, 0.5, 0.1}, {0.5, -2.0, 0.3}, {0.1, 0.3, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the density method with valid input
    @Test
    public void testDensity_ValidInput() {
        double[] means = {1.5, 2.5};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        double[] vals = {1.0, 2.0};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double expected = (1 / (2 * Math.PI * Math.sqrt(0.75))) * FastMath.exp(-0.5 * 0.5);
            Assert.assertEquals(expected, distribution.density(vals), 1e-9);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test the density method with invalid input where the dimension of input array is not equal to the dimension of the distribution
    @Test(expected = DimensionMismatchException.class)
    public void testDensity_InvalidInput_DimensionMismatch() {
        double[] means = {1.5, 2.5};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        double[] vals = {1.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        distribution.density(vals);
    }

    // Test the sample method
    @Test
    public void testSample() {
        double[] means = {1.5, 2.5};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new 
        MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        Assert.assertEquals(means.length, sample.length);
        for (int i = 0; i < means.length; i++) {
            Assert.assertTrue(sample[i] >= Double.NEGATIVE_INFINITY && sample[i] <= Double.POSITIVE_INFINITY);
        }
    }

    // Test the getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.5, 2.5};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expected = {Math.sqrt(1.0), Math.sqrt(2.0)};
        double[] actual = distribution.getStandardDeviations();
        Assert.assertArrayEquals(expected, actual, 1e-9);
    }

}