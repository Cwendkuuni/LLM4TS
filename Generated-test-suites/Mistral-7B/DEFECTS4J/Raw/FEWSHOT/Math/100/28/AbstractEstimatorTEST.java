package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.junit.Test;

public class AbstractEstimatorTest {

    static final int MAX_COST_EVAL = 10;

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(MAX_COST_EVAL);
        assertEquals(MAX_COST_EVAL, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            @Override
            void estimate(EstimationProblem problem) throws EstimationException {
                ++costEvaluations;
            }
        };
        estimator.estimate(createEstimationProblem());
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;
            @Override
            protected void incrementJacobianEvaluationsCounter() {
                ++jacobianEvaluations;
            }

            @Override
            void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        estimator.estimate(createEstimationProblem());
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;
            double[] jacobian = new double[10];

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                ++jacobianEvaluations;
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
                Arrays.fill(jacobian, 0);
                jacobian[0] = 1;
            }
        };
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
        assertEquals(1, jacobian[0]);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            double cost = 0;
            double[] residuals = new double[1];

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                ++costEvaluations;
                residuals[0] = 1;
                cost = 1;
            }

            @Override
            void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
            }
        };
        estimator.estimate(createEstimationProblem());
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(1, estimator.cost, 0.001);
        assertEquals(1, residuals[0]);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = createEstimationProblem();
        double rms = new AbstractEstimator() {
            @Override
            double getRMS(EstimationProblem problem) {
                return 1;
            }
        }.getRMS(problem);
        assertEquals(1, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = createEstimationProblem();
        double chiSquare = new AbstractEstimator() {
            @Override
            double getChiSquare(EstimationProblem problem) {
                return 1;
            }
        }.getChiSquare(problem);
        assertEquals(1, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = createEstimationProblem();
        RealMatrix covariances = new AbstractEstimator() {
            @Override
            double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                return new double[][]{{1, 0}, {0, 1}};
            }
        }.getCovariances(problem);
        RealMatrix expectedCovariances = new RealMatrixImpl(new double[][]{
                {1, 0},
                {0, 1}
        });
        assertEquals(expectedCovariances, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = createEstimationProblem();
        double[] errors = new AbstractEstimator() {
            @Override
            double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                return new double[]{1, 1};
            }
        }.guessParametersErrors(problem);
        assertArrayEquals(new double[]{1, 1}, errors);
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            WeightedMeasurement[] measurements;
            EstimatedParameter[] parameters;
            double[] jacobian;
            double[] residuals;
            int costEvaluations = 0;
            int jacobianEvaluations = 0;
            double cost = Double.POSITIVE_INFINITY;

            @Override
            void initializeEstimate(EstimationProblem problem) {
                this.measurements = problem.getMeasurements();
                this.parameters = problem.getUnboundParameters();
            }

            @Override
            void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.initializeEstimate(problem);
        assertArrayEquals(problem.getMeasurements(), estimator.measurements);
        assertArrayEquals(problem.getUnboundParameters(), estimator.parameters);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertArrayEquals(new double[rows * cols], estimator.jacobian);
        assertArrayEquals(new double[rows], estimator.residuals);
    }

    @Test
    public void testEstimateThrowsMaxCostEvalExceeded() throws EstimationException {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            @Override
            void estimate(EstimationProblem problem) throws EstimationException {
                ++costEvaluations;
            }

            @Override
            int getMaxCostEval() {
                return 0;
            }
        };
        estimator.setMaxCostEval(1);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createEstimationProblem() {
        double[][] measurementsData = {{1, 2}, {3, 4}, {5, 6}};
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; ++i) {
            measurements[i] = new WeightedMeasurement(new EstimatedParameter(i, 0), measurementsData[i][0], measurementsData[i][1], 1);
        }
        EstimationProblem problem = new EstimationProblem(new EstimatedParameter[0], measurements);
        return problem;
    }
}