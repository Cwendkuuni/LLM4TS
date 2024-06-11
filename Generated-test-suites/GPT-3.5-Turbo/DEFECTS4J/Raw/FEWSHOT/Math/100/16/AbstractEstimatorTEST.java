package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {
    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
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
        // TODO: Implement test
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.getJacobianEvaluations());
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        // TODO: Implement test
    }

    @Test
    public void testGetRMS() {
        // TODO: Implement test
    }

    @Test
    public void testGetChiSquare() {
        // TODO: Implement test
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        // TODO: Implement test
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        // TODO: Implement test
    }
}