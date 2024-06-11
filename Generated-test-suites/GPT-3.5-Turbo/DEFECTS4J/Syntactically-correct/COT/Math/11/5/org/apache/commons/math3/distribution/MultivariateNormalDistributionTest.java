import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-6;
    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();

        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {0, 0};
        double[] actual = distribution.getMeans();

        Assert.assertArrayEquals(expected, actual, EPSILON);
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {{1, 0}, {0, 1}};
        double[][] actual = distribution.getCovariances().getData();

        Assert.assertArrayEquals(expected[0], actual[0], EPSILON);
        Assert.assertArrayEquals(expected[1], actual[1], EPSILON);
    }

    @Test
    public void testDensity() {
        double[] values = {0, 0};
        double expected = 0.15915494309189535;
        double actual = distribution.density(values);

        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatch() {
        double[] values = {0};
        distribution.density(values);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1, 1};
        double[] actual = distribution.getStandardDeviations();

        Assert.assertArrayEquals(expected, actual, EPSILON);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertEquals(2, sample.length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidDimensionForRandomGenerator() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        distribution.sample();
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMeansLength() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {0};
        double[][] covariances = {{1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesLength() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}, {0, 0}};
        RandomGenerator rng = new Well19937c();
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesDimensions() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}};
        RandomGenerator rng = new Well19937c();
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidCovariancesEigenvalues() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {0, 0};
        double[][] covariances = {{-1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }
}

