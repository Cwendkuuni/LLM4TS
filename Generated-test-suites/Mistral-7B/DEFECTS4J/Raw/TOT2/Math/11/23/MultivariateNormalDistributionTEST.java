import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructorWithVectorAndMatrix() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(means, dist.getMeans());
        RealMatrix cov = dist.getCovariances();
        assertEquals(cov.getRowDimension(), cov.getColumnDimension());
        assertEquals(cov.getData(), covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentArrays() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0}, {1.0, 1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularMatrix() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{0.0, 1.0}, {1.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeEigenvalues() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{-1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        double[] sampleMeans = new MultivariateNormalDistribution(means, new double[0][0]).getMeans();
        assertArrayEquals(means, sampleMeans);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        double[][] sampleCovariances = new MultivariateNormalDistribution(means, covariances).getCovariances().getData();
        assertArrayEquals(covariances, sampleCovariances);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            double expected = Math.random();
            double actual = sample[i];
            assertEquals(expected, actual, 0.001);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSampleWithIncorrectDimension() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        dist.sample(3);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {1.5, 2.5};
        double expectedDensity = 0.11274054;
        double actualDensity = dist.density(testValues);
        assertEquals(expectedDensity, actualDensity, 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithIncorrectDimension() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {1.0, 2.0, 3.0};
        dist.density(testValues);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = dist.getStandardDeviations();
        assertArrayEquals(new double[]{Math.sqrt(0.5), Math.sqrt(0.5)}, stdDeviations, 0.001);
    }
}