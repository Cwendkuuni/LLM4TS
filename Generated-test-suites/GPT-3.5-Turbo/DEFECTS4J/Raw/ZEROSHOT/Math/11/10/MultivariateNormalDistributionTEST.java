import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }

        assertNotNull(distribution);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatchException() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0}, {0, 1}, {0, 0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }

        double[] result = distribution.getMeans();

        assertArrayEquals(means, result, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }

        RealMatrix result = distribution.getCovariances();

        double[][] expectedCovariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix expected = new Array2DRowRealMatrix(expectedCovariances);

        assertTrue(result.equals(expected));
    }

    @Test
    public void testDensity() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        double[] values = {0, 0, 0};

        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }

        double result = distribution.density(values);

        double expected = 0.06349363593424099;

        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0.5, 0.5}, {0.5, 1, 0.5}, {0.5, 0.5, 1}};

        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }

        double[] result = distribution.getStandardDeviations();

        double[] expected = {1, 1, 1};

        assertArrayEquals(expected, result, 0.0);
    }

    @Test
    public void testSample() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }

        double[] result = distribution.sample();

        assertNotNull(result);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {0, 0, 0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        double[] values = {0, 0, 0};

        MultivariateNormalDistribution distribution = null;

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException should not be thrown");
        }

        double result = distribution.getExponentTerm(values);

        double expected = 1.0;

        assertEquals(expected, result, 0.0);
    }

}