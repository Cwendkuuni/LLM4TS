import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private final double[] means = {1, 2, 3};
    private final double[][] covariances = {{1, 0, 0}, {0, 4, 2}, {0, 2, 9}};
    private final double[] value = {1.5, 3.5, 2.5};
    private MultivariateNormalDistribution distribution;

    @Before
    public void setup() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        RandomGenerator rng = RandomGeneratorFactory.createRandomGenerator();
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = distribution.getMeans();
        Assert.assertArrayEquals(this.means, means, Precision.EPSILON);
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = distribution.getCovariances().getData();
        Assert.assertArrayEquals(this.covariances, covariances);
    }

    @Test
    public void testDensity() throws DimensionMismatchException {
        double density = distribution.density(value);
        Assert.assertEquals(0.029235692, density, Precision.EPSILON);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] stdDeviations = distribution.getStandardDeviations();
        double[] expectedStdDeviations = {1, 2, 3};
        Assert.assertArrayEquals(expectedStdDeviations, stdDeviations, Precision.EPSILON);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertEquals(3, sample.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidDimension() throws DimensionMismatchException {
        double[] invalidValue = {1.5, 3.5};
        distribution.density(invalidValue);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidDimension2() throws DimensionMismatchException {
        double[] invalidValue = {1.5, 3.5, 2.5, 4.5};
        distribution.density(invalidValue);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[][] singularCovariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution singularDistribution = new MultivariateNormalDistribution(means, singularCovariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[][] nonPosDefCovariances = {{1, 0}, {0, -1}};
        MultivariateNormalDistribution nonPosDefDistribution = new MultivariateNormalDistribution(means, nonPosDefCovariances);
    }
}