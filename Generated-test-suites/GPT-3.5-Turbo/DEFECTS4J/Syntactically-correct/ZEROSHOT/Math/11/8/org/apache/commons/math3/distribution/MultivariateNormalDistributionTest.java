import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() throws Exception {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1, 2, 3};
        assertArrayEquals(expected, distribution.getMeans(), 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        assertArrayEquals(expected, distribution.getCovariances().getData(), 0.0001);
    }

    @Test
    public void testDensity() {
        double[] vals = {1, 2, 3};
        double expected = 0.06349363593424097;
        assertEquals(expected, distribution.density(vals), 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1, 1, 1};
        assertArrayEquals(expected, distribution.getStandardDeviations(), 0.0001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertEquals(3, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1, 2, 3};
        double expected = 0.1353352832366127;
        assertEquals(expected, distribution.getExponentTerm(values), 0.0001);
    }
}