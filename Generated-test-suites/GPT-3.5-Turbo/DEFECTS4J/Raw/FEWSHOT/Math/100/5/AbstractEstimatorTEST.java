package org.apache.commons.math.estimation;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.maxCostEval);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.costEvaluations = 5;
        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.jacobianEvaluations = 3;
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        // TODO: Write test cases for updateJacobian()
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.jacobianEvaluations);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        // TODO: Write test cases for updateResidualsAndCost()
    }

    @Test
    public void testGetRMS() {
        // TODO: Write test cases for getRMS()
    }

    @Test
    public void testGetChiSquare() {
        // TODO: Write test cases for getChiSquare()
    }

    @Test
    public void testGetCovariances() {
        // TODO: Write test cases for getCovariances()
    }

    @Test
    public void testGuessParametersErrors() {
        // TODO: Write test cases for guessParametersErrors()
    }

    @Test
    public void testInitializeEstimate() {
        // TODO: Write test cases for initializeEstimate()
    }

    @Test
    public void testEstimate() {
        // TODO: Write test cases for estimate()
    }

    private static class ConcreteEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            // Do nothing
        }
    }
}