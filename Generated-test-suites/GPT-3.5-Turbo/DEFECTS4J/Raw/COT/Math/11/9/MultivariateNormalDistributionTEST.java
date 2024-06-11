import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private double DELTA = 1e-15;

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        assertArrayEquals(means, distribution.getMeans(), DELTA);
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0, 3.0}, covariances);
        assertArrayEquals(covariances, distribution.getCovariances().getData(), DELTA);
    }

    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {0.5, 0.0};
        assertEquals(0.159154943, distribution.density(values), DELTA);
    }

    @Test
    public void testGetStandardDeviations() {
        double[][] covariances = {{1.0, 0.0}, {0.0, 4.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, covariances);
        double[] standardDeviations = {1.0, 2.0};
        assertArrayEquals(standardDeviations, distribution.getStandardDeviations(), DELTA);
    }

    @Test
    public void testSample() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertEquals(2, sample.length);
    }

    @Test
    public void testWithInvalidDimensions() {
        try {
            double[] means = {1.0, 2.0, 3.0};
            double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException ex) {
            // success
        }
    }

    @Test
    public void testWithNegativeEigenvalue() {
        try {
            double[] means = {0.0, 0.0};
            double[][] covariances = {{1.0, 0.0}, {0.0, -1.0}};
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException ex) {
            // success
        }
    }
}