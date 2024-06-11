import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = new double[] {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        assertArrayEquals(means, distribution.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[] means = new double[] {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(covariances, distribution.getCovariances().getData());
    }

    @Test
    public void testDensity() {
        double[] means = new double[] {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double density = distribution.density(new double[] {0.0, 0.0});
        assertEquals(0.15915494309189535, density, 0.0);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = distribution.getStandardDeviations();
        assertArrayEquals(new double[] {1.0, 1.0}, stdDeviations, 0.0);
    }

    @Test
    public void testSample() {
        double[] means = new double[] {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sampled = distribution.sample();
        assertNotNull(sampled);
    }
}