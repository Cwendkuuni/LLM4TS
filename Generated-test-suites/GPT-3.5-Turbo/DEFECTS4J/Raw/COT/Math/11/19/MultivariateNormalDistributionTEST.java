import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {

    // Test density method
    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 1.0};
        double result = distribution.density(values);
        assertEquals(0.058549831524319, result, 1e-9);
    }

    // Test getMeans method
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.getMeans();
        assertTrue(MathArrays.equals(means, result));
    }

    // Test getCovariances method
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        RealMatrix result = distribution.getCovariances();
        double[][] expected = {{1.0, 0.0}, {0.0, 1.0}};
        assertTrue(MathArrays.equals(expected, result.getData()));
    }

    // Test getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.getStandardDeviations();
        double[] expected = {1.0, 1.0};
        assertTrue(MathArrays.equals(expected, result));
    }

    // Test sample method
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] result = distribution.sample();
        assertNotNull(result);
    }

    // Test for Exception - DimensionMismatchException
    @Test (expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0};
        distribution.density(values);
    }

    // Test for Exception - NonPositiveDefiniteMatrixException
    @Test (expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{-1.0, 0.0}, {0.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    // Test for Exception - DimensionMismatchException
    @Test (expected = DimensionMismatchException.class)
    public void testDimensionMismatchException2() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 1.0}, {0.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    // Test for Exception - SingularMatrixException
    @Test (expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

}