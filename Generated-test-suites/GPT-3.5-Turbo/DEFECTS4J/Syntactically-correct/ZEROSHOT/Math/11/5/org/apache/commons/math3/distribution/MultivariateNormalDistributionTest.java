import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultivariateNormalDistributionTest {

    // Test Constructor with valid input
    @Test
    public void testValidConstructor() {
        double[] means = {1, 2};
        double[][] covariances = {{2, 1}, {1, 3}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            assertTrue(false);
        }
        assertEquals(mnd.getDimension(), means.length);
        assertArrayEquals(mnd.getMeans(), means, 0.0001);
        assertTrue(mnd.getCovariances().equals(new Array2DRowRealMatrix(covariances)));
    }

    // Test Constructor with invalid input - DimensionMismatchException
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructor1() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{2, 1}, {1, 3}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(rng, means, covariances);
    }

    // Test Constructor with invalid input - SingularMatrixException
    @Test(expected = SingularMatrixException.class)
    public void testInvalidConstructor2() {
        double[] means = {1, 2};
        double[][] covariances = {{2, 1}, {1, 2}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(rng, means, covariances);
    }

    // Test Constructor with invalid input - NonPositiveDefiniteMatrixException
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidConstructor3() {
        double[] means = {1, 2};
        double[][] covariances = {{-2, 1}, {1, 3}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(rng, means, covariances);
    }

    // Test density method with valid input
    @Test
    public void testDensity() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            assertTrue(false);
        }
        double[] vals = {1, 1};
        double density = mnd.density(vals);
        assertEquals(density, 0.058549831, 0.000000001);
    }

    // Test getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            assertTrue(false);
        }
        double[] stdDeviations = mnd.getStandardDeviations();
        double[] expected = {1, 1};
        assertArrayEquals(stdDeviations, expected, 0.000000001);
    }

    // Test sample method
    @Test
    public void testSample() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            assertTrue(false);
        }
        double[] sample = mnd.sample();
        assertTrue(sample.length == means.length);
        for (int i = 0; i < sample.length; i++) {
            assertTrue(sample[i] >= Double.NEGATIVE_INFINITY && sample[i] <= Double.POSITIVE_INFINITY);
        }
    }

    // Test getExponentTerm method
    @Test
    public void testGetExponentTerm() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            assertTrue(false);
        }
        double[] vals = {1, 1};
        double exponentTerm = mnd.getExponentTerm(vals);
        assertEquals(exponentTerm, 0.01831563888, 0.0000000001);
    }

}