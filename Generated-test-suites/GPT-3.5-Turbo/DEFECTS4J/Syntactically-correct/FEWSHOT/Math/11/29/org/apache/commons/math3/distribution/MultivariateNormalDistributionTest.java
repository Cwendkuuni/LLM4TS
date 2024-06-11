import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private MultivariateNormalDistribution distribution;
    private double[] means;
    private double[][] covariances;

    @Before
    public void setUp() {
        means = new double[] {1.0, 2.0, 3.0};
        covariances = new double[][] {{1.0, 0.5, 0.2}, {0.5, 1.0, 0.3}, {0.2, 0.3, 1.0}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1.0, 2.0, 3.0};
        double[] actual = distribution.getMeans();
        assertArrayEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expected = MatrixUtils.createRealMatrix(covariances);
        RealMatrix actual = distribution.getCovariances();
        assertEquals(expected, actual);
    }

    @Test
    public void testDensity() {
        double[] values = {1.0, 2.0, 3.0};
        double expected = 0.048130063130877814;
        double actual = distribution.density(values);
        assertEquals(expected, actual, 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidDimension() {
        double[] values = {1.0, 2.0};
        distribution.density(values);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1.0, 1.0, 1.0};
        double[] actual = distribution.getStandardDeviations();
        assertArrayEquals(expected, actual, 0.0);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(3, sample.length);
    }
    
    @Test
    public void testSampleMean() {
        int samples = 10000;
        double[] sampleMean = new double[3];
        RandomDataGenerator randomData = new RandomDataGenerator();
        
        for (int i = 0; i < samples; i++) {
            double[] sample = distribution.sample();
            sampleMean[0] += sample[0];
            sampleMean[1] += sample[1];
            sampleMean[2] += sample[2];
        }
        
        sampleMean[0] /= samples;
        sampleMean[1] /= samples;
        sampleMean[2] /= samples;
        
        double[] expectedMean = distribution.getMeans();
        
        assertArrayEquals(expectedMean, sampleMean, 0.1);
    }
}