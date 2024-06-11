import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        // Test constructor with mean vector and covariance matrix
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}};

        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            Assert.assertArrayEquals(means, dist.getMeans());
            Assert.assertEquals(covariances, dist.getCovariances().getData());
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test constructor with mean vector and diagonal covariance matrix
        double[] meansDiagonal = {1.0, 2.0, 3.0};
        double[] covariancesDiagonal = {1.0, 2.5, 3.5};

        try {
            MultivariateNormalDistribution distDiagonal = new MultivariateNormalDistribution(new Well19937c(), meansDiagonal, covariancesDiagonal);
            Assert.assertArrayEquals(meansDiagonal, distDiagonal.getMeans());
            RealMatrix covarianceMatrix = distDiagonal.getCovariances();
            Assert.assertTrue(covarianceMatrix.isSymmetric());
            Assert.assertTrue(covarianceMatrix.isDiagonal());
            Assert.assertArrayEquals(covariancesDiagonal, covarianceMatrix.getColumnVector(0));
        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0}, new double[][]{{1.0, 1.0}, {1.0, 1.0}});

        double[] testValues = {0.0, 0.0};
        double density = dist.density(testValues);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(1.0, -1.0) * FastMath.exp(-0.5 * (FastMath.pow(testValues[0] - 0.0, 2.0) + FastMath.pow(testValues[1] - 0.0, 2.0)));
        Assert.assertEquals(expectedDensity, density, 1e-8);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0}, new double[][]{{1.0, 1.0}, {1.0, 1.0}});

        double[] sample = dist.sample();

        for (int i = 0; i < sample.length; i++) {
            double expected = RandomGenerator.getDefault().nextGaussian();
            Assert.assertEquals(expected, sample[i], 1e-8);
        }

        // Check if sample is centered around the mean vector
        RealMatrix inverseCovarianceMatrix = dist.covarianceMatrixInverse;
        double[] means = dist.means;
        double[] sampleCentered = new double[means.length];

        for (int i = 0; i < means.length; i++) {
            sampleCentered[i] = sample[i] - means[i];
        }

        RealMatrix centeredSamples = new Array2DRowRealMatrix(1, sampleCentered.length).setColumnVector(0, sampleCentered);
        RealMatrix product = inverseCovarianceMatrix.multiply(centeredSamples);

        for (int i = 0; i < means.length; i++) {
            double expected = 0.0;
            for (int j = 0; j < means.length; j++) {
                expected += product.getEntry(0, j) * means[j];
            }
            Assert.assertEquals(means[i], expected, 1e-8);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInconsistentArrayLengths() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0}, new double[][]{{1.0, 2.0}, {1.0, 1.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInconsistentRowsAndColumns() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0, 3.0}, new double[][]{{1.0, 1.0}, {1.0, 1.0, 1.0}});
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovarianceMatrix() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{0.0, 0.0}, {0.0, 0.0}});
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteCovarianceMatrix() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0}, new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
    }
}