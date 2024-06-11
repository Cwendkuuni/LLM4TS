import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.1}, {0.5, 0.1, 4.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] result = distribution.getMeans();

        assertArrayEquals(result, means, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.1}, {0.5, 0.1, 4.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        RealMatrix result = distribution.getCovariances();

        assertEquals(result.getColumnDimension(), covariances[0].length);
        assertEquals(result.getRowDimension(), covariances.length);
        for (int i = 0; i < result.getRowDimension(); i++) {
            assertArrayEquals(result.getRow(i), covariances[i], 0.0001);
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.1}, {0.5, 0.1, 4.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] vals = {1.5, 2.5, 3.5};
        double result = distribution.density(vals);

        assertEquals(result, 0.0393776, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.1}, {0.5, 0.1, 4.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] result = distribution.getStandardDeviations();

        double[] expected = {1.4142, 1.7321, 2.0000};

        assertArrayEquals(result, expected, 0.0001);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.1}, {0.5, 0.1, 4.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] result = distribution.sample();

        assertEquals(result.length, means.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0, 0.5}, {1.0, 3.0, 0.1}, {0.5, 0.1, 4.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] vals = {1.5, 2.5, 3.5};
        double result = distribution.getExponentTerm(vals);

        assertEquals(result, 0.3134304, 0.0001);
    }

}