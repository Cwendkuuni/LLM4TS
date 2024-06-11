import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

  private AbstractLeastSquaresOptimizer optimizer;

  @Before
  public void setUp() {
    optimizer = new MockOptimizer();
  }

  @Test
  public void testMaxIterations() {
    optimizer.setMaxIterations(100);
    assertEquals(100, optimizer.getMaxIterations());
  }

  @Test
  public void testIterationsCounterIncremented() {
    optimizer.incrementIterationsCounter();
    assertEquals(1, optimizer.getIterations());
    optimizer.incrementIterationsCounter();
    assertEquals(2, optimizer.getIterations());
  }

  @Test(expected = OptimizationException.class)
  public void testIterationsCounterExceeded() {
    optimizer.setMaxIterations(2);
    optimizer.incrementIterationsCounter();
    optimizer.incrementIterationsCounter();
    optimizer.incrementIterationsCounter();
  }

  @Test
  public void testMaxEvaluations() {
    optimizer.setMaxEvaluations(200);
    assertEquals(200, optimizer.getMaxEvaluations());
  }

  @Test
  public void testConvergenceChecker() {
    optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
    assertTrue(optimizer.getConvergenceChecker() instanceof SimpleVectorialValueChecker);
  }

  @Test
  public void testUpdateJacobian() {
    optimizer.jacobian = new double[3][2];
    optimizer.rows = 3;
    optimizer.cols = 2;
    optimizer.updateJacobian();
    // Assert that jacobian has been updated correctly
    // ...
  }

  @Test
  public void testUpdateResidualsAndCost() {
    double[] targetValues = {1.0, 2.0, 3.0};
    double[] residualsWeights = {0.1, 0.2, 0.3};
    double[] point = {0.1, 0.2};
    optimizer.targetValues = targetValues;
    optimizer.residualsWeights = residualsWeights;
    optimizer.point = point;
    optimizer.rows = 3;
    optimizer.residuals = new double[3];
    optimizer.function = new MockFunction();
    optimizer.cost = 0.0;
    optimizer.updateResidualsAndCost();
    // Assert that residuals, cost, and objective have been updated correctly
    // ...
  }

  @Test(expected = FunctionEvaluationException.class)
  public void testUpdateResidualsAndCostExceeded() {
    optimizer.maxEvaluations = 2;
    optimizer.objectiveEvaluations = 2;
    optimizer.updateResidualsAndCost();
  }

  @Test
  public void testGetRMS() {
    double[] residuals = {0.1, 0.2, 0.3};
    double[] residualsWeights = {0.1, 0.2, 0.3};
    optimizer.residuals = residuals;
    optimizer.residualsWeights = residualsWeights;
    optimizer.rows = 3;
    double rms = optimizer.getRMS();
    // Assert that RMS has been computed correctly
    // ...
  }

  @Test
  public void testGetChiSquare() {
    double[] residuals = {0.1, 0.2, 0.3};
    double[] residualsWeights = {0.1, 0.2, 0.3};
    optimizer.residuals = residuals;
    optimizer.residualsWeights = residualsWeights;
    optimizer.rows = 3;
    double chiSquare = optimizer.getChiSquare();
    // Assert that chi-square has been computed correctly
    // ...
  }

  @Test(expected = FunctionEvaluationException.class)
  public void testGetCovariances() {
    optimizer.function = new MockFunction();
    optimizer.rows = 3;
    optimizer.cols = 2;
    optimizer.jacobian = new double[3][2];
    optimizer.targetValues = new double[3];
    optimizer.residualsWeights = new double[3];
    double[][] covariances = optimizer.getCovariances();
    // Assert that covariances matrix has been computed correctly
    // ...
  }

  @Test
  public void testGuessParametersErrors() {
    optimizer.function = new MockFunction();
    optimizer.rows = 3;
    optimizer.cols = 2;
    optimizer.jacobian = new double[3][2];
    optimizer.targetValues = new double[3];
    optimizer.residualsWeights = new double[3];
    double[] errors = optimizer.guessParametersErrors();
    // Assert that errors have been computed correctly
    // ...
  }

  private static class MockOptimizer extends AbstractLeastSquaresOptimizer {
    protected VectorialPointValuePair doOptimize() {
      return null;
    }
  }

  private static class MockFunction implements DifferentiableMultivariateVectorialFunction {
    public double[] value(double[] point) {
      return null;
    }

    public MultivariateMatrixFunction jacobian() {
      return null;
    }
  }

}