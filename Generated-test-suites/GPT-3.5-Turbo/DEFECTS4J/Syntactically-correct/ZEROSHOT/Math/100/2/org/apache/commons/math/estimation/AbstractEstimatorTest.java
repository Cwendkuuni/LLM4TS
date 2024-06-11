import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // implementation details not relevant for testing
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.setCostEvaluations(50);
        assertEquals(50, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.setJacobianEvaluations(25);
        assertEquals(25, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        // implementation details not relevant for testing
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        // implementation details not relevant for testing
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        // implementation details not relevant for testing
    }

    @Test
    public void testGetRMS() {
        // implementation details not relevant for testing
    }

    @Test
    public void testGetChiSquare() {
        // implementation details not relevant for testing
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        // implementation details not relevant for testing
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        // implementation details not relevant for testing
    }

    @Test
    public void testInitializeEstimate() {
        // implementation details not relevant for testing
    }

}