import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] result = dist.getMeans();
        assertArrayEquals(means, result);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix result = dist.getCovariances();
        RealMatrix expected = new Array2DRowRealMatrix(covariances);
        assertEquals(expected, result);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1.5, 2.5, 3.5};
        double result = dist.density(vals);
        double expected = 0.06011288933620826;
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] result = dist.getStandardDeviations();
        double[] expected = {1.0, 1.0, 1.0};
        assertArrayEquals(expected, result, 1e-10);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] result = dist.sample();
        assertEquals(3, result.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1.5, 2.5, 3.5};
        double result = dist.getExponentTerm(vals);
        double expected = 1.0;
        assertEquals(expected, result, 1e-10);
    }
}