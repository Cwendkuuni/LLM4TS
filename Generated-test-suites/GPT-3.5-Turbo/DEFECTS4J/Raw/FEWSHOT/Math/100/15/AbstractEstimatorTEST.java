import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {
  
  @Test
  public void testSetMaxCostEval() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    estimator.setMaxCostEval(100);
    assertEquals(100, estimator.getMaxCostEval());
  }
  
  @Test
  public void testGetCostEvaluations() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    estimator.incrementCostEvaluations();
    assertEquals(1, estimator.getCostEvaluations());
  }
  
  @Test
  public void testGetJacobianEvaluations() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    estimator.incrementJacobianEvaluations();
    assertEquals(1, estimator.getJacobianEvaluations());
  }
  
  @Test
  public void testUpdateJacobian() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    estimator.updateJacobian();
    // assert jacobian matrix is updated correctly
    
  }
  
  @Test
  public void testUpdateResidualsAndCost() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    estimator.updateResidualsAndCost();
    // assert residuals array and cost value are updated correctly
    
  }
  
  @Test
  public void testGetRMS() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    EstimationProblem problem = new EstimationProblem();
    double rms = estimator.getRMS(problem);
    // assert rms value is correct
    
  }
  
  @Test
  public void testGetChiSquare() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    EstimationProblem problem = new EstimationProblem();
    double chiSquare = estimator.getChiSquare(problem);
    // assert chiSquare value is correct
    
  }
  
  @Test
  public void testGetCovariances() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    EstimationProblem problem = new EstimationProblem();
    double[][] covariances = estimator.getCovariances(problem);
    // assert covariances matrix is correct
    
  }
  
  @Test
  public void testGuessParametersErrors() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    EstimationProblem problem = new EstimationProblem();
    double[] errors = estimator.guessParametersErrors(problem);
    // assert errors array is correct
    
  }
  
  @Test
  public void testInitializeEstimate() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    EstimationProblem problem = new EstimationProblem();
    estimator.initializeEstimate(problem);
    // assert the estimator fields are initialized correctly
    
  }
  
  @Test
  public void testEstimate() {
    AbstractEstimator estimator = new AbstractEstimator() {
      public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not needed for this test
      }
    };
    EstimationProblem problem = new EstimationProblem();
    estimator.estimate(problem);
    // assert the estimate method is implemented correctly
    
  }
  
}