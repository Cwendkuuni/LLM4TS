import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;
    
    @Before
    public void setup() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test
    public void testGetMeans() {
        double[] expected = {1, 2, 3};
        assertArrayEquals(expected, distribution.getMeans(), 0.0);
    }
    
    @Test
    public void testGetCovariances() {
        double[][] expected = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        assertArrayEquals(expected, distribution.getCovariances().getData());
    }
    
    @Test
    public void testDensity() {
        double[] values = {1, 2, 3};
        assertEquals(0.06349363593424098, distribution.density(values), 1e-10);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1, 1, 1};
        assertArrayEquals(expected, distribution.getStandardDeviations(), 0.0);
    }
    
    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(3, sample.length);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidDimension() {
        double[] values = {1, 2};
        distribution.density(values);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSampleWithInvalidDimension() {
        double[] values = {1, 2};
        distribution.sample();
    }
    
}