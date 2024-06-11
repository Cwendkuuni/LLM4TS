package org.apache.commons.math.estimation;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservedValues;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[]{0, 1});
            }

            @Override
            public RealMatrix getJacobian(double[] parameters) {
                return new Array2DRowRealMatrix(new double[][]{
                        {1, 1},
                        {2, 2}
                });
            }

            @Override
            public WeightedObservedValues getMeasurements() {
                return new WeightedObservedValues(new double[][]{
                        {1, 3},
                        {2, 4}
                }, new double[]{1, 1});
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
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
        estimator.costEvaluations = 5;
        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.jacobianEvaluations = 3;
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() throws EstimationException {
        estimator.maxCostEval = 1;
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        double[] residuals = estimator.residuals;
        RealMatrix jacobian = estimator.jacobian;
        assertEquals(1.4142135625, residuals[0], 0.001);
        assertEquals(1.4142135625, residuals[1], 0.001);
        assertEquals(-0.7071067812, jacobian.getEntry(0, 0), 0.001);
        assertEquals(-0.7071067812, jacobian.getEntry(0, 1), 0.001);
        assertEquals(-0.7071067812, jacobian.getEntry(1, 0), 0.001);
        assertEquals(-0.7071067812, jacobian.getEntry(1, 1), 0.001);
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        assertEquals(2.0, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(5.0, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals(0.4082482905, covariances[0][0], 0.001);
        assertEquals(0.4082482905, covariances[1][1], 0.001);
        assertEquals(-0.0248290463, covariances[0][1], 0.001);
        assertEquals(-0.0248290463, covariances[1][0], 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(1.1180339887, errors[0], 0.001);
        assertEquals(1.1180339887, errors[1], 0.001);
    }
}