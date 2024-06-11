import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        Assert.assertArrayEquals(means, distribution.getMeans());
        Assert.assertEquals(covariances.length, distribution.getCovariances().getRowDimension());
        Assert.assertEquals(covariances[0].length, distribution.getCovariances().getColumnDimension());
        Assert.assertEquals(covariances.length, distribution.getDimension());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionWithInvalidMeansDimension() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        new MultivariateNormalDistribution(means, covariances, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionWithInvalidCovariancesRows() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}, {3.0, 4.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionWithInvalidCovariancesColumns() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}, {0.5, 2.0, 3.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionWithInvalidCovariances() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, -1.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testMultivariateNormalDistributionDensity() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[]{1.5, 2.5};

        double density = distribution.density(testValues);
        double expectedDensity = 0.2189176252641184;

        Assert.assertEquals(expectedDensity, density, 1e-9);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionDensityWithInvalidValuesLength() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] testValues = new double[]{1.5, 2.5, 3.0};
        distribution.density(testValues);
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();

        Assert.assertArrayEquals(means.length, sample.length);

        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(Double.isFinite(sample[i]));
        }
    }

    @Test
    public void testMultivariateNormalDistributionGetStandardDeviations() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] standardDeviations = distribution.getStandardDeviations();

        Assert.assertArrayEquals(means.length, standardDeviations.length);

        for (int i = 0; i < standardDeviations.length; i++) {
            Assert.assertTrue(standardDeviations[i] > 0);
        }
    }
}