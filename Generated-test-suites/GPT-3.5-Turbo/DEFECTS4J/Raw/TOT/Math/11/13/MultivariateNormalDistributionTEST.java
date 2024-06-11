import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
            Assert.assertArrayEquals(means, dist.getMeans(), 0);
            Assert.assertArrayEquals(covariances, dist.getCovariances().getData(), 0);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException ex) {
            Assert.fail("Constructor threw an exception");
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularMatrix() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 1}, {1, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedDimensions() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        double[] means = {0};
        double[][] covariances = {{1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {0};
        Assert.assertEquals(0.3989422804014327, dist.density(vals), 1e-9);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithMismatchedDimensions() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {0};
        dist.density(vals);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] expected = {1, 1, 1};
        Assert.assertArrayEquals(expected, dist.getStandardDeviations(), 0);
    }

    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        Assert.assertEquals(3, sample.length);
    }

    @Test
    public void testSampleSizeZero() {
        double[] means = {1};
        double[][] covariances = {{1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        Assert.assertEquals(0, sample.length);
    }
}