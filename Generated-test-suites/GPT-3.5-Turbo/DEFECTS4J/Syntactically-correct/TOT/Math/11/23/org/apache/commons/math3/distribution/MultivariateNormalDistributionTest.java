import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    
    // Test case for constructor with valid mean and covariance matrix
    @Test
    public void testConstructorValid() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{4, 1, 2}, {1, 5, 3}, {2, 3, 6}};
        
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            
            // Check that mean vector is correctly set
            assertArrayEquals(means, distribution.getMeans(), 0.0);
            
            // Check that covariance matrix is correctly set
            RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
            assertTrue(expectedCovarianceMatrix.equals(distribution.getCovariances()));
            
        } catch (Exception e) {
            fail("Exception thrown for valid constructor arguments");
        }
    }
    
    // Test case for constructor with invalid mean and covariance matrix sizes
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorInvalidSize() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{4, 1}, {1, 5, 3}, {2, 3, 6}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    // Test case for constructor with singular covariance matrix
    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    // Test case for constructor with negative eigenvalue in covariance matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNegativeEigenvalue() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{5, 2, 3}, {2, -4, 1}, {3, 1, 6}};
        
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    // Test case for density calculation with valid input
    @Test
    public void testDensityValid() {
        double[] means = {2, 1};
        double[][] covariances = {{9, 3}, {3, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] vals = {1, 2};
        
        try {
            double density = distribution.density(vals);
            
            // Expected density value obtained from external library
            double expectedDensity = 0.033331516;
            
            assertEquals(expectedDensity, density, 1e-6);
            
        } catch (Exception e) {
            fail("Exception thrown for valid density calculation");
        }
    }
    
    // Test case for density calculation with input of incorrect size
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidSize() {
        double[] means = {2, 1};
        double[][] covariances = {{9, 3}, {3, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] vals = {1, 2, 3};
        
        double density = distribution.density(vals);
    }
    
    // Test case for sampling method
    @Test
    public void testSample() {
        double[] means = {2, 1};
        double[][] covariances = {{9, 3}, {3, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] sample = distribution.sample();
        
        assertEquals(2, sample.length);
    }
}