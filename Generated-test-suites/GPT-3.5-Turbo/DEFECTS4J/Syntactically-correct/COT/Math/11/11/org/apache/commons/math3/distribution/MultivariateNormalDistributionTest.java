import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2, 3},
                new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}
        );
        double[] expectedMeans = {1, 2, 3};
        double[] actualMeans = distribution.getMeans();
        Assert.assertArrayEquals(expectedMeans, actualMeans, 0);
    }

    @Test
    public void testGetCovariances() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2, 3},
                new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}
        );
        double[][] expectedCovariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        double[][] actualCovariances = distribution.getCovariances().getData();
        for (int i = 0; i < expectedCovariances.length; i++) {
            Assert.assertArrayEquals(expectedCovariances[i], actualCovariances[i], 0);
        }
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2, 3},
                new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}
        );
        double[] values = {1, 2, 3};
        double expectedDensity = 0.15915494309189535;
        double actualDensity = distribution.density(values);
        Assert.assertEquals(expectedDensity, actualDensity, 0);
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2, 3},
                new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}
        );
        double[] expectedStandardDeviations = {1, 1, 1};
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2, 3},
                new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}
        );
        double[] sample = distribution.sample();
        Assert.assertEquals(sample.length, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDensityInput() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2, 3},
                new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}
        );
        double[] values = {1, 2};
        distribution.density(values);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovariancesMatrix() {
        double[][] singularCovariances = {{1, 2}, {2, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2},
                singularCovariances
        );
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteCovariancesMatrix() {
        double[][] nonPositiveDefiniteCovariances = {{1, -1}, {-1, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(
                new double[]{1, 2},
                nonPositiveDefiniteCovariances
        );
    }
}