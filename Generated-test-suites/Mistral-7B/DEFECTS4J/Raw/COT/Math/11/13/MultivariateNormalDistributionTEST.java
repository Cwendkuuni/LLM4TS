import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructors() {
        final double[] means = new double[]{0.0, 0.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mnd1 = new MultivariateNormalDistribution(means, covariances);
        MultivariateNormalDistribution mnd2 = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        Assert.assertArrayEquals(means, mnd1.getMeans());
        Assert.assertArrayEquals(means, mnd2.getMeans());
        Assert.assertEquals(covariances, mnd1.getCovariances().getData());
        Assert.assertEquals(covariances, mnd2.getCovariances().getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMeansLength() {
        double[] means = new double[]{0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesRows() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = {{1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesColumns() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}, {1.0, 0.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovarianceMatrix() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = {{0.0, 0.5}, {0.5, 0.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeCovarianceMatrix() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});

        double[] testValues = new double[]{0.0, 0.0};
        double density = mnd.density(testValues);
        double expectedDensity = 1.0 / (2 * FastMath.PI * FastMath.SQRT_2) * FastMath.pow(2, -1) * FastMath.E;
        Assert.assertEquals(expectedDensity, density, 1e-12);
    }

    @Test
    public void testDensityNegativeValues() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});

        double[] testValues = new double[]{-1.0, -1.0};
        double density = mnd.density(testValues);
        Assert.assertEquals(0.0, density, 1e-12);
    }

    @Test
    public void testDensityDifferentDimension() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}, {1.0, 0.5}});

        double[] testValues = new double[]{0.0, 0.0, 0.0};
        double density = mnd.density(testValues);
        Assert.assertEquals(0.0, density, 1e-12);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});

        double[] sample = mnd.sample();
        double[] means = mnd.getMeans();
        RealVector covarianceMatrix = mnd.getCovariances();

        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(sample[i] >= -3.0 && sample[i] <= 3.0);
        }

        RealVector expectedSample = covarianceMatrix.operate(new Array2DRowRealMatrix(new double[][]{{FastMath.random(), FastMath.random()},
                {FastMath.random(), FastMath.random()}}).getColumnVector(0));

        for (int i = 0; i < sample.length; i++) {
            Assert.assertEquals(means[i] + expectedSample.getEntry(i), sample[i], 1e-12);
        }
    }

    @Test
    public void testSampleNegativeValues() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});

        double[] sample = mnd.sample();
        double[] means = mnd.getMeans();
        RealVector covarianceMatrix = mnd.getCovariances();

        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(sample[i] >= -3.0 && sample[i] <= 3.0);
        }

        RealVector expectedSample = covarianceMatrix.operate(new Array2DRowRealMatrix(new double[][]{{FastMath.random(), FastMath.random()},
                {FastMath.random(), FastMath.random()}}).getColumnVector(0));

        for (int i = 0; i < sample.length; i++) {
            Assert.assertEquals(means[i] + expectedSample.getEntry(i), sample[i], 1e-12);
        }

        for (int i = 0; i < sample.length; i++) {
            Assert.assertFalse(sample[i] < 0.0);
        }
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});

        double[] standardDeviations = mnd.getStandardDeviations();
        Assert.assertEquals(1.0, standardDeviations[0], 1e-12);
        Assert.assertEquals(1.0, standardDeviations[1], 1e-12);
    }
}