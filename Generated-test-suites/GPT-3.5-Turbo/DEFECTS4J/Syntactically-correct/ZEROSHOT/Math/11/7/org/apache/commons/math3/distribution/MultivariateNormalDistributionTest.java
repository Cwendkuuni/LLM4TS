import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;

public class MultivariateNormalDistributionTest {

    @Test(expected = DimensionMismatchException.class)
    public void testWrongMeansDimension() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testWrongCovariancesDimension() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeEigenvalue() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, -1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1, 1};
        double density = mnd.density(vals);
        assertEquals(0.058549, density, 1e-6);
    }

    @Test
    public void testGetStandardDeviations() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] std = mnd.getStandardDeviations();
        assertEquals(1, std[0], 1e-6);
        assertEquals(1, std[1], 1e-6);
    }

    @Test
    public void testSample() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] sample = mnd.sample();
        assertEquals(2, sample.length);
    }

    @Test
    public void testGetMeans() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] meansResult = mnd.getMeans();
        assertArrayEquals(means, meansResult, 1e-6);
    }

    @Test
    public void testGetCovariances() throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[][] covResult = mnd.getCovariances().getData();
        assertArrayEquals(covariances, covResult);
    }

}