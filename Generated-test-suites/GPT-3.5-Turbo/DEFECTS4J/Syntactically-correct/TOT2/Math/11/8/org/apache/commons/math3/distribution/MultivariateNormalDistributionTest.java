import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] expectedMeans = {1.0, 2.0};
        assertArrayEquals(expectedMeans, distribution.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[][] expectedCovariances = {{1.0, 0.0}, {0.0, 1.0}};
        assertArrayEquals(expectedCovariances, distribution.getCovariances().getData(), 0.0);
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] values = {1.0, 2.0};
        double expectedDensity = 0.15915494309189535;
        assertEquals(expectedDensity, distribution.density(values), 1e-10);
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] expectedStandardDeviations = {1.0, 1.0};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0.0);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] sample = distribution.sample();
        assertNotNull(sample);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidDimensions() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] values = {1.0};
        distribution.density(values);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
    }

}