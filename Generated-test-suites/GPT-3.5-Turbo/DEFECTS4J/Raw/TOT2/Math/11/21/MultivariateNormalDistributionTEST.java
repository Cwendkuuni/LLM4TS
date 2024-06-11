import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.3}, {0.5, 1.0, 0.4}, {0.3, 0.4, 1.0}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
        
        assertArrayEquals(means, distribution.getMeans(), 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.3}, {0.5, 1.0, 0.4}, {0.3, 0.4, 1.0}};
        
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
        
        RealMatrix expectedCov = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCov, distribution.getCovariances());
    }

    // Add more test cases for other methods

}