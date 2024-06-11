import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
 
public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
    }
 
    @Test
    public void testDensity() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        double[] values = {1, 1, 1};
        double expected = 0.06349363593424088;
        double actual = distribution.density(values);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testSample() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        double[] sample = distribution.sample();
        assertEquals(3, sample.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatch() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        double[] values = {1, 1};
        distribution.density(values);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSampleDimensionMismatch() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        distribution.sample();
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidCovarianceMatrix() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, -1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovarianceMatrix() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 1}, {1, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }
    
    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        double[] expected = {1, 2, 3};
        double[] actual = distribution.getMeans();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        RealMatrix expected = new Array2DRowRealMatrix(covariances);
        RealMatrix actual = distribution.getCovariances();
        assertTrue(expected.subtract(actual).isZero());
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        double[] expected = {1, 1, 1};
        double[] actual = distribution.getStandardDeviations();
        assertArrayEquals(expected, actual);
    }
    
    private void assertArrayEquals(double[] expected, double[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0.000001);
        }
    }
 
}