import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    
    private MultivariateNormalDistribution distribution;
    
    @Before
    public void setUp() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }
    
    @Test
    public void testGetMeans() {
        double[] expectedMeans = {1, 2, 3};
        assertArrayEquals(expectedMeans, distribution.getMeans(), 0);
    }
    
    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        assertArrayEquals(expectedCovariances, distribution.getCovariances(), 0);
    }
    
    @Test
    public void testDensity() {
        double[] vals = {1, 2, 3};
        double expectedDensity = 0.06349363593424097;
        assertEquals(expectedDensity, distribution.density(vals), 0);
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1, 1, 1};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0);
    }
    
    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(3, sample.length);
    }
    
    @Test
    public void testGetExponentTerm() {
        double[] values = {1, 2, 3};
        double expectedExponentTerm = 0.1353352832366127;
        assertEquals(expectedExponentTerm, distribution.getExponentTerm(values), 0);
    }
}