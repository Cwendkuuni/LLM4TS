import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution1;
    private MultivariateNormalDistribution distribution2;
    private MultivariateNormalDistribution distribution3;

    @Before
    public void setUp() throws Exception {
        double[] means1 = {1, 2, 3};
        double[][] covariances1 = {
            {2, 0, 0},
            {0, 3, 0},
            {0, 0, 4}
        };
        distribution1 = new MultivariateNormalDistribution(means1, covariances1);

        double[] means2 = {0, 0};
        double[][] covariances2 = {
            {1, 0},
            {0, 1}
        };
        distribution2 = new MultivariateNormalDistribution(means2, covariances2);

        double[] means3 = {5};
        double[][] covariances3 = {
            {9}
        };
        distribution3 = new MultivariateNormalDistribution(means3, covariances3);
    }

    @After
    public void tearDown() throws Exception {
        distribution1 = null;
        distribution2 = null;
        distribution3 = null;
    }

    // Test cases for density() method

    @Test
    public void testDensity_ValidValues() {
        double[] values = {1, 2, 3};
        double expected = 0.01616469;
        double actual = distribution1.density(values);
        assertEquals(expected, actual, 1e-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensity_InvalidDimension() {
        double[] values = {1, 2};
        distribution2.density(values);
    }

    // Test cases for getStandardDeviations() method

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {Math.sqrt(2), Math.sqrt(3), Math.sqrt(4)};
        double[] actual = distribution1.getStandardDeviations();
        assertArrayEquals(expected, actual, 1e-5);
    }

    // Test cases for sample() method

    @Test
    public void testSample_ValidValues() {
        double[] sample = distribution1.sample();
        assertNotNull(sample);
        assertEquals(distribution1.getDimension(), sample.length);
    }

    // Test cases for getMeans() method

    @Test
    public void testGetMeans_ValidValues() {
        double[] expected = {1, 2, 3};
        double[] actual = distribution1.getMeans();
        assertArrayEquals(expected, actual, 1e-5);
    }

    // Test cases for getCovariances() method

    @Test
    public void testGetCovariances_ValidValues() {
        double[][] expected = {
            {2, 0, 0},
            {0, 3, 0},
            {0, 0, 4}
        };
        double[][] actual = distribution1.getCovariances().getData();
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], 1e-5);
        }
    }

    // Test cases for constructor

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatchException() {
        double[] means = {1, 2, 3};
        double[][] covariances = {
            {2, 0, 0},
            {0, 3, 0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrixException() {
        double[] means = {1, 2};
        double[][] covariances = {
            {1, -1},
            {-1, 1}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NonPositiveDimension() {
        double[] means = {1, 2, 3};
        double[][] covariances = {
            {2, 0, 0},
            {0, 3, 0},
            {0, 0, 4}
        };
        new MultivariateNormalDistribution(means, covariances).sample(-1);
    }

}