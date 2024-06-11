package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem(new double[][]{new double[]{1.0, 2.0}, new double[]{3.0, 4.0}}, new double[]{-1.0, -1.0});
        AbstractEstimator estimator = new AbstractEstimator() {};
        estimator.initializeEstimate(problem);

        assertEquals(problem.getMeasurements(), estimator.measurements);
        assertEquals(problem.getUnboundParameters(), estimator.parameters);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() {
        EstimationProblem problem = new EstimationProblem(new double[][]{new double[]{1.0, 2.0}, new double[]{3.0, 4.0}}, new double[]{-1.0, -1.0});
        AbstractEstimator estimator = new AbstractEstimator() {};
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);

        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem(new double[][]{new double[]{1.0, 2.0}, new double[]{3.0, 4.0}}, new double[]{-1.0, -1.0});
        AbstractEstimator estimator = new AbstractEstimator() {};
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();

        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(2.0), rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem(new double[][]{new double[]{1.0, 2.0}, new double[]{3.0, 4.0}}, new double[]{-1.0, -1.0});
        AbstractEstimator estimator = new AbstractEstimator() {};
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();

        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(5.0, chiSquare, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesWithSingularProblem() {
        EstimationProblem problem = new EstimationProblem(new double[][]{new double[]{1.0, 2.0}, new double[]{3.0, 4.0}}, new double[]{-1.0, -1.0, 0.0});
        AbstractEstimator estimator = new AbstractEstimator() {};
        estimator.initializeEstimate(problem);

        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() {
        EstimationProblem problem = new EstimationProblem(new double[][]{new double[]{1.0, 2.0}, new double[]{3.0, 4.0}}, new double[]{-1.0, -1.0});
        AbstractEstimator estimator = new AbstractEstimator() {};
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();

        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[]{Math.sqrt(5.0 / 4.0), Math.sqrt(5.0 / 4.0)}, errors, 0.001);
    }
}