import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {
  
  private AbstractEstimator estimator;
  
  @Before
  public void setUp() {
    estimator = new AbstractEstimator() {
      @Override
      public void estimate(EstimationProblem problem) throws EstimationException {
        // Do nothing in this test implementation
      }
    };
  }
  
  @Test
  public void testSetMaxCostEval() {
    estimator.setMaxCostEval(100);
    assertEquals(100, estimator.maxCostEval);
  }
  
  @Test
  public void testGetCostEvaluations() {
    estimator.costEvaluations = 10;
    assertEquals(10, estimator.getCostEvaluations());
  }
  
  @Test
  public void testGetJacobianEvaluations() {
    estimator.jacobianEvaluations = 5;
    assertEquals(5, estimator.getJacobianEvaluations());
  }
  
  @Test
  public void testUpdateJacobian() {
    // TODO: Implement test case
  }
  
  @Test
  public void testIncrementJacobianEvaluationsCounter() {
    estimator.jacobianEvaluations = 0;
    estimator.incrementJacobianEvaluationsCounter();
    assertEquals(1, estimator.jacobianEvaluations);
  }
  
  @Test
  public void testUpdateResidualsAndCost() throws EstimationException {
    // TODO: Implement test case
  }
  
  @Test
  public void testGetRMS() {
    // TODO: Implement test case
  }
  
  @Test
  public void testGetChiSquare() {
    // TODO: Implement test case
  }
  
  @Test
  public void testGetCovariances() throws EstimationException {
    // TODO: Implement test case
  }
  
  @Test
  public void testGuessParametersErrors() throws EstimationException {
    // TODO: Implement test case
  }
  
  @Test
  public void testInitializeEstimate() {
    // TODO: Implement test case
  }
  
  @Test
  public void testEstimate() throws EstimationException {
    // TODO: Implement test case
  }
  
}