import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expectedMeans = {1.0, 2.0, 3.0};
        double[] actualMeans = distribution.getMeans();
        Assert.assertArrayEquals(expectedMeans, actualMeans, DELTA);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[][] expectedCovariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        double[][] actualCovariances = distribution.getCovariances().getData();
        for (int i = 0; i < expectedCovariances.length; i++) {
            Assert.assertArrayEquals(expectedCovariances[i], actualCovariances[i], DELTA);
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {0.0, 0.0};
        double expectedDensity = 0.0585498315;
        double actualDensity = distribution.density(values);
        Assert.assertEquals(expectedDensity, actualDensity, DELTA);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expectedStd = {1.0, 1.0, 1.0};
        double[] actualStd = distribution.getStandardDeviations();
        Assert.assertArrayEquals(expectedStd, actualStd, DELTA);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        Assert.assertEquals(2, sample.length);
    }

    @Test
    public void testConstructorInvalidMeanLength() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Expected DimensionMismatchException to be thrown.");
        } catch (DimensionMismatchException e) {
            Assert.assertEquals("Dimension mismatch: 2 != 3", e.getMessage());
        }
    }

    @Test
    public void testConstructorInvalidCovariancesLength() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Expected DimensionMismatchException to be thrown.");
        } catch (DimensionMismatchException e) {
            Assert.assertEquals("Dimension mismatch: 3 != 2", e.getMessage());
        }
    }

    @Test
    public void testConstructorInvalidCovariancesLengthInner() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0}, {0.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Expected DimensionMismatchException to be thrown.");
        } catch (DimensionMismatchException e) {
            Assert.assertEquals("Dimension mismatch: 0 != 2", e.getMessage());
        }
    }

    @Test
    public void testConstructorSingularMatrix() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 1.0}, {1.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Expected SingularMatrixException to be thrown.");
        } catch (SingularMatrixException e) {
            Assert.assertEquals("Singular matrix", e.getMessage());
        }
    }

    @Test
    public void testConstructorNegativeEigenvalues() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Expected NonPositiveDefiniteMatrixException to be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("Negative eigenvalue in matrix index [0][0]: -1.0", e.getMessage());
        }
    }

}