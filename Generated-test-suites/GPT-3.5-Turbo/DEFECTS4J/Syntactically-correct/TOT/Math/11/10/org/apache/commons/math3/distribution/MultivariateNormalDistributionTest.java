import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class MultivariateNormalDistributionTest {
    
    // Test case for creating a MultivariateNormalDistribution object
    @Test
    public void testConstructor() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        try {
            MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
            
            assertNotNull(mvn);
            assertEquals(means.length, mvn.getDimension());
            assertArrayEquals(means, mvn.getMeans(), 0.0001);
            
            RealMatrix expectedCovMat = new Array2DRowRealMatrix(covariances);
            assertEquals(expectedCovMat, mvn.getCovariances());
            
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException was thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("NonPositiveDefiniteMatrixException was thrown");
        }
    }
    
    // Test case for computing the density at a given value
    @Test
    public void testDensity() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        try {
            MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
            
            double[] values = {1, 1};
            double expectedDensity = 0.05854983152431917;
            double actualDensity = mvn.density(values);
            
            assertEquals(expectedDensity, actualDensity, 0.0001);
            
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException was thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("NonPositiveDefiniteMatrixException was thrown");
        }
    }
    
    // Test case for sampling from the distribution
    @Test
    public void testSample() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        
        try {
            MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
            
            double[] sample = mvn.sample();
            
            assertEquals(means.length, sample.length);
            
            assertFalse(Double.isNaN(sample[0]));
            assertFalse(Double.isNaN(sample[1]));
            
        } catch (DimensionMismatchException e) {
            fail("DimensionMismatchException was thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("NonPositiveDefiniteMatrixException was thrown");
        }
    }
}