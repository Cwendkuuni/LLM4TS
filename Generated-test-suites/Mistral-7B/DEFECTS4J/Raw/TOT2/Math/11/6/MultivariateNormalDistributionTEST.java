import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructors() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.5, 0.5}, {0.5, 2.5}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, distribution.getDimension());
        assertEquals(means, distribution.getMeans());
        assertEquals(covariances, distribution.getCovariances().getData());

        final RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(means, distribution2.getMeans());
        assertEquals(covariances, distribution2.getCovariances().getData());
        assertEquals(rng, distribution2.getRandomGenerator());
    }

    @Test
    public void testGetStandardDeviations() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.5, 0.5}, {0.5, 2.5}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = distribution.getStandardDeviations();
        assertArrayEquals(new double[]{Math.sqrt(1.5), Math.sqrt(0.5), Math.sqrt(2.5)}, standardDeviations, 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0}, new double[][]{});
        distribution.density(new double[]{1.0, 2.0});
    }

    @Test
    public void testDensity() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.5, 0.5}, {0.5, 2.5}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.5, 3.0};
        double expected = 0.1145568838631187;
        double actual = distribution.density(values);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testSample() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.5, 0.5}, {0.5, 2.5}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertArrayEquals(new double[]{1.932430707604328, 3.188362145082692}, sample, 0.01);
    }

    @Test
    public void testSampleMultipleTimes() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.5, 0.5}, {0.5, 2.5}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final int size = 1000;
        RealMatrix matrix = new Array2DRowRealMatrix(size, 2);
        for (int i = 0; i < size; i++) {
            double[] sample = distribution.sample();
            matrix.setRow(i, sample);
        }
        RealVector expectedMean = new Array2DRowRealMatrix(means, true).transpose();
        RealVector actualMean = matrix.getColumnVector(0);
        assertEquals(expectedMean, actualMean, 0.01);
    }
}