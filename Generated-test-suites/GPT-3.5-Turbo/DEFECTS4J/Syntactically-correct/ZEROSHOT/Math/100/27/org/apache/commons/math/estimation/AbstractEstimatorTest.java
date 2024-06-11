import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class AbstractEstimatorTest {

  private AbstractEstimator estimator;

  @Before
  public void setUp() {
    estimator = new AbstractEstimator() {
      @Override
      public void estimate(EstimationProblem problem) throws EstimationException {
        // Do nothing for testing purposes
      }
    };
  }

  @Test
  public void testSetMaxCostEval() {
    estimator.setMaxCostEval(10);
    assertEquals(10, estimator.getMaxCostEval());
  }

  @Test
  public void testGetCostEvaluations() {
    assertEquals(0, estimator.getCostEvaluations());
  }

  @Test
  public void testGetJacobianEvaluations() {
    assertEquals(0, estimator.getJacobianEvaluations());
  }

  @Test
  public void testUpdateJacobian() {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(1.0);
    Mockito.when(measurement.getPartial(Mockito.any(EstimatedParameter.class))).thenReturn(1.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});

    estimator.initializeEstimate(problem);

    // Act
    estimator.updateJacobian();

    // Assert
    assertEquals(1.0, estimator.jacobian[0], 0.0);
  }

  @Test
  public void testIncrementJacobianEvaluationsCounter() {
    estimator.incrementJacobianEvaluationsCounter();
    assertEquals(1, estimator.getJacobianEvaluations());
  }

  @Test
  public void testUpdateResidualsAndCost() throws EstimationException {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(1.0);
    Mockito.when(measurement.getResidual()).thenReturn(2.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});

    estimator.initializeEstimate(problem);

    // Act
    estimator.updateResidualsAndCost();

    // Assert
    assertEquals(2.0, estimator.residuals[0], 0.0);
    assertEquals(4.0, estimator.cost, 0.0);
  }

  @Test(expected = EstimationException.class)
  public void testUpdateResidualsAndCost_MaxEvalExceeded() throws EstimationException {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(1.0);
    Mockito.when(measurement.getResidual()).thenReturn(2.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});

    estimator.initializeEstimate(problem);
    estimator.setMaxCostEval(0);

    // Act
    estimator.updateResidualsAndCost();
  }

  @Test
  public void testGetRMS() {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(1.0);
    Mockito.when(measurement.getResidual()).thenReturn(2.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});

    // Act
    double rms = estimator.getRMS(problem);

    // Assert
    assertEquals(2.0, rms, 0.0);
  }

  @Test
  public void testGetChiSquare() {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(1.0);
    Mockito.when(measurement.getResidual()).thenReturn(2.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});

    // Act
    double chiSquare = estimator.getChiSquare(problem);

    // Assert
    assertEquals(4.0, chiSquare, 0.0);
  }

  @Test(expected = EstimationException.class)
  public void testGetCovariances_SingularProblem() throws EstimationException {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(0.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});

    // Act
    estimator.getCovariances(problem);
  }

  @Test
  public void testGuessParametersErrors() throws EstimationException {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(1.0);
    Mockito.when(measurement.getResidual()).thenReturn(2.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});
    Mockito.when(problem.getAllParameters()).thenReturn(new EstimatedParameter[]{});

    // Act
    double[] errors = estimator.guessParametersErrors(problem);

    // Assert
    assertEquals(0, errors.length);
  }

  @Test(expected = EstimationException.class)
  public void testGuessParametersErrors_NoDegreesOfFreedom() throws EstimationException {
    // Arrange
    WeightedMeasurement measurement = Mockito.mock(WeightedMeasurement.class);
    Mockito.when(measurement.getWeight()).thenReturn(0.0);

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[]{measurement});
    Mockito.when(problem.getAllParameters()).thenReturn(new EstimatedParameter[]{});

    // Act
    estimator.guessParametersErrors(problem);
  }

  @Test
  public void testInitializeEstimate() {
    // Arrange
    WeightedMeasurement[] measurements = new WeightedMeasurement[3];
    EstimatedParameter[] parameters = new EstimatedParameter[2];

    EstimationProblem problem = Mockito.mock(EstimationProblem.class);
    Mockito.when(problem.getMeasurements()).thenReturn(measurements);
    Mockito.when(problem.getUnboundParameters()).thenReturn(parameters);

    // Act
    estimator.initializeEstimate(problem);

    // Assert
    assertEquals(0, estimator.getCostEvaluations());
    assertEquals(0, estimator.getJacobianEvaluations());
    assertEquals(measurements, estimator.measurements);
    assertEquals(parameters, estimator.parameters);
    assertEquals(2, estimator.cols);
    assertEquals(3, estimator.rows);
    assertEquals(6, estimator.jacobian.length);
    assertEquals(3, estimator.residuals.length);
    assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 0.0);
  }
}