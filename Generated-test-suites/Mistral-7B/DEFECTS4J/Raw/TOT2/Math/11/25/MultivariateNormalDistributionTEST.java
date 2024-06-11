import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionCreation() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        Assert.assertArrayEquals(means, distribution.getMeans());
        RealMatrix covarianceMatrix = distribution.getCovariances();
        Assert.assertEquals(covariances[0].length, covarianceMatrix.getRowDimension());
        Assert.assertEquals(covariances.length, covarianceMatrix.getColumnDimension());
        Assert.assertArrayEquals(covariances[0], covarianceMatrix.getColumnVector(0).toArray());
        Assert.assertArrayEquals(covariances[1], covarianceMatrix.getColumnVector(1).toArray());
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] testData = {2.5, 3.5};
        double expected = FastMath.pow(2 * FastMath.PI, -2.0) *
                FastMath.pow(distribution.covarianceMatrixDeterminant, -0.5) *
                distribution.getExponentTerm(testData);
        double actual = distribution.density(testData);
        Assert.assertEquals(expected, actual, 1e-12);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        for (int i = 0; i < sample.length; i++) {
            Assert.assertNotSame(means[i], sample[i], 1e-6);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] means = {1.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5},
                {0.1, 0.2}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {0.0, 0.5},
                {0.5, 0.0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {-1.0, 0.5},
                {0.5, -2.5}
        };
        new MultivariateNormalDistribution(means, covariances);
    }
}