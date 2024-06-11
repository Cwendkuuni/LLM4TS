package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                ++jacobianEvaluations;
            }
        };
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxEvaluationsReached() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                setMaxCostEval(1);
                updateResidualsAndCost();
            }
        };
        estimator.setMaxCostEval(1);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 1;
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };
        assertEquals(1, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 1;
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };
        assertEquals(1, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem)
              throws EstimationException {
                return new double[0][];
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };
        assertNull(estimator.getCovariances(problem));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem)
              throws EstimationException {
                return new double[0];
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };
        assertArrayEquals(new double[0], estimator.guessParametersErrors(problem));
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // not implemented
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };

        estimator.initializeEstimate(problem);

        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }
}