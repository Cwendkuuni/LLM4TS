import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        // Test with identity covariance matrix
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix inverse = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix matrix = dist.getCovariances();
        Assert.assertEquals(matrix, inverse.transpose().multiply(inverse));

        // Test with non-identity covariance matrix
        means = new double[]{0.0, 1.0};
        covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(means, covariances);
            RealMatrix inverseMatrix = new EigenDecomposition(dist.getCovariances()).getSolver().getInverse();
            RealMatrix matrix2 = dist.getCovariances();
            Assert.assertEquals(inverseMatrix.transpose().multiply(inverseMatrix), matrix2);
        } catch (Exception e) {
            // Test for exceptions
            Assert.assertTrue(e instanceof SingularMatrixException || e instanceof DimensionMismatchException || e instanceof NonPositiveDefiniteMatrixException);
        }

        // Test mean and standard deviation
        means = new double[]{1.0, 1.0};
        covariances = new double[][]{{2.0, 1.0}, {1.0, 2.0}};
        try {
            dist = new MultivariateNormalDistribution(means, covariances);
            double[] std = dist.getStandardDeviations();
            Assert.assertArrayEquals(new double[]{Math.sqrt(2.0), Math.sqrt(2.0)}, std);
            RealVector mean = dist.getParameters();
            Assert.assertEquals(mean, new Array2DRowRealMatrix(means).transpose());
        } catch (Exception e) {
            // Test for exceptions
            Assert.assertTrue(e instanceof SingularMatrixException || e instanceof DimensionMismatchException || e instanceof NonPositiveDefiniteMatrixException);
        }

        // Test density
        means = new double[]{0.0, 0.0};
        covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        try {
            dist = new MultivariateNormalDistribution(means, covariances);
            double[] testValues = new double[]{1.0, 1.0};
            double density = dist.density(testValues);
            double expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(dist.getCovariances().determinant(), -0.5) * getExponentTerm(testValues);
            Assert.assertEquals(expectedDensity, density, 1e-8);
        } catch (Exception e) {
            // Test for exceptions
            Assert.assertTrue(e instanceof SingularMatrixException || e instanceof DimensionMismatchException || e instanceof NonPositiveDefiniteMatrixException);
        }

        // Test sample
        means = new double[]{0.0, 0.0};
        covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        try {
            dist = new MultivariateNormalDistribution(means, covariances);
            double[] sample = dist.sample();
            double[] meansCopy = dist.getMeans().clone();
            for (int i = 0; i < sample.length; i++) {
                Assert.assertEquals(meansCopy[i], sample[i], 1e-8);
            }
        } catch (Exception e) {
            // Test for exceptions
            Assert.assertTrue(e instanceof SingularMatrixException || e instanceof DimensionMismatchException || e instanceof NonPositiveDefiniteMatrixException);
        }
    }

    private double getExponentTerm(double[] values) {
        final int dim = 2;
        double sum = 0;
        double[] centered = new double[dim];
        for (int i = 0; i < dim; i++) {
            centered[i] = values[i] - dist.getMeans()[i];
            sum += centered[i] * centered[i];
        }
        return Math.exp(-0.5 * sum);
    }
}