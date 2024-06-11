import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setup() throws Exception {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1, 2, 3};
        assertArrayEquals(expected, distribution.getMeans(), 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expected);
        assertEquals(expectedMatrix, distribution.getCovariances());
    }

    @Test
    public void testDensity() {
        double[] vals = {2, 3, 4};
        double expected = 0.06349363593424098;
        assertEquals(expected, distribution.density(vals), 0.0001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatch() {
        double[] vals = {2, 3};
        distribution.density(vals);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1, 1, 1};
        assertArrayEquals(expected, distribution.getStandardDeviations(), 0.0001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertEquals(3, sample.length);
    }
}