import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expectedMeans = {1.0, 2.0, 3.0};
        double[] actualMeans = distribution.getMeans();
        assertArrayEquals(expectedMeans, actualMeans, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        RealMatrix actualCovariances = distribution.getCovariances();
        assertEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1.0, 2.0, 3.0};
        double expectedDensity = 0.06349363593424099;
        double actualDensity = distribution.density(vals);
        assertEquals(expectedDensity, actualDensity, 0.0);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expectedStandardDeviations = {1.0, 1.0, 1.0};
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0.0);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertEquals(3, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 2.0, 3.0};
        double expectedExponentTerm = 1.0;
        double actualExponentTerm = distribution.getExponentTerm(values);
        assertEquals(expectedExponentTerm, actualExponentTerm, 0.0);
    }
}