import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {
    
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] actualMeans = distribution.getMeans();
        
        Assert.assertArrayEquals(means, actualMeans, 0.00001);
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[][] actualCovariances = distribution.getCovariances().getData();
        
        Assert.assertArrayEquals(covariances, actualCovariances);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] expectedStandardDeviations = {1.0, 1.0, 1.0};
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        
        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0.00001);
    }
    
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] values = {1.5, 2.5, 3.5};
        double expectedDensity = 0.06349363593424099;
        double actualDensity = distribution.density(values);
        
        Assert.assertEquals(expectedDensity, actualDensity, 0.00001);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidDimension() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        
        double[] values = {1.5, 2.5};
        double density = distribution.density(values);
    }
    
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        
        double[] expectedSample = {2.418280726716733, 1.978911697042852, 2.384623862287939};
        double[] actualSample = distribution.sample();
        
        Assert.assertArrayEquals(expectedSample, actualSample, 0.00001);
    }
    
    @Test
    public void testSampleMultipleTimes() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        int sampleSize = 1000;
        double tolerance = 0.05;
        
        double[] sum = new double[3];
        double[] mean = new double[3];
        
        for (int i = 0; i < sampleSize; i++) {
            double[] sample = distribution.sample();
            sum[0] += sample[0];
            sum[1] += sample[1];
            sum[2] += sample[2];
        }
        
        mean[0] = sum[0] / sampleSize;
        mean[1] = sum[1] / sampleSize;
        mean[2] = sum[2] / sampleSize;
        
        Assert.assertEquals(means[0], mean[0], tolerance);
        Assert.assertEquals(means[1], mean[1], tolerance);
        Assert.assertEquals(means[2], mean[2], tolerance);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeEigenvalues() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }
    
}