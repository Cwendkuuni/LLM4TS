import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.UnivariateRealDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpectralDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-12;
    private static final int DIM = 3;

    private MultivariateNormalDistribution dist;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        double[] means = new double[]{0.0, 1.0, 2.0};
        double[][] covariances = new double[][]{
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 0.5},
            {0.5, 0.5, 3.0}
        };
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (NonPositiveDefiniteMatrixException | SingularMatrixException | DimensionMismatchException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getMeans method.
     */
    @Test
    public void testGetMeans() {
        Assert.assertArrayEquals(dist.getMeans(), new double[]{0.0, 1.0, 2.0}, EPSILON);
    }

    /**
     * Test getCovariances method.
     */
    @Test
    public void testGetCovariances() {
        RealMatrix covMatrix = dist.getCovariances();
        Assert.assertEquals(covMatrix.getRowDimension(), covMatrix.getColumnDimension(), DIM);

        RealMatrix expectedCovMatrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 0.5},
            {0.5, 0.5, 3.0}
        });

        Assert.assertEquals(covMatrix, expectedCovMatrix);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[]{1.0, 2.0, 3.0};
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -DIM / 2) *
            FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) *
            dist.getExponentTerm(testValues);

        double actualDensity = dist.density(testValues);
        Assert.assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        double[] sampleValues = dist.sample();

        double[] means = dist.getMeans();
        double[] std = dist.getStandardDeviations();

        for (int i = 0; i < DIM; i++) {
            Assert.assertEquals(means[i], sampleValues[i], EPSILON);
            Assert.assertTrue(std[i] > 0);
            Assert.assertTrue(FastMath.abs(sampleValues[i] - means[i]) < std[i] * 2);
        }
    }

    /**
     * Test getExponentTerm method.
     */
    @Test
    public void testGetExponentTerm() {
        double[] testValues = new double[]{1.0, 2.0, 3.0};

        double actualExponentTerm = dist.getExponentTerm(testValues);
        double[] means = dist.getMeans();
        double[] standardDeviations = dist.getStandardDeviations();
        RealMatrix covMatrix = dist.covarianceMatrix;
        RealVector centered = new Array2DRowRealMatrix(new double[][]{centeredValues(testValues, means)})
                .getColumnVector(0);
        RealMatrix preMultiplied = covMatrix.inverse().preMultiply(centered);

        double expectedExponentTerm = FastMath.exp(-0.5 * preMultiplied.dotProduct(centered).getValue());

        Assert.assertEquals(expectedExponentTerm, actualExponentTerm, EPSILON);
    }

    private double[] centeredValues(double[] values, double[] means) {
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i] - means[i];
        }
        return result;
    }
}