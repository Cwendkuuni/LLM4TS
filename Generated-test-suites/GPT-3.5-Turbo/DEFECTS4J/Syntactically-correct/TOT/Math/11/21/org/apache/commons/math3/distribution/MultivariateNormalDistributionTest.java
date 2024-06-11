import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MultivariateNormalDistributionTest {

    @Mock
    private RandomGenerator rng;

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorWithValidInputs() throws Exception {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double[] expectedMeans = {1.0, 2.0, 3.0};
        double[][] expectedCovariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        RealMatrix expectedCovarianceMatrix = MathArrays.createRealMatrix(expectedCovariances);

        assertArrayEquals(expectedMeans, distribution.getMeans(), 0.0);
        assertArrayEquals(expectedCovariances, distribution.getCovariances().getData());
        assertEquals(expectedCovarianceMatrix, distribution.getCovariances());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentMeansAndCovariances() throws Exception {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeEigenvalues() throws Exception {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test
    public void testDensity() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        double[] vals = {2.0, 3.0};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double expectedDensity = 0.03272916586041062;
        assertEquals(expectedDensity, distribution.density(vals), 1e-9);
    }

    @Test
    public void testSampling() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double[] vals = distribution.sample();

        assertNotNull(vals);
        assertEquals(2, vals.length);
    }

    @Test
    public void testGetMeans() throws Exception {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double[] expectedMeans = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedMeans, distribution.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() throws Exception {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double[][] expectedCovariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        assertArrayEquals(expectedCovariances, distribution.getCovariances().getData());
    }

    @Test
    public void testGetStandardDeviations() throws Exception {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 4.0, 0.0}, {0.0, 0.0, 9.0}};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double[] expectedStandardDeviations = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 0.0);
    }

    @Test
    public void testGetExponentTerm() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        double[] values = {2.0, 3.0};

        distribution = new MultivariateNormalDistribution(rng, means, covariances);

        double expectedExponentTerm = 0.310042668;

        assertEquals(expectedExponentTerm, distribution.getExponentTerm(values), 1e-9);
    }
}