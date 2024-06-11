import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() throws Exception {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {0, 0};
        double[] actual = distribution.getMeans();
        assertArrayEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {{1, 0}, {0, 1}};
        RealMatrix actual = distribution.getCovariances();
        assertArrayEquals(expected, actual.toArray());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidDimension() {
        double[] vals = {0};
        distribution.density(vals);
    }

    // Edge case: Density at mean should be highest value
    @Test
    public void testDensityAtMean() {
        double[] vals = {0, 0};
        double expected = Double.POSITIVE_INFINITY;
        double actual = distribution.density(vals);
        assertEquals(expected, actual, 0.0001);
    }

    // Edge case: Density at infinity should be close to zero
    @Test
    public void testDensityAtInfinity() {
        double[] vals = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        double expected = 0;
        double actual = distribution.density(vals);
        assertEquals(expected, actual, 0.0001);
    }

    // Edge case: Density at -infinity should be close to zero
    @Test
    public void testDensityAtNegativeInfinity() {
        double[] vals = {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        double expected = 0;
        double actual = distribution.density(vals);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        distribution = new MultivariateNormalDistribution(rng, new double[]{0, 0}, new double[][]{{1, 0}, {0, 1}});
        double[] sample = distribution.sample();
        assertNotNull(sample);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 1}, {1, 1}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatch() {
        double[] means = {0};
        double[][] covariances = {{1, 0}, {0, 1}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrix() {
        double[] means = {0, 0};
        double[][] covariances = {{0, 0}, {0, 0}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }
}