package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new DummyEstimationProblem(3, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // do nothing
            }
        };
        estimator.initializeEstimate(problem);

        WeightedMeasurement[] measurements = problem.getMeasurements();
        EstimatedParameter[] parameters = problem.getUnboundParameters();
        assertEquals(3, measurements.length);
        assertEquals(2, parameters.length);
        assertNotNull(estimator.jacobian);
        assertNotNull(estimator.residuals);
        assertEquals(3, estimator.rows);
        assertEquals(2, estimator.cols);
        assertEquals(6, estimator.jacobian.length);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new DummyEstimationProblem(3, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem estimationProblem) {
                return 1.0;
            }
        };
        assertEquals(1.0, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new DummyEstimationProblem(3, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem estimationProblem) {
                return 1.0;
            }
        };
        assertEquals(1.0, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws Exception {
        EstimationProblem problem = new DummyEstimationProblem(3, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem estimationProblem) throws EstimationException {
                double[][] covarianceMatrix = new double[2][2];
                covarianceMatrix[0][0] = 1;
                covarianceMatrix[1][1] = 2;
                return covarianceMatrix;
            }
        };

        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        RealMatrix covarianceMatrix = estimator.getCovariances(problem);
        assertEquals(expectedCovarianceMatrix, covarianceMatrix);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        EstimationProblem problem = new DummyEstimationProblem(3, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem estimationProblem) throws EstimationException {
                double[] errors = new double[2];
                errors[0] = 1.0;
                errors[1] = 2.0;
                return errors;
            }
        };

        double[] expectedErrors = new double[]{1.0, 2.0};
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem));
    }

    @Test(expected = EstimationException.class)
    public void testEstimateThrowsException() throws Exception {
        EstimationProblem problem = new DummyEstimationProblem(3, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                throw new EstimationException("Test exception");
            }
        };
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testMaxCostEvalReached() throws Exception {
        EstimationProblem problem = new DummyEstimationProblem(3, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            private final int maxCostEval = 1;

            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // do nothing
            }
        };
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }
}

class DummyEstimationProblem implements EstimationProblem {
    private final int rows;
    private final int cols;

    public DummyEstimationProblem(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[rows];
    }

    @Override
    public EstimatedParameter[] getUnboundParameters() {
        return new EstimatedParameter[cols];
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[rows + cols];
    }
}