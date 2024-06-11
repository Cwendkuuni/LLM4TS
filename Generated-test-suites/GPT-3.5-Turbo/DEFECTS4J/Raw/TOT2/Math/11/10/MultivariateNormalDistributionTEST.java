import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        
        assertArrayEquals(means, distribution.getMeans(), 0.0);
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        assertArrayEquals(covariances, distribution.getCovariances().getData(), 0.0);
    }
    
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] vals = {0.0, 0.0};
        double expectedDensity = 0.159154943; // Calculated using external tool
        assertEquals(expectedDensity, distribution.density(vals), 0.000000001);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{4.0, 0.0}, {0.0, 9.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] expectedDeviations = {2.0, 3.0};
        assertArrayEquals(expectedDeviations, distribution.getStandardDeviations(), 0.0);
    }
    
    @Test
    public void testSample() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] sample = distribution.sample();
        
        assertNotNull(sample);
        assertEquals(2, sample.length);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithWrongDimension() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] vals = {0.0};
        distribution.density(vals);
    }
}