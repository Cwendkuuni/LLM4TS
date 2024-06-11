import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    
    @Test
    public void testConstructor() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5, 0.1},
                                  {0.5, 1.0, 0.2},
                                  {0.1, 0.2, 0.5}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown");
        }
        
        Assert.assertNotNull(distribution);
    }
    
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5, 0.1},
                                  {0.5, 1.0, 0.2},
                                  {0.1, 0.2, 0.5}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown");
        }
        
        double[] actualMeans = distribution.getMeans();
        
        Assert.assertArrayEquals(means, actualMeans, 0.0);
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5, 0.1},
                                  {0.5, 1.0, 0.2},
                                  {0.1, 0.2, 0.5}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown");
        }
        
        RealMatrix actualCovariances = distribution.getCovariances();
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        
        Assert.assertEquals(expectedCovariances, actualCovariances);
    }
    
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5, 0.1},
                                  {0.5, 1.0, 0.2},
                                  {0.1, 0.2, 0.5}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown");
        }
        
        double[] values = {0.5, 1.5, 2.5};
        double expectedDensity = 0.121386186;

        try {
            double actualDensity = distribution.density(values);
            Assert.assertEquals(expectedDensity, actualDensity, 1e-9);
        } catch (DimensionMismatchException e) {
            Assert.fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5, 0.1},
                                  {0.5, 1.0, 0.2},
                                  {0.1, 0.2, 0.5}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown");
        }
        
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        double[] expectedStandardDeviations = {Math.sqrt(2.0), Math.sqrt(1.0), Math.sqrt(0.5)};
        
        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 1e-9);
    }
    
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5, 0.1},
                                  {0.5, 1.0, 0.2},
                                  {0.1, 0.2, 0.5}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown");
        }
        
        double[] sample = distribution.sample();
        
        Assert.assertNotNull(sample);
        Assert.assertEquals(means.length, sample.length);
    }
    
    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5, 0.1},
                                  {0.5, 1.0, 0.2},
                                  {0.1, 0.2, 0.5}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown");
        }
        
        double[] values = {0.5, 1.5, 2.5};
        double expectedExponentTerm = 0.090717953;
        
        double actualExponentTerm = distribution.getExponentTerm(values);
        
        Assert.assertEquals(expectedExponentTerm, actualExponentTerm, 1e-9);
    }
}