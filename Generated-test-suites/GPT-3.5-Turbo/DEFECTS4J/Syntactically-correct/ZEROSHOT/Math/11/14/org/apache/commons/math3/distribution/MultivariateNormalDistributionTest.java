import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class MultivariateNormalDistributionTest {
    
    @Test
    public void testConstructor() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            Assert.assertNotNull(mnd);
            Assert.assertArrayEquals(means, mnd.getMeans(), 0);
            Assert.assertArrayEquals(covariances, mnd.getCovariances().getData(), 0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution((RandomGenerator) null, means, covariances);
            Assert.assertNotNull(mnd);
            Assert.assertArrayEquals(means, mnd.getMeans(), 0);
            Assert.assertArrayEquals(covariances, mnd.getCovariances().getData(), 0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatchException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0}, {0, 1}, {0, 0}};
        
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrixException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrixException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}};
        
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] result = mnd.getMeans();
            Assert.assertArrayEquals(means, result, 0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[][] result = mnd.getCovariances().getData();
            Assert.assertArrayEquals(covariances, result, 0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDensity() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {1, 2};
            double result = mnd.density(vals);
            double expected = 0.15915494309189535;
            Assert.assertEquals(expected, result, 0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatchException() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1, 2, 3};
        double result = mnd.density(vals);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] result = mnd.getStandardDeviations();
            double[] expected = {1, 1, 1};
            Assert.assertArrayEquals(expected, result, 0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSample() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] result = mnd.sample();
            Assert.assertNotNull(result);
            Assert.assertEquals(means.length, result.length);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNotStrictlyPositiveException() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(null, means, covariances);
        double[] result = mnd.sample();
    }
    
    @Test
    public void testGetExponentTerm() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {1, 2};
            double result = mnd.getExponentTerm(vals);
            double expected = 0.6065306597126334;
            Assert.assertEquals(expected, result, 0);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
}