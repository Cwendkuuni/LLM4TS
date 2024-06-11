import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            public void estimate(EstimationProblem problem) {
                // implementation for testing purposes
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
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        // test implementation specific behavior
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        // test implementation specific behavior
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        // test implementation specific behavior
    }

    @Test
    public void testGetRMS() {
        // test implementation specific behavior
    }

    @Test
    public void testGetChiSquare() {
        // test implementation specific behavior
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        // test implementation specific behavior
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        // test implementation specific behavior
    }
}