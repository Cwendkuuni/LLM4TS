import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    private MultivariateNormalDistribution distribution;
    private double[] means;
    private double[][] covariances;

    @Before
    public void setUp() {
        means = new double[]{0, 1, 2};
        covariances = new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}};
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Failed to create distribution");
        }
    }

    @Test
    public void testGetMeans() {
        double[] expectedMeans = means;
        double[] actualMeans = distribution.getMeans();
        assertArrayEquals(expectedMeans, actualMeans, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = covariances;
        double[][] actualCovariances = distribution.getCovariances().getData();
        for (int i = 0; i < expectedCovariances.length; i++) {
            assertArrayEquals(expectedCovariances[i], actualCovariances[i], 0.0001);
        }
    }

    @Test
    public void testDensity() {
        double[] testValues = new double[]{0, 0, 0};
        double expectedDensity = 0.06349363;
        double actualDensity = distribution.density(testValues);
        assertEquals(expectedDensity, actualDensity, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStd = new double[]{1, Math.sqrt(2), Math.sqrt(3)};
        double[] actualStd = distribution.getStandardDeviations();
        assertArrayEquals(expectedStd, actualStd, 0.0001);
    }

    @Test
    public void testSample() {
        double[][] samples = new double[1000][3];
        for (int i = 0; i < 1000; i++) {
            samples[i] = distribution.sample();
        }
        double[][] transposedSamples = MathArrays.transpose(samples);
        double[] means = Arrays.stream(transposedSamples).mapToDouble(arr -> Arrays.stream(arr).average().orElse(0)).toArray();
        double[] expectedMeans = distribution.getMeans();
        assertArrayEquals(expectedMeans, means, 0.1);
        double[][] actualCovariances = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                actualCovariances[i][j] = MathArrays.covariance(samples[i], samples[j]);
            }
        }
        double[][] expectedCovariances = distribution.getCovariances().getData();
        for (int i = 0; i < expectedCovariances.length; i++) {
            assertArrayEquals(expectedCovariances[i], actualCovariances[i], 0.1);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] testValues = new double[]{0, 0};
        distribution.density(testValues);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] nonPositiveDefiniteCovariances = new double[][]{{-1, 0, 0}, {0, 2, 0}, {0, 0, 3}};
        distribution = new MultivariateNormalDistribution(means, nonPositiveDefiniteCovariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[][] singularCovariances = new double[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 3}};
        distribution = new MultivariateNormalDistribution(means, singularCovariances);
    }
}