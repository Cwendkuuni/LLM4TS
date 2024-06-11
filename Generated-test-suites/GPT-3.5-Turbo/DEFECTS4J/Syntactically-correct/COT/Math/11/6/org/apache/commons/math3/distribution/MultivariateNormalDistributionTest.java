import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    //Test case for constructor 1
    @Test
    public void testConstructor1() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            // Check if the means are correctly set
            Assert.assertArrayEquals(means, distribution.getMeans(), 0);

            // Check if the covariance matrix is correctly set
            RealMatrix expectedCovarianceMatrix = distribution.getCovariances();
            for (int i = 0; i < covariances.length; i++) {
                Assert.assertArrayEquals(covariances[i], expectedCovarianceMatrix.getRow(i), 0);
            }
        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected DimensionMismatchException");
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected Exception");
        }
    }

    //Test case for constructor 2
    @Test
    public void testConstructor2() {
        double[] means = {-2, 5};
        double[][] covariances = {{15, 11}, {11, 14}};
        RandomGenerator rng = null;
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);

            // Check if the means are correctly set
            Assert.assertArrayEquals(means, distribution.getMeans(), 0);

            // Check if the covariance matrix is correctly set
            RealMatrix expectedCovarianceMatrix = distribution.getCovariances();
            for (int i = 0; i < covariances.length; i++) {
                Assert.assertArrayEquals(covariances[i], expectedCovarianceMatrix.getRow(i), 0);
            }
        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected DimensionMismatchException");
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected Exception");
        }
    }

    //Test case for density method
    @Test
    public void testDensity() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            double[] vals = {0, 0, 0};
            double expectedDensity = 1.0 / (2 * Math.PI);
            Assert.assertEquals(expectedDensity, distribution.density(vals), 0);

            double[] vals2 = {-1, 0, 1};
            double expectedDensity2 = 1.0 / (2 * Math.PI);
            Assert.assertEquals(expectedDensity2, distribution.density(vals2), 0);

        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected DimensionMismatchException");
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected Exception");
        }
    }

    //Test case for getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            double[] expectedStandardDeviations = {1, 1, 1};
            Assert.assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0);

        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected DimensionMismatchException");
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected Exception");
        }
    }

    //Test case for sample method
    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            double[] sample = distribution.sample();

            Assert.assertEquals(distribution.getDimension(), sample.length);

        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected DimensionMismatchException");
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected Exception");
        }
    }
}