import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    private RandomGenerator rng;
    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        rng = new Well19937c();
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 0.5},
            {0.5, 2.5}
        };

        try {
            distribution = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1.0, 2.0};
        Assert.assertArrayEquals(expected, distribution.getMeans());
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expected = new Array2DRowRealMatrix(
            new double[][]{{3.0, 0.5}, {0.5, 2.5}}
        );
        Assert.assertEquals(expected, distribution.getCovariances());
    }

    @Test
    public void testDensityEqualsZeroWhenValuesOutsideOfDistribution() {
        double[] values = {1.5, 3.0};
        Assert.assertEquals(0.0, distribution.density(values), 1e-10);
    }

    @Test
    public void testDensityWithinDistribution() {
        double[] values = {1.2, 2.1};
        double expectedDensity = 0.15430321468554516; // Calculated using MathWorld formula
        Assert.assertEquals(expectedDensity, distribution.density(values), 1e-10);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {FastMath.sqrt(3.0), FastMath.sqrt(2.5)};
        Assert.assertArrayEquals(expected, distribution.getStandardDeviations());
    }

    @Test
    public void testSampleSingleValue() {
        double expected = 1.0 + 3.0 * FastMath.sqrt(0.5 / 3.0) * FastMath.RAND;
        double sample = distribution.sample()[0];
        Assert.assertEquals(expected, sample, 1e-6);
    }

    @Test
    public void testSampleTwoValues() {
        double[] expected = {1.0 + 3.0 * FastMath.sqrt(0.5 / 3.0) * FastMath.RAND,
            2.0 + 2.5 * FastMath.sqrt(0.5 / 2.5) * FastMath.RAND};
        double[] sample = distribution.sample();
        Assert.assertArrayEquals(expected, sample, 1e-6);
    }

    @Test(expected = IllegalStateException.class)
    public void testSampleWhenDistributionIsNotInitialized() {
        MultivariateNormalDistribution uninitialized = new MultivariateNormalDistribution(rng, new double[0], new double[0][0]);
        uninitialized.sample();
    }
}