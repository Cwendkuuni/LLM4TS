import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing for testing purposes
            }
        };
        problem = new EstimationProblem();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
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
        estimator.updateJacobian();
        double[] jacobian = estimator.getJacobian();
        assertNotNull(jacobian);
        assertEquals(estimator.getRows() * estimator.getCols(), jacobian.length);
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostExceedsMaxCostEval() throws EstimationException {
        estimator.setMaxCostEval(1);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{};
        problem.setMeasurements(measurements);
        double rms = estimator.getRMS(problem);
        assertEquals(0, rms, 0);
    }

    @Test
    public void testGetChiSquare() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{};
        problem.setMeasurements(measurements);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(0, chiSquare, 0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{};
        problem.setMeasurements(measurements);
        double[][] covariances = estimator.getCovariances(problem);
        assertNotNull(covariances);
        assertEquals(estimator.getCols(), covariances.length);
        assertEquals(estimator.getCols(), covariances[0].length);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesSingularProblem() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{};
        problem.setMeasurements(measurements);
        estimator.updateJacobian();
        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{};
        problem.setMeasurements(measurements);
        double[] parametersErrors = estimator.guessParametersErrors(problem);
        assertNotNull(parametersErrors);
        assertEquals(estimator.getCols(), parametersErrors.length);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsNoDegreesOfFreedom() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{};
        problem.setMeasurements(measurements);
        estimator.guessParametersErrors(problem);
    }

    @Test
    public void testInitializeEstimate() {
        estimator.initializeEstimate(problem);
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertNotNull(estimator.getMeasurements());
        assertNotNull(estimator.getParameters());
        assertEquals(estimator.getMeasurements().length, estimator.getRows());
        assertEquals(estimator.getParameters().length, estimator.getCols());
        assertNotNull(estimator.getJacobian());
        assertEquals(estimator.getRows() * estimator.getCols(), estimator.getJacobian().length);
        assertNotNull(estimator.getResiduals());
        assertEquals(estimator.getRows(), estimator.getResiduals().length);
        assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 0);
    }

    @Test
    public void testAbstractEstimator() {
        assertNotNull(estimator);
    }
}