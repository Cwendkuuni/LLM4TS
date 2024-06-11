import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class MultivariateNormalDistributionTest {

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 2}, {3, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 1}, {1, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, -1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {0, 0};
        double density = distribution.density(vals);
        // add assertion for density
    }

    @Test
    public void testGetMeans() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.getMeans();
        // add assertion for means
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        RealMatrix result = distribution.getCovariances();
        // add assertion for covariances
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.getStandardDeviations();
        // add assertion for standard deviations
    }

    @Test
    public void testSample() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.sample();
        // add assertion for sample
    }

}