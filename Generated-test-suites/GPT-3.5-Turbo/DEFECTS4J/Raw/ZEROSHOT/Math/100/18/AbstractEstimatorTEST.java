import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {

  @Test
  public void testSetMaxCostEval() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    estimator.setMaxCostEval(100);
    assertEquals(100, estimator.maxCostEval);
  }

  @Test
  public void testGetCostEvaluations() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    assertEquals(0, estimator.getCostEvaluations());
  }

  @Test
  public void testGetJacobianEvaluations() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    assertEquals(0, estimator.getJacobianEvaluations());
  }

  @Test
  public void testUpdateJacobian() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    estimator.updateJacobian();
    // Add assertions here
  }

  @Test
  public void testIncrementJacobianEvaluationsCounter() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    estimator.incrementJacobianEvaluationsCounter();
    // Add assertions here
  }

  @Test
  public void testUpdateResidualsAndCost() throws EstimationException {
    AbstractEstimator estimator = new TestAbstractEstimator();
    estimator.updateResidualsAndCost();
    // Add assertions here
  }

  @Test
  public void testGetRMS() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    EstimationProblem problem = new TestEstimationProblem();
    double rms = estimator.getRMS(problem);
    // Add assertions here
  }

  @Test
  public void testGetChiSquare() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    EstimationProblem problem = new TestEstimationProblem();
    double chiSquare = estimator.getChiSquare(problem);
    // Add assertions here
  }

  @Test
  public void testGetCovariances() throws EstimationException {
    AbstractEstimator estimator = new TestAbstractEstimator();
    EstimationProblem problem = new TestEstimationProblem();
    double[][] covariances = estimator.getCovariances(problem);
    // Add assertions here
  }

  @Test
  public void testGuessParametersErrors() throws EstimationException {
    AbstractEstimator estimator = new TestAbstractEstimator();
    EstimationProblem problem = new TestEstimationProblem();
    double[] errors = estimator.guessParametersErrors(problem);
    // Add assertions here
  }

  @Test
  public void testInitializeEstimate() {
    AbstractEstimator estimator = new TestAbstractEstimator();
    EstimationProblem problem = new TestEstimationProblem();
    estimator.initializeEstimate(problem);
    // Add assertions here
  }

  @Test
  public void testEstimate() throws EstimationException {
    AbstractEstimator estimator = new TestAbstractEstimator();
    EstimationProblem problem = new TestEstimationProblem();
    estimator.estimate(problem);
    // Add assertions here
  }

  private class TestAbstractEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
      // TODO Auto-generated method stub
      
    }

  }

  private class TestEstimationProblem implements EstimationProblem {

    @Override
    public WeightedMeasurement[] getMeasurements() {
      // TODO Auto-generated method stub
      return null;
      }

    @Override
    public EstimatedParameter[] getUnboundParameters() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
      // TODO Auto-generated method stub
      return null;
    }

  }
}