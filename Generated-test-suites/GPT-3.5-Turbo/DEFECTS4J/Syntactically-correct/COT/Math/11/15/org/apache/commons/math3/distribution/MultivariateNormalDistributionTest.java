
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class MultivariateNormalDistributionTest {
    
    @Test
    public void testConstructor() {
        // Test valid input
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
            Assert.assertArrayEquals(means, distribution.getMeans(), 0);
            Assert.assertArrayEquals(covariances, distribution.getCovariances().getData(), 0);
        } catch (Exception e) {
            Assert.fail("Constructor should not throw an exception");
        }
        
        // Test invalid input - dimension mismatch
        double[] invalidMeans = {1, 2, 3};
        double[][] invalidCovariances = {{1, 0, 0}, {0, 1, 0}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(invalidMeans, invalidCovariances);
            Assert.fail("Constructor should throw a DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Constructor should throw a DimensionMismatchException");
        }
        
        // Test invalid input - non-positive definite matrix
        double[][] invalidCovariancesNonPositiveDefinite = {{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, invalidCovariancesNonPositiveDefinite);
            Assert.fail("Constructor should throw a NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Constructor should throw a NonPositiveDefiniteMatrixException");
        }
        
        // Test invalid input - singular matrix
        double[][] invalidCovariancesSingular = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, invalidCovariancesSingular);
            Assert.fail("Constructor should throw a SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Constructor should throw a SingularMatrixException");
        }
    }
    
    @Test
    public void testDensity() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        // Test valid input
        double[] validInput = {1, 2};
        try {
            double density = distribution.density(validInput);
            Assert.assertEquals(0.159155, density, 0.000001);
        } catch (Exception e) {
            Assert.fail("Density method should not throw an exception");
        }
        
        // Test invalid input - dimension mismatch
        double[] invalidInput = {1};
        try {
            double density = distribution.density(invalidInput);
            Assert.fail("Density method should throw a DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Density method should throw a DimensionMismatchException");
        }
    }
    
    @Test
    public void testGetMeans() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        // Test getMeans() method
        double[] result = distribution.getMeans();
        Assert.assertArrayEquals(means, result, 0);
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        // Test getCovariances() method
        double[][] result = distribution.getCovariances().getData();
        Assert.assertArrayEquals(covariances, result, 0);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        // Test getStandardDeviations() method
        double[] result = distribution.getStandardDeviations();
        double[] expected = {1, 1};
        Assert.assertArrayEquals(expected, result, 0);
    }
    
    @Test
    public void testSample() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        // Test sample() method
        double[] result = distribution.sample();
        Assert.assertEquals(2, result.length);
    }
    
    @Test
    public void testGetExponentTerm() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        // Test getExponentTerm() method
        double[] input = {0, 0};
        double result = distribution.getExponentTerm(input);
        Assert.assertEquals(0.017528, result, 0.000001);
    }
}
