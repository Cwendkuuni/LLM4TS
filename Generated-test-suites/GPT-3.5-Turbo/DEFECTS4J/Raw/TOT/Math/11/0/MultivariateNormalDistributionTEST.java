import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.assertNotNull(distribution);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown.");
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatchException() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrixException() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, -2.0, 0.0}, {0.0, 0.0, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] actualMeans = distribution.getMeans();
            Assert.assertArrayEquals(means, actualMeans, 0.0);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown.");
        }
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[][] actualCovariances = distribution.getCovariances().getData();
            for (int i = 0; i < actualCovariances.length; i++) {
                Assert.assertArrayEquals(covariances[i], actualCovariances[i], 0.0);
            }
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown.");
        }
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] point = {1.0, 2.0, 3.0};
            double density = distribution.density(point);
            Assert.assertEquals(0.009244497, density, 1e-6);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown.");
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] expectedStandardDeviations = {1.0, Math.sqrt(2.0), Math.sqrt(3.0)};
            double[] actualStandardDeviations = distribution.getStandardDeviations();
            Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0.0);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown.");
        }
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] sample = distribution.sample();
            Assert.assertEquals(3, sample.length);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown.");
        }
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            double[] point = {1.0, 2.0, 3.0};
            double exponentTerm = distribution.getExponentTerm(point);
            Assert.assertEquals(1.0, exponentTerm, 0.0);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown.");
        }
    }
}