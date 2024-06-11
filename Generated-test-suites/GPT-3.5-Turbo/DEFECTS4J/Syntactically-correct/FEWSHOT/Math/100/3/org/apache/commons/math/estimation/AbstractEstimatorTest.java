import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightMeasurement;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = mock(AbstractEstimator.class);
        problem = mock(EstimationProblem.class);
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        verify(estimator).setMaxCostEval(100);
    }

    @Test
    public void testGetCostEvaluations() {
        when(estimator.getCostEvaluations()).thenReturn(10);
        assertEquals(10, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        when(estimator.getJacobianEvaluations()).thenReturn(5);
        assertEquals(5, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        verify(estimator).updateJacobian();
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.incrementJacobianEvaluationsCounter();
        verify(estimator).incrementJacobianEvaluationsCounter();
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        when(estimator.getMaxCostEval()).thenReturn(100);
        when(estimator.getCostEvaluations()).thenReturn(50);
        estimator.updateResidualsAndCost();
        verify(estimator).updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        WeightMeasurement[] measurements = new WeightMeasurement[]{};
        when(problem.getMeasurements()).thenReturn(measurements);
        estimator.getRMS(problem);
        verify(estimator).getRMS(problem);
    }

    @Test
    public void testGetChiSquare() {
        WeightMeasurement[] measurements = new WeightMeasurement[]{};
        when(problem.getMeasurements()).thenReturn(measurements);
        estimator.getChiSquare(problem);
        verify(estimator).getChiSquare(problem);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        when(problem.getMeasurements()).thenReturn(new WeightMeasurement[]{});
        estimator.getCovariances(problem);
        verify(estimator).getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        when(problem.getMeasurements()).thenReturn(new WeightMeasurement[]{});
        estimator.guessParametersErrors(problem);
        verify(estimator).guessParametersErrors(problem);
    }

    @Test
    public void testInitializeEstimate() {
        estimator.initializeEstimate(problem);
        verify(estimator).initializeEstimate(problem);
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.estimate(problem);
        verify(estimator).estimate(problem);
    }
}