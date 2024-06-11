import org.junit.Test;

public class MultivariateNormalDistributionTest {
    
    @Test
    public void testGetMeans() {
        // Test case 1
        double[] means1 = {1, 2, 3};
        MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(means1, null);
        assertArrayEquals(means1, distribution1.getMeans(), 0.0001);
        
        // Test case 2
        double[] means2 = {0, 0, 0};
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(means2, null);
        assertArrayEquals(means2, distribution2.getMeans(), 0.0001);
    }
    
    @Test
    public void testGetCovariances() {
        // Test case 1
        double[][] covariances1 = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(null, covariances1);
        assertArrayEquals(covariances1, distribution1.getCovariances().getData(), 0.0001);
        
        // Test case 2
        double[][] covariances2 = {{1.5, 0.5, 0.3}, {0.5, 1.5, 0.2}, {0.3, 0.2, 1.5}};
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(null, covariances2);
        assertArrayEquals(covariances2, distribution2.getCovariances().getData(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        // Test case 1
        double[] means1 = {1, 2, 3};
        double[][] covariances1 = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(means1, covariances1);
        double[] vals1 = {1, 2, 3};
        assertEquals(0.1591, distribution1.density(vals1), 0.0001);
        
        // Test case 2
        double[] means2 = {0, 0, 0};
        double[][] covariances2 = {{1.5, 0.5, 0.3}, {0.5, 1.5, 0.2}, {0.3, 0.2, 1.5}};
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(means2, covariances2);
        double[] vals2 = {0, 0, 0};
        assertEquals(0.0635, distribution2.density(vals2), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviations() {
        // Test case 1
        double[][] covariances1 = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(null, covariances1);
        double[] std1 = {1, 1, 1};
        assertArrayEquals(std1, distribution1.getStandardDeviations(), 0.0001);
        
        // Test case 2
        double[][] covariances2 = {{1.5, 0.5, 0.3}, {0.5, 1.5, 0.2}, {0.3, 0.2, 1.5}};
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(null, covariances2);
        double[] std2 = {1.2247, 1.2247, 1.2247};
        assertArrayEquals(std2, distribution2.getStandardDeviations(), 0.0001);
    }
    
    @Test
    public void testSample() {
        // Test case 1
        double[] means1 = {1, 2, 3};
        double[][] covariances1 = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(means1, covariances1);
        double[] sample1 = distribution1.sample();
        assertEquals(1, sample1.length);
        
        // Test case 2
        double[] means2 = {0, 0, 0};
        double[][] covariances2 = {{1.5, 0.5, 0.3}, {0.5, 1.5, 0.2}, {0.3, 0.2, 1.5}};
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(means2, covariances2);
        double[] sample2 = distribution2.sample();
        assertEquals(1, sample2.length);
    }
    
    @Test
    public void testGetExponentTerm() {
        // Test case 1
        double[] means1 = {1, 2, 3};
        double[][] covariances1 = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(means1, covariances1);
        double[] vals1 = {1, 2, 3};
        assertEquals(0.6065, distribution1.getExponentTerm(vals1), 0.0001);
        
        // Test case 2
        double[] means2 = {0, 0, 0};
        double[][] covariances2 = {{1.5, 0.5, 0.3}, {0.5, 1.5, 0.2}, {0.3, 0.2, 1.5}};
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(means2, covariances2);
        double[] vals2 = {0, 0, 0};
        assertEquals(1.0000, distribution2.getExponentTerm(vals2), 0.0001);
    }
}