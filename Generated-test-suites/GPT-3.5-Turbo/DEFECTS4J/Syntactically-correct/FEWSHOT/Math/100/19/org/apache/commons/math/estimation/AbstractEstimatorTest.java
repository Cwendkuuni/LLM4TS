import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {
    
    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new TestEstimator();
        problem = new TestEstimationProblem();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() throws EstimationException {
        estimator.estimate(problem);
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() throws EstimationException {
        estimator.estimate(problem);
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        // assert statements
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.incrementJacobianEvaluationsCounter();
        // assert statements
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.updateResidualsAndCost();
        // assert statements
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        // assert statements
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        // assert statements
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] covariances = estimator.getCovariances(problem);
        // assert statements
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] errors = estimator.guessParametersErrors(problem);
        // assert statements
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.estimate(problem);
        // assert statements
    }

    private class TestEstimator extends AbstractEstimator {
        public void estimate(EstimationProblem problem) throws EstimationException {
            // estimation logic
        }
    }

    private class TestEstimationProblem implements EstimationProblem {
        public WeightedMeasurement[] getMeasurements() {
            // return measurements
        }

        public EstimatedParameter[] getAllParameters() {
            // return parameters
        }
    }

}