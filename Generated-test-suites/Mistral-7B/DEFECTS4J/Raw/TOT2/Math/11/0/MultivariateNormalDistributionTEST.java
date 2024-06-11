import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributiontest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;
    private RandomGenerator random;

    @Before
    public void setUp() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5, 0.1},
            {0.5, 2.0, 0.5},
            {0.1, 0.5, 4.0}
        };
        try {
            distribution = new MultivariateNormalDistribution(random, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
        random = new Well19937c();
    }

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(distribution);
        Assert.assertEquals(3, distribution.getDimension());
        Assert.assertArrayEquals(means, distribution.getMeans());

        RealMatrix covarianceMatrix = distribution.getCovariances();
        Assert.assertEquals(covariances.length, covarianceMatrix.getRowDimension());
        Assert.assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        Assert.assertArrayEquals(covariances, covarianceMatrix.getData());
    }

    /**
     * Test method getMeans().
     */
    @Test
    public void testGetMeans() {
        double[] means = distribution.getMeans();
        Assert.assertArrayEquals(means, new double[]{1.0, 2.0, 3.0});
    }

    /**
     * Test method getCovariances().
     */
    @Test
    public void testGetCovariances() {
        RealMatrix covarianceMatrix = distribution.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        Assert.assertEquals(expectedCovarianceMatrix, covarianceMatrix);
    }

    /**
     * Test method density().
     */
    @Test
    public void testDensity() {
        double[] values = new double[]{0.0, 0.0, 0.0};
        double density = distribution.density(values);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -3.0 / 2) * FastMath.pow(distribution.covarianceMatrixDeterminant, -0.5) * getExponentTerm(values);
        Assert.assertEquals(expectedDensity, density, 1e-6);
    }

    /**
     * Test method sample().
     */
    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        RealVector expectedSample = random.nextMultivariateVector(distribution.getDimension());
        expectedSample = distribution.samplingMatrix.operate(expectedSample);
        expectedSample = FastMath.add(expectedSample.toArray(), distribution.means);
        Assert.assertArrayEquals(expectedSample.toArray(), sample);
    }

    /**
     * Test method getStandardDeviations().
     */
    @Test
    public void testGetStandardDeviations() {
        double[] standardDeviations = distribution.getStandardDeviations();
        double[] expectedStandardDeviations = {FastMath.sqrt(1.0), FastMath.sqrt(2.0), FastMath.sqrt(4.0)};
        Assert.assertArrayEquals(expectedStandardDeviations, standardDeviations);
    }

    /**
     * Test method with invalid input
     * (means and covariances arrays with different lengths).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidInput() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {1.0, 0.5, 0.1},
            {0.5, 2.0, 0.5},
            {0.1, 0.5, 4.0},
            {1.0, 0.5}
        };
        new MultivariateNormalDistribution(random, means, covariances);
    }

    /**
     * Test method with invalid input
     * (covariances array with incorrect dimensions).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidInput2() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5, 0.1},
            {0.5, 2.0},
            {0.1, 0.5, 4.0}
        };
        new MultivariateNormalDistribution(random, means, covariances);
    }

    /**
     * Test method with invalid input
     * (non-positive definite covariance matrix).
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
            {1.0, -1.0},
            {-1.0, 1.0}
        };
        new MultivariateNormalDistribution(random, means, covariances);
    }

    /**
     * Helper method for calculating exponent term.
     *
     * @param values Values at which to compute density.
     * @return the multiplication factor of density calculations.
     */
    private double getExponentTerm(double[] values) {
        final double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - distribution.getMeans()[i];
        }
        final double[] preMultiplied = distribution.covarianceMatrixInverse.preMultiply(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }
}