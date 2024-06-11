import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};

        try {
            distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] expected = {0, 0};
        double[] actual = distribution.getMeans();

        Assert.assertArrayEquals(expected, actual, Math.ulp(actual[0]));
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {{1, 0}, {0, 1}};
        double[][] actual = distribution.getCovariances().getData();

        for (int i = 0; i < expected.length; i++) {
            Assert.assertArrayEquals(expected[i], actual[i], Math.ulp(actual[i][0]));
        }
    }

    @Test
    public void testDensity() {
        double[] values = {0, 0};

        double expected = 0.15915494309189535; // Calculated manually
        double actual = distribution.density(values);

        Assert.assertEquals(expected, actual, Math.ulp(actual));
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1, 1};
        double[] actual = distribution.getStandardDeviations();

        Assert.assertArrayEquals(expected, actual, Math.ulp(actual[0]));
    }

    @Test
    public void testSample() {
        int numSamples = 10000;
        double[] x = new double[numSamples];
        double[] y = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double[] sample = distribution.sample();
            x[i] = sample[0];
            y[i] = sample[1];
        }

        // Statistical tests
        Assert.assertEquals(0, Math.abs(Arrays.stream(x).sum()), 0.1 * numSamples);
        Assert.assertEquals(0, Math.abs(Arrays.stream(y).sum()), 0.1 * numSamples);

        Assert.assertEquals(1, Math.abs(Arrays.stream(x).map(v -> Math.pow(v, 2)).sum()), 0.1 * numSamples);
        Assert.assertEquals(1, Math.abs(Arrays.stream(y).map(v -> Math.pow(v, 2)).sum()), 0.1 * numSamples);

        Assert.assertEquals(0, Math.abs(Arrays.stream(x).map(v -> Math.pow(v, 3)).sum()), 0.1 * numSamples);
        Assert.assertEquals(0, Math.abs(Arrays.stream(y).map(v -> Math.pow(v, 3)).sum()), 0.1 * numSamples);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {1, 0}, {0, 1}};

        distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 1}, {1, 1}};

        distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[] means = {0, 0};
        double[][] covariances = {{-1, 0}, {0, -1}};

        distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

}