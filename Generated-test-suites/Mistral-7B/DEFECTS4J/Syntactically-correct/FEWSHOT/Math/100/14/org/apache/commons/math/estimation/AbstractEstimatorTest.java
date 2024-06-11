package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.UnboundEstimationProblem;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 5;

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 5;

            @Override
            protected void updateJacobian() {}
        };
        assertEquals(5, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;
            double[] jacobian = new double[10];
            int rows = 2, cols = 5;

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
                Arrays.fill(jacobian, 0);
                for (int i = 0, index = 0; i < rows; i++) {
                    for (int j = 0; j < cols; ++j) {
                        jacobian[index++] = 1;
                    }
                }
            }

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                ++jacobianEvaluations;
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{0, 0, 0, 0, 0});
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
        assertArrayEquals(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, estimator.jacobian, 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            double cost = Double.POSITIVE_INFINITY;
            double[] residuals = new double[2];

            @Override
            protected void updateResidualsAndCost() throws EstimationException {}

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{0, 0, 0, 0, 0});
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(Math.sqrt(2.0), estimator.cost, 0.01);
        assertArrayEquals(new double[]{-1, -1}, estimator.residuals, 0.01);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 1.0;
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{1, 1, 0, 0, 0});
        assertEquals(1.0, estimator.getRMS(problem), 0.01);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 2.0;
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{1, 1, 0, 0, 0});
        assertEquals(2.0, estimator.getChiSquare(problem), 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                double[][] covariances = new double[2][2];
                covariances[0][0] = 1.0;
                covariances[0][1] = 0.0;
                covariances[1][0] = 0.0;
                covariances[1][1] = 1.0;
                return covariances;
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{1, 1, 0, 0, 0});
        double[][] covariances = estimator.getCovariances(problem);
        assertArrayEquals(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}}, covariances, 0.01);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[]{1.0, 1.0};
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{1, 1, 0, 0, 0});
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[]{1.0, 1.0}, errors, 0.01);
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                measurements = problem.getMeasurements();
                parameters = problem.getUnboundParameters();
                rows = measurements.length;
                cols = parameters.length;
                jacobian = new double[rows * cols];
                residuals = new double[rows];
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{1, 1, 0, 0, 0});
        estimator.initializeEstimate(problem);
        assertArrayEquals(new WeightedMeasurement[]{
            new WeightedMeasurement(0.0, 1.0, 1.0, new double[]{0, 1}),
            new WeightedMeasurement(0.0, 1.0, 1.0, new double[]{2, 3})}, estimator.measurements);
        assertArrayEquals(new EstimatedParameter[]{
            new EstimatedParameter(0.0), new EstimatedParameter(0.0), new EstimatedParameter(0.0), new EstimatedParameter(0.0), new EstimatedParameter(0.0)}, estimator.parameters);
        assertEquals(2, estimator.rows);
        assertEquals(5, estimator.cols);
        assertEquals(20, estimator.jacobian.length);
        assertNull(estimator.cost);
        assertNull(estimator.error);
    }

    @Test
    public void testEstimateWithException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("test estimation exception");
            }
        };
        EstimationProblem problem = new UnboundEstimationProblem(new double[][]{{0, 1}, {2, 3}}, new double[]{1, 1, 0, 0, 0});
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }
}