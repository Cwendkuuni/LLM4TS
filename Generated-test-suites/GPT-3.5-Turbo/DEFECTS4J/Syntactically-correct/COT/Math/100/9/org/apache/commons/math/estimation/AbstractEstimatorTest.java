import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.AbstractEstimator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                
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
        //TODO: Write test case for this method
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        //TODO: Write test case for this method
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        //TODO: Write test case for this method
    }

    @Test
    public void testGetRMS() {
        //TODO: Write test case for this method
    }

    @Test
    public void testGetChiSquare() {
        //TODO: Write test case for this method
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        //TODO: Write test case for this method
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        //TODO: Write test case for this method
    }

    @Test
    public void testInitializeEstimate() {
        //TODO: Write test case for this method
    }
}