import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    private double[] means;
    private double[][] covariances;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        means = new double[]{1.0, 2.0, 3.0};
        covariances = new double[][]{{2.0, 0.5, 0.1}, {0.5, 1.5, 0.2}, {0.1, 0.2, 1.0}};
        rng = new Well19937c();
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] invalidMeans = {1.0, 2.0};
        new MultivariateNormalDistribution(rng, invalidMeans, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[][] invalidCovariances = {{1.0, 0.5, 0.1}, {0.5, 1.5, 0.2}};
        new MultivariateNormalDistribution(rng, means, invalidCovariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] invalidCovariances = {{-1.0, 0.5, 0.1}, {0.5, 1.5, 0.2}, {0.1, 0.2, 1.0}};
        new MultivariateNormalDistribution(rng, means, invalidCovariances);
    }

    @Test
    public void testGetMeans() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        assertArrayEquals(means, distribution.getMeans(), 0.001);
    }

    @Test
    public void testGetCovariances() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        assertArrayEquals(covariances, distribution.getCovariances().getData(), 0.001);
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        double[] values = {2.0, 3.0, 4.0};
        double expectedDensity = 0.02661427566388762;
        assertEquals(expectedDensity, distribution.density(values), 0.001);
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        double[] expectedStandardDeviations = {1.414, 1.224, 1.0};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0.001);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        double[] sample = distribution.sample();
        assertEquals(3, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
        double[] values = {2.0, 3.0, 4.0};
        double expectedExponentTerm = 0.5289490751001358;
        assertEquals(expectedExponentTerm, distribution.getExponentTerm(values), 0.001);
    }
}