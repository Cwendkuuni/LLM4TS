import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    // Test the constructor with valid input
    @Test
    public void testValidConstructor() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 2.0}, {2.0, 3.0}};

        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

            Assert.assertArrayEquals(means, distribution.getMeans(), 0.0001);
            Assert.assertArrayEquals(covariances, distribution.getCovariances().getData(), 0.0001);
        } catch (DimensionMismatchException e) {
            Assert.fail("DimensionMismatchException should not be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("NonPositiveDefiniteMatrixException should not be thrown");
        }
    }

    // Test the constructor with invalid means array length
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorMeansLength() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 2.0}, {2.0, 3.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with invalid covariances array lengths
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorCovariancesLengths() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with invalid covariances array lengths of inner arrays
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorCovariancesInnerLengths() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 2.0}, {2.0, 3.0, 4.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with invalid covariances not being square matrix
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorCovariancesNotSquareMatrix() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with invalid covariances being singular matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidConstructorCovariancesSingularMatrix() throws NonPositiveDefiniteMatrixException, DimensionMismatchException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 1.0}, {1.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the density method
    @Test
    public void testDensity() throws DimensionMismatchException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] vals = {1.0, 2.0};

        double density = distribution.density(vals);

        Assert.assertEquals(0.159154943, density, 0.000000001);
    }

    // Test the density method with invalid length of input array
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidLength() throws DimensionMismatchException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] vals = {1.0, 2.0, 3.0};

        distribution.density(vals);
    }

    // Test the getStandardDeviations method
    @Test
    public void testGetStandardDeviations() throws DimensionMismatchException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] std = distribution.getStandardDeviations();

        Assert.assertArrayEquals(new double[]{1.0, 1.0}, std, 0.0001);
    }

    // Test the sample method
    @Test
    public void testSample() throws DimensionMismatchException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();

        Assert.assertEquals(2, sample.length);
    }
}