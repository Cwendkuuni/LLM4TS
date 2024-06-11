import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private MultivariateNormalDistribution distribution;
    private double[] means;
    private double[][] covariances;
    private double[] vals;

    @Before
    public void setUp() {
        means = new double[]{1.0, 2.0, 3.0};
        covariances = new double[][]{{2.0, 0.5, 0.0}, {0.5, 1.5, -0.1}, {0.0, -0.1, 1.2}};
        vals = new double[]{1.5, 2.5, 3.5};
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(distribution);
        assertArrayEquals(means, distribution.getMeans(), 0.0001);
        assertArrayEquals(covariances, distribution.getCovariances().getData(), 0.0001);
        assertArrayEquals(new double[]{1.4142135623730951, 1.224744871391589, 1.0954451150103321}, distribution.getStandardDeviations(), 0.0001);
    }

    @Test
    public void testDensity() {
        assertEquals(0.03848306239010145, distribution.density(vals), 0.0001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(3, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] centered = new double[]{0.5, 0.5, 0.5};
        assertEquals(0.6065306597126334, distribution.getExponentTerm(centered), 0.0001);
    }

}