import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class MultivariateNormalDistributionTest {
  
  // Test constructor
  @Test
  public void testConstructor() {
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
    
    try {
      MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (SingularMatrixException e) {
      e.printStackTrace();
      fail("SingularMatrixException occurred");
    } catch (DimensionMismatchException e) {
      e.printStackTrace();
      fail("DimensionMismatchException occurred");
    } catch (NonPositiveDefiniteMatrixException e) {
      e.printStackTrace();
      fail("NonPositiveDefiniteMatrixException occurred");
    }
  }
  
  // TODO: Add more test cases for other public methods
  
}