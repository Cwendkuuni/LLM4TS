import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;
    private double[] means = {1.0, 2.0};
    private double[][] covariances = {{1.5, 0.5}, {0.5, 2.5}};
    private Well19937c rng;

    @Before
    public void setUp() {
        try {
            rng = new Well19937c();
            distribution = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for constructor with single argument.
     */
    @Test
    public void testSingleArgumentConstructor() {
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means);
            Assert.assertArrayEquals(means, distribution.getMeans());
            RealMatrix covarianceMatrix = distribution.getCovariances();
            Assert.assertEquals(2, covarianceMatrix.getColumnDimension());
            Assert.assertEquals(2, covarianceMatrix.getRowDimension());
            Array2DRowRealMatrix covarianceMatrixView = (Array2DRowRealMatrix) covarianceMatrix;
            Assert.assertArrayEquals(new double[]{1.5, 0.5}, covarianceMatrixView.getColumnVector(0).toArray());
            Assert.assertArrayEquals(new double[]{0.5, 2.5}, covarianceMatrixView.getColumnVector(1).toArray());
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Test for constructor with two arguments.
     */
    @Test
    public void testTwoArgumentsConstructor() {
        try {
            MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
            Assert.assertArrayEquals(means, distribution.getMeans());
            RealMatrix covarianceMatrix = distribution.getCovariances();
            Assert.assertEquals(2, covarianceMatrix.getColumnDimension());
            Assert.assertEquals(2, covarianceMatrix.getRowDimension());
            Array2DRowRealMatrix covarianceMatrixView = (Array2DRowRealMatrix) covarianceMatrix;
            Assert.assertArrayEquals(new double[]{1.5, 0.5}, covarianceMatrixView.getColumnVector(0).toArray());
            Assert.assertArrayEquals(new double[]{0.5, 2.5}, covarianceMatrixView.getColumnVector(1).toArray());
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Test for density method.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[]{1.2, 2.7};
        double expectedDensity = 0.3321928094595053;
        double density = distribution.density(testValues);
        Assert.assertEquals(expectedDensity, density, 1e-8);
    }

    /**
     * Test for sample method.
     */
    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertArrayEquals(means, sample, 1e-8);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(sample[i] > -1000 && sample[i] < 1000);
        }
    }

    /**
     * Test for getStandardDeviations method.
     */
    @Test
    public void testGetStandardDeviations() {
        double[] standardDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{FastMath.sqrt(1.5), FastMath.sqrt(2.5)}, standardDeviations, 1e-8);
    }

    /**
     * Test for getExponentTerm method.
     */
    @Test
    public void testGetExponentTerm() {
        double[] values = new double[]{1.2, 2.7};
        double expectedExponentTerm = -0.10498359685838298;
        double exponentTerm = distribution.getExponentTerm(values);
        Assert.assertEquals(expectedExponentTerm, exponentTerm, 1e-8);
    }
}