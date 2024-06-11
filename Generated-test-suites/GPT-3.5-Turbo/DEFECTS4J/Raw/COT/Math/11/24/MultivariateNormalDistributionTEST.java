import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        assertArrayEquals(means, distribution.getMeans(), 0);
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = {{1.0, 0.0}, {0.0, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0, 3.0}, covariances);
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);
        assertEquals(covarianceMatrix, distribution.getCovariances());
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1.0, 2.0};
        assertEquals(0.072114, distribution.density(vals), 1e-6);
    }

    @Test
    public void testGetStandardDeviations() {
        double[][] covariances = {{1.0, 0.0}, {0.0, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0, 3.0}, covariances);
        double[] standardDeviations = distribution.getStandardDeviations();
        double[] expected = {1.0, Math.sqrt(2.0)};
        assertArrayEquals(expected, standardDeviations, 1e-6);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertEquals(sample.length, means.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 2.0};
        double exponentTerm = distribution.getExponentTerm(values);
        assertEquals(0.606530, exponentTerm, 1e-6);
    }
}