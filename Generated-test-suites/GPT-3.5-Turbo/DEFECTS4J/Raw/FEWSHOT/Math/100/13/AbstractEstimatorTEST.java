import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.Estimator;
import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.WeightMeasurement;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private EstimationProblem problem;
    private Estimator estimator;

    @Before
    public void setup() {
        problem = new EstimationProblem();
        estimator = new TestEstimator();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        int costEvaluations = estimator.getCostEvaluations();
        assertEquals(0, costEvaluations);
    }

    @Test
    public void testGetJacobianEvaluations() {
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        assertEquals(0, jacobianEvaluations);
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        assertNotNull(rms);
        assertTrue(Double.isFinite(rms));
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        assertNotNull(chiSquare);
        assertTrue(Double.isFinite(chiSquare));
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] covariances = estimator.getCovariances(problem);
        assertNotNull(covariances);
        assertEquals(problem.getAllParameters().length, covariances.length);
        for (double[] row : covariances) {
            assertEquals(problem.getAllParameters().length, row.length);
        }
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] errors = estimator.guessParametersErrors(problem);
        assertNotNull(errors);
        assertEquals(problem.getAllParameters().length, errors.length);
        for (double error : errors) {
            assertTrue(Double.isFinite(error));
        }
    }

    @Test
    public void testInitializeEstimate() {
        estimator.initializeEstimate(problem);
        double[] initialParameters = problem.getAllParameters();
        assertNotNull(initialParameters);
        assertEquals(problem.getAllParameters().length, initialParameters.length);
    }

    @Test
    public void testEstimate() {
        double[] initialParameters = problem.getAllParameters();
        estimator.estimate(problem);
        double[] estimatedParameters = problem.getAllParameters();
        assertArrayEquals(initialParameters, estimatedParameters, 0.001);
    }

    private class TestEstimator extends AbstractEstimator {

        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            // Do nothing
        }
    }

}