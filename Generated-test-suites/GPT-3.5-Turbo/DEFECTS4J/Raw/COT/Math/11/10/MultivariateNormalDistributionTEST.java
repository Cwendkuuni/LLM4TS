import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] expectedMeans = {1.0, 2.0};
        Assert.assertArrayEquals(expectedMeans, distribution.getMeans(), 0.0001);
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[][] expectedCovariances = {{1.0, 0.0}, {0.0, 1.0}};
        Assert.assertArrayEquals(expectedCovariances, distribution.getCovariances().getData(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] vals = {1.0, 2.0};
        double expectedDensity = 0.15915494309189535; // Calculated using a separate calculator
        Assert.assertEquals(expectedDensity, distribution.density(vals), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] expectedStandardDeviations = {1.0, 1.0};
        Assert.assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0.0001);
    }
    
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] sample = distribution.sample();
        
        // No explicit assertions on the sample values as they are randomly generated
        Assert.assertNotNull(sample);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] vals = {1.0, 2.0, 3.0}; // Dimension mismatch
        
        distribution.density(vals);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 2.0}, {2.0, 1.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 1.0}, {0.0, 0.0}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
}