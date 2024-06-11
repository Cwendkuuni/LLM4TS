import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.ExceptionUtils;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MultivariateNormalDistributionTest {

    // Test constants
    private static final double EPSILON = 1e-10;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testConstructor() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        Assert.assertEquals(2, distribution.getDimension());
        Assert.assertArrayEquals(means, distribution.getMeans(), EPSILON);
        Assert.assertEquals(1.0, distribution.getCovariances().getEntry(0, 0), EPSILON);
        Assert.assertEquals(1.0, distribution.getCovariances().getEntry(1, 1), EPSILON);
    }

    @Test
    public void testConstructor_DimensionMismatchException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0}};

        exception.expect(DimensionMismatchException.class);
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testConstructor_SingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 2.0}, {3.0, 4.0}};

        exception.expect(NonPositiveDefiniteMatrixException.class);
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        double[] values = {1.5, 2.5};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double expectedDensity = 0.1591549431;
        double actualDensity = distribution.density(values);

        Assert.assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    @Test
    public void testDensity_DimensionMismatchException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        double[] values = {1.5};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        exception.expect(DimensionMismatchException.class);
        distribution.density(values);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();

        Assert.assertEquals(2, sample.length);
        Assert.assertTrue(Precision.compareTo(sample[0], 1.0, EPSILON) == 0);
        Assert.assertTrue(Precision.compareTo(sample[1], 2.0, EPSILON) == 0);
    }

    @Test
    public void testSample_RandomGenerator() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        RandomGenerator rng = new Well19937c(12345);
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double[] sample = distribution.sample();

        Assert.assertEquals(2, sample.length);
        Assert.assertTrue(Precision.compareTo(sample[0], 0.41944516, EPSILON) == 0);
        Assert.assertTrue(Precision.compareTo(sample[1], 2.82698918, EPSILON) == 0);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] expectedStdDeviations = {1.0, 1.0};
        double[] actualStdDeviations = distribution.getStandardDeviations();

        Assert.assertArrayEquals(expectedStdDeviations, actualStdDeviations, EPSILON);
    }

    @Test
    public void testGetStandardDeviations_DimensionMismatchException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0, 0.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        exception.expect(DimensionMismatchException.class);
        distribution.getStandardDeviations();
    }

}