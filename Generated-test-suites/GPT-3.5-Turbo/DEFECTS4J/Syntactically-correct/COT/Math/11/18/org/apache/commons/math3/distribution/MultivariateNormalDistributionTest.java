import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMeanVector() {
        double[] expectedMeans = {0, 0};
        double[] actualMeans = distribution.getMeans();
        Assert.assertArrayEquals(expectedMeans, actualMeans, 0.0001);
    }

    @Test
    public void testCovarianceMatrix() {
        double[][] expectedCovariances = {{1, 0}, {0, 1}};
        double[][] actualCovariances = distribution.getCovariances().getData();
        Assert.assertArrayEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testDensity() {
        double[] vals = {0, 0};
        double expectedDensity = 0.1591;
        double actualDensity = distribution.density(vals);
        Assert.assertEquals(expectedDensity, actualDensity, 0.0001);
    }

    @Test
    public void testStandardDeviations() {
        double[] expectedStandardDeviations = {1, 1};
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0.0001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertEquals(2, sample.length);
    }
}