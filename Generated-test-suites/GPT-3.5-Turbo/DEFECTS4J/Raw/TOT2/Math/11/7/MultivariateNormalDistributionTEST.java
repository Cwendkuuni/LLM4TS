import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        
        assertNotNull(mnd);
    }

    // Test case for density method
    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        
        double[] values = {1.5, 3.0};
        double density = mnd.density(values);
        
        assertEquals(0.07978845608028654, density, 1e-10);
    }

    // Test case for sample method
    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);

        double[] sample = mnd.sample();
        
        assertNotNull(sample);
        assertEquals(2, sample.length);
    }

    // Test case for exception handling in constructor
    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 1.0}, {1.0, 1.0}};
        
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
    }

    // Test case for exception handling in density method
    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatchException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        
        double[] values = {1.5};
        mnd.density(values);
    }

    // Test case for edge case in density method
    @Test
    public void testDensityEdgeCase() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1.0, 2.0};
        double density = mnd.density(values);
        
        assertEquals(0.1251643014396304, density, 1e-10);
    }

    // Test case for getMeans method
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);

        double[] meanValues = mnd.getMeans();
        
        assertNotNull(meanValues);
        assertArrayEquals(means, meanValues, 1e-10);
    }

    // Test case for getCovariances method
    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);

        RealMatrix covarianceMatrix = mnd.getCovariances();
        
        assertNotNull(covarianceMatrix);
        assertArrayEquals(covariances[0], covarianceMatrix.getData()[0], 1e-10);
        assertArrayEquals(covariances[1], covarianceMatrix.getData()[1], 1e-10);
    }

    // Test case for getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);

        double[] stdDeviations = mnd.getStandardDeviations();
        
        assertNotNull(stdDeviations);
        double[] expectedStdDeviations = {1.0, Math.sqrt(2.0)};
        assertArrayEquals(expectedStdDeviations, stdDeviations, 1e-10);
    }

    // Test case for getExponentTerm method
    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1.5, 3.0};
        double exponentTerm = mnd.getExponentTerm(values);
        
        assertEquals(2.1556058565884516, exponentTerm, 1e-10);
    }
}