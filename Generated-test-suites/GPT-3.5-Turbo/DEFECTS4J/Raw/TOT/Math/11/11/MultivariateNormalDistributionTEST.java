import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class MultivariateNormalDistributionTest {
    
    private static final double[] MEANS = {1.0, 2.0, 3.0};
    private static final double[][] COVARIANCES = {{1.0, 0.0, 0.0},
                                                   {0.0, 1.0, 0.0},
                                                   {0.0, 0.0, 1.0}};
    private MultivariateNormalDistribution multivariateNormalDist;
    
    @Before
    public void setUp() throws Exception {
        RandomGenerator rng = new Well19937c();
        multivariateNormalDist = new MultivariateNormalDistribution(rng, MEANS, COVARIANCES);
    }
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        // Invalid input cases
        // empty means array
        double[] emptyMeans = new double[0];
        assertThrows(DimensionMismatchException.class, () -> new MultivariateNormalDistribution(emptyMeans, COVARIANCES));
        
        // empty covariances array
        double[][] emptyCovariances = new double[0][0];
        assertThrows(DimensionMismatchException.class, () -> new MultivariateNormalDistribution(MEANS, emptyCovariances));
        
        // covariances array with different dimensions than means array
        double[][] wrongSizeCovariances = {{1.0, 0.0}, {0.0, 1.0}};
        assertThrows(DimensionMismatchException.class, () -> new MultivariateNormalDistribution(MEANS, wrongSizeCovariances));
        
        // covariances array with invalid values
        double[][] invalidCovariances = {{1.0, -1.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new MultivariateNormalDistribution(MEANS, invalidCovariances));
        
        // Valid input case
        MultivariateNormalDistribution validDist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
        assertNotNull(validDist);
    }
    
    // Test case for getMeans() method
    @Test
    public void testGetMeans() {
        double[] means = multivariateNormalDist.getMeans();
        assertArrayEquals(MEANS, means, 0.0);
    }
    
    // Test case for getCovariances() method
    @Test
    public void testGetCovariances() {
        double[][] covariances = multivariateNormalDist.getCovariances().getData();
        assertArrayEquals(COVARIANCES, covariances);
    }
    
    // Test case for density() method
    @Test
    public void testDensity() {
        // Typical use case
        double[] typicalVals = {1.5, 2.5, 3.5};
        double density = multivariateNormalDist.density(typicalVals);
        double expectedDensity = 0.06349363593424099;
        assertEquals(expectedDensity, density, 0.0);
        
        // Edge case
        double[] edgeVals = {0.0, 0.0, 0.0};
        density = multivariateNormalDist.density(edgeVals);
        expectedDensity = 0.0044318484119380075;
        assertEquals(expectedDensity, density, 0.0);
        
        // Error scenario
        // vals array with different dimension
        double[] wrongSizeVals = {1.5, 2.5};
        assertThrows(DimensionMismatchException.class, () -> multivariateNormalDist.density(wrongSizeVals));
    }
    
    // Test case for getStandardDeviations() method
    @Test
    public void testGetStandardDeviations() {
        double[] stdDeviations = multivariateNormalDist.getStandardDeviations();
        double[] expectedStdDeviations = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedStdDeviations, stdDeviations, 0.0);
    }
    
    // Test case for sample() method
    @Test
    public void testSample() {
        // Typical use case
        double[] sample = multivariateNormalDist.sample();
        assertNotNull(sample);
        assertEquals(MEANS.length, sample.length);
        
        // Error scenario is not tested as the randomness of the sample does not allow precise validation
        
    }
    
}