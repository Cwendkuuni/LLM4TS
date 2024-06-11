import org.junit.Test;

public class AbstractEstimatorTest {
  
  @Test
  public void testSetMaxCostEval() {
    AbstractEstimator estimator = new ConcreteEstimator();
    estimator.setMaxCostEval(100);
    assertEquals(100, estimator.maxCostEval);
  }
  
  @Test
  public void testGetCostEvaluations() {
    AbstractEstimator estimator = new ConcreteEstimator();
    estimator.costEvaluations = 50;
    assertEquals(50, estimator.getCostEvaluations());
  }
  
  @Test
  public void testGetJacobianEvaluations() {
    AbstractEstimator estimator = new ConcreteEstimator();
    estimator.jacobianEvaluations = 20;
    assertEquals(20, estimator.getJacobianEvaluations());
  }
  
  @Test
  public void testUpdateJacobian() {
    // Test case not provided
  }
  
  @Test
  public void testIncrementJacobianEvaluationsCounter() {
    AbstractEstimator estimator = new ConcreteEstimator();
    estimator.jacobianEvaluations = 5;
    estimator.incrementJacobianEvaluationsCounter();
    assertEquals(6, estimator.jacobianEvaluations);
  }
  
  @Test
  public void testUpdateResidualsAndCost() throws EstimationException {
    // Test case not provided
  }
  
  @Test
  public void testGetRMS() {
    // Test case not provided
  }
  
  @Test
  public void testGetChiSquare() {
    // Test case not provided
  }
  
  @Test
  public void testGetCovariances() throws EstimationException {
    // Test case not provided
  }
  
  @Test(expected = EstimationException.class)
  public void testGuessParametersErrorsWithException() throws EstimationException {
    AbstractEstimator estimator = new ConcreteEstimator();
    EstimationProblem problem = new ConcreteProblem();
    estimator.guessParametersErrors(problem);
  }
}

class ConcreteEstimator extends AbstractEstimator {
  
  @Override
  public void estimate(EstimationProblem problem) throws EstimationException {
    // implementation not provided
  }
  
}

class ConcreteProblem implements EstimationProblem {
  
  @Override
  public WeightedMeasurement[] getMeasurements() {
    // implementation not provided
  }
  
  @Override
  public EstimatedParameter[] getAllParameters() {
    // implementation not provided
  }
  
}