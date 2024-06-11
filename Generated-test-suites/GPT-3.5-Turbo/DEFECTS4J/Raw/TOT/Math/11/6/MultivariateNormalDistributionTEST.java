import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    
    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        };
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] expectedMeans = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedMeans, distribution.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        };
        assertArrayEquals(expectedCovariances, distribution.getCovariances().getData());
    }

    @Test
    public void testDensity() {
        double[] vals = {1.0, 2.0, 3.0};
        double expectedDensity = 0.015073;
        assertEquals(expectedDensity, distribution.density(vals), 0.00001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1.0, Math.sqrt(2.0), Math.sqrt(3.0)};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0.001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(3, sample.length);
    }
}