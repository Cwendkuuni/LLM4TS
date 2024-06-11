import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributiontest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructorWithMeansAndCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(means[0], dist.getMeans()[0]);
        assertEquals(means[1], dist.getMeans()[1]);
        RealMatrix covMat = dist.getCovariances();
        assertEquals(covariances.length, covMat.getRowDimension());
        assertEquals(covariances[0].length, covMat.getColumnDimension());
        assertArrayEquals(covariances[0], covMat.getColumnVector(0).toArray());
        assertArrayEquals(covariances[1], covMat.getColumnVector(1).toArray());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentMeansAndCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0}, {1.0, 2.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{0.0, 1.0}, {1.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] test = {1.5, 2.5};
        double expected = 0.1878353219161618;
        assertEquals(expected, dist.density(test), 1e-7);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 2.0}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        double[] result = dist.sample();
        assertTrue(result.length == means.length);
        for (int i = 0; i < result.length; i++) {
            assertTrue(result[i] > means[i] - 2.5 && result[i] < means[i] + 2.5);
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDev = dist.getStandardDeviations();
        assertEquals(stdDev.length, means.length);
        for (int i = 0; i < stdDev.length; i++) {
            assertNotNull(stdDev[i]);
            assertTrue(stdDev[i] > 0.0);
        }
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[0][0]);
        double[] result = dist.getMeans();
        assertArrayEquals(means, result);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix result = dist.getCovariances();
        assertEquals(covariances.length, result.getRowDimension());
        assertEquals(covariances[0].length, result.getColumnDimension());
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances[i].length; j++) {
                assertEquals(covariances[i][j], result.getEntry(i, j), 1e-7);
            }
        }
    }
}