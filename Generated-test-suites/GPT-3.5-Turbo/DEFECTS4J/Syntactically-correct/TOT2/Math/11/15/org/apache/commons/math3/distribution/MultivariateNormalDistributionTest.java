import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private static final double[] MEANS = {1.0, 2.0};
    private static final double[][] COVARIANCES = {{1.0, 0.5}, {0.5, 2.0}};

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        distribution = new MultivariateNormalDistribution(MEANS, COVARIANCES);
    }

    @Test
    public void testGetMeans() {
        double[] means = distribution.getMeans();
        assertArrayEquals(MEANS, means, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = distribution.getCovariances().getData();
        assertArrayEquals(COVARIANCES, covariances);
    }

    @Test
    public void testDensity() {
        double[] values = {1.5, 3.0};
        double density = distribution.density(values);
        assertEquals(0.0952775, density, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] stdDeviations = distribution.getStandardDeviations();
        double[] expected = {1.0, Math.sqrt(2.0)};
        assertArrayEquals(expected, stdDeviations, 0.0);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertEquals(distribution.getDimension(), sample.length);
        assertNotNull(sample);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithWrongDimension() {
        double[] values = {1.5};
        distribution.density(values);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithMismatchedDimension() {
        double[] values = {1.5, 3.0, 2.5};
        distribution.density(values);
    }

    @Test(expected = SingularMatrixException.class)
    public void testDistributionWithSingularMatrix() {
        double[][] singularCovariances = {{1.0, 1.0}, {1.0, 1.0}};
        new MultivariateNormalDistribution(MEANS, singularCovariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDistributionWithNonPositiveDefiniteMatrix() {
        double[][] nonPositiveDefiniteCovariances = {{1.0, 0.0}, {0.0, -1.0}};
        new MultivariateNormalDistribution(MEANS, nonPositiveDefiniteCovariances);
    }
}