package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
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
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.estimate(problem);
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.estimate(problem);
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() {
        estimator.estimate(problem);
        estimator.updateResidualsAndCost();
        assertTrue(estimator.getCostEvaluations() > 0);
        assertTrue(estimator.getCost() > 0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostExceedMaxCostEval() {
        estimator.setMaxCostEval(0);
        estimator.estimate(problem);
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        assertTrue(rms > 0);
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        assertTrue(chiSquare > 0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] covariances = estimator.getCovariances(problem);
        assertFalse(Arrays.deepEquals(new double[0][0], covariances));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] errors = estimator.guessParametersErrors(problem);
        assertFalse(Arrays.equals(new double[0], errors));
    }

    @Test
    public void testInitializeEstimate() {
        estimator.initializeEstimate(problem);
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertNull(estimator.cost);
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.estimate(problem);
        assertTrue(Arrays.deepEquals(problem.getAllParameters(), estimator.parameters));
    }

    @Test
    public void testJacobian() {
        estimator.estimate(problem);
        estimator.updateJacobian();
        RealMatrix jacobianMatrix = new Array2DRowRealMatrix(estimator.jacobian);
        RealVector x = problem.getAllParameters();
        RealVector y = problem.getMeasurements()[0].getObservedValue();
        RealVector expectedJacobian = problem.getAllParametersJacobian().transpose().multiply(y);
        assertArrayEquals(expectedJacobian.toArray(), jacobianMatrix.getColumnVector(0).toArray());
    }
}

class TestEstimator extends AbstractEstimator {
    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // empty implementation
    }
}

class TestEstimationProblem implements EstimationProblem {

    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[0];
    }

    @Override
    public EstimatedParameter[] getUnboundParameters() {
        return new EstimatedParameter[0];
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[0];
    }

    @Override
    public double[] getAllParametersInitValue() {
        return new double[0];
    }

    @Override
    public RealMatrix getAllParametersJacobian() {
        return new RealMatrixImpl(new double[0][0]);
    }

}