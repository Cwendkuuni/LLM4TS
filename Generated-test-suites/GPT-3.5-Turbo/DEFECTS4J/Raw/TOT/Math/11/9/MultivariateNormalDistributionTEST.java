import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class MultivariateNormalDistributionTest {
    
    private MultivariateNormalDistribution distribution;
    private double[] means = {1, 2, 3};
    private double[][] covariances = {{4, 1, 0}, {1, 5, 2}, {0, 2, 6}};
    
    @Before
    public void setup() {
        distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test
    public void testGetMeans() {
        double[] expectedMeans = {1, 2, 3};
        assertArrayEquals(expectedMeans, distribution.getMeans(), 0);
    }
    
    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {{4, 1, 0}, {1, 5, 2}, {0, 2, 6}};
        assertArrayEquals(expectedCovariances, distribution.getCovariances().getData());
    }
    
    @Test
    public void testDensity() {
        double[] vals = {2, 3, 4};
        double expectedDensity = 0.004714;

        assertEquals(expectedDensity, distribution.density(vals), 1e-6);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {2, 2.236, 2.449};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 1e-3);
    }
    
    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertEquals(3, sample.length);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatchException() {
        double[] vals = {2, 3};
        distribution.density(vals);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatchException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{4, 1}, {1, 5}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrixException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{-1, 1, 0}, {1, 5, 2}, {0, 2, 6}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
}