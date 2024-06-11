import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.0}
        };
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expectedMeans = {1.0, 2.0};
        assertArrayEquals(expectedMeans, distribution.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {
                {1.0, 0.5},
                {0.5, 2.0}
        };
        assertArrayEquals(expectedCovariances, distribution.getCovariances().getData());
    }

    @Test
    public void testDensity() {
        double[] vals = {0.5, 1.5};
        double expectedDensity = 0.04418718939843742;
        assertEquals(expectedDensity, distribution.density(vals), 0.000000000000001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1.0, 1.4142135623730951};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0.000000000000001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(2, sample.length);
    }

}