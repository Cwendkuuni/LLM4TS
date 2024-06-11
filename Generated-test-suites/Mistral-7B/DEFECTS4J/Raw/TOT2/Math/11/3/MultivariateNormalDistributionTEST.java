import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor_ValidInput() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, distribution.getMeans());
        RealMatrix covarianceMatrix = distribution.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        Assert.assertEquals(expectedCovarianceMatrix, covarianceMatrix);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_InvalidMeansLength() {
        final double[] means = new double[]{0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_InvalidCovariancesRows() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_InvalidCovariancesColumns() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructor_SingularCovarianceMatrix() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{0.0, 0.5}, {0.5, 0.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testDensity() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final double[] input = new double[]{-1.0, -1.0};
        final double expectedDensity = 0.112740975e-06;
        Assert.assertEquals(expectedDensity, distribution.density(input), 1e-6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensity_InvalidInputLength() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0}, new double[][]{});
        distribution.density(new double[]{0.0});
    }

    @Test
    public void testSample() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final double[] output = distribution.sample();
        RealMatrix covarianceMatrix = distribution.getCovariances();
        RealMatrix expectedOutputCovarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {0.5}});
        expectedOutputCovarianceMatrix = expectedOutputCovarianceMatrix.transpose().multiply(covarianceMatrix);
        RealArray2D expectedOutput = new Array2DRowRealMatrix(new double[][]{{0.0}, {0.0}}).add(means);
        Assert.assertArrayEquals(expectedOutput.getData(), output, 1e-6);
        Assert.assertEquals(expectedOutputCovarianceMatrix, distribution.getCovariances().multiply(expectedOutputCovarianceMatrix));
    }

    @Test
    public void testSample_MultipleSamples() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final int numberOfSamples = 100;
        double[] samples = new double[numberOfSamples][covariances.length];
        for (int i = 0; i < numberOfSamples; i++) {
            samples[i] = distribution.sample();
        }
        RealMatrix expectedSamplesCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        Assert.assertEquals(expectedSamplesCovarianceMatrix, distribution.getCovariances().multiply(expectedSamplesCovarianceMatrix).multiply(distribution.getCovariances()));
    }
}