import org.junit.Test;
import org.junit.Assert;

public class AbstractEstimatorTest {
  
  @Test
  public void testSetMaxCostEval() {
    AbstractEstimator estimator = new EstimatorImpl();
    estimator.setMaxCostEval(100);
    Assert.assertEquals(100, estimator.getMaxCostEval());
  }
  
  @Test
  public void testGetCostEvaluations() {
    AbstractEstimator estimator = new EstimatorImpl();
    estimator.incrementCostEvaluationsCounter();
    Assert.assertEquals(1, estimator.getCostEvaluations());
  }
  
  @Test
  public void testGetJacobianEvaluations() {
    AbstractEstimator estimator = new EstimatorImpl();
    estimator.incrementJacobianEvaluationsCounter();
    Assert.assertEquals(1, estimator.getJacobianEvaluations());
  }
  
  @Test
  public void testUpdateJacobian() {
    AbstractEstimator estimator = new EstimatorImpl();
    estimator.updateJacobian();
    Assert.assertArrayEquals(new double[0][0], estimator.getJacobian());
  }
  
  @Test
  public void testIncrementJacobianEvaluationsCounter() {
    AbstractEstimator estimator = new EstimatorImpl();
    estimator.incrementJacobianEvaluationsCounter();
    Assert.assertEquals(1, estimator.getJacobianEvaluations());
  }
  
  @Test
  public void testUpdateResidualsAndCost() {
    AbstractEstimator estimator = new EstimatorImpl();
    estimator.updateResidualsAndCost();
    Assert.assertArrayEquals(new double[0][0], estimator.getResiduals());
    Assert.assertEquals(0.0, estimator.getCost(), 0.0);
  }
  
  @Test
  public void testGetRMS() {
    AbstractEstimator estimator = new EstimatorImpl();
    EstimationProblem problem = new ProblemImpl();
    double rms = estimator.getRMS(problem);
    Assert.assertEquals(0.0, rms, 0.0);
  }
  
  @Test
  public void testGetChiSquare() {
    AbstractEstimator estimator = new EstimatorImpl();
    EstimationProblem problem = new ProblemImpl();
    double chiSquare = estimator.getChiSquare(problem);
    Assert.assertEquals(0.0, chiSquare, 0.0);
  }
  
  @Test
  public void testGetCovariances() {
    AbstractEstimator estimator = new EstimatorImpl();
    EstimationProblem problem = new ProblemImpl();
    double[][] covariances = estimator.getCovariances(problem);
    Assert.assertArrayEquals(new double[0][0], covariances);
  }
  
  @Test
  public void testGuessParametersErrors() {
    AbstractEstimator estimator = new EstimatorImpl();
    EstimationProblem problem = new ProblemImpl();
    double[] errors = estimator.guessParametersErrors(problem);
    Assert.assertArrayEquals(new double[0], errors, 0.0);
  }
  
  @Test
  public void testInitializeEstimate() {
    AbstractEstimator estimator = new EstimatorImpl();
    EstimationProblem problem = new ProblemImpl();
    estimator.initializeEstimate(problem);
    Assert.assertEquals(0, estimator.getCostEvaluations());
    Assert.assertEquals(0, estimator.getJacobianEvaluations());
    Assert.assertArrayEquals(new double[0][0], estimator.getJacobian());
    Assert.assertArrayEquals(new double[0], estimator.getResiduals());
    Assert.assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 0.0);
  }
  
  @Test
  public void testAbstractEstimator() {
    AbstractEstimator estimator = new EstimatorImpl();
  }
  
  @Test
  public void testEstimate() {
    AbstractEstimator estimator = new EstimatorImpl();
    EstimationProblem problem = new ProblemImpl();
    estimator.estimate(problem);
  }
}