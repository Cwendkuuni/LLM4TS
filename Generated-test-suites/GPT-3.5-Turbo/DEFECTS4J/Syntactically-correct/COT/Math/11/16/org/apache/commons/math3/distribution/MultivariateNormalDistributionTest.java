import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import java.util.Arrays;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.getMeans();
        Assert.assertTrue(Arrays.equals(result, means));
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[][] result = distribution.getCovariances().getData();
        Assert.assertTrue(Arrays.deepEquals(result, covariances));
    }

    @Test
    public void testDensity() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {0, 0, 0};
        double result = distribution.density(vals);
        Assert.assertEquals(result, 0.0029150244650281946, 0.0000000000000001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.getStandardDeviations();
        double[] expected = {1, 1, 1};
        Assert.assertTrue(Arrays.equals(result, expected));
    }

    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.sample();
        Assert.assertEquals(result.length, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
}