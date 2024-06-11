package org.apache.commons.math.estimation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;

    @Before
    public void setup() {
        estimator = new AbstractEstimator() {

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        
        Assert.assertEquals(100, estimator.maxCostEval);
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.costEvaluations = 10;

        Assert.assertEquals(10, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.jacobianEvaluations = 5;

        Assert.assertEquals(5, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.jacobianEvaluations = 5;

        estimator.incrementJacobianEvaluationsCounter();

        Assert.assertEquals(6, estimator.jacobianEvaluations);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testGetRMS() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testGetChiSquare() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testGetCovariances() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testGuessParametersErrors() {
        Assert.fail("Not implemented");
    }
}