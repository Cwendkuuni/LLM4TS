package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                costEvaluations++;
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
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }
        };
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobianAndResiduals() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0, jacobianEvaluations = 0;
            double[] jacobian = new double[0];
            double[] residuals = new double[0];

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
                updateJacobian();
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                ++costEvaluations;
                cost = 0;
                for (int i = 0; i < rows; i++) {
                    residuals[i] = Math.sqrt(measurements[i].getWeight()) * measurements[i].getResidual();
                    cost += measurements[i].getWeight() * residuals[i] * residuals[i];
                }
                cost = Math.sqrt(cost);
            }

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                ++jacobianEvaluations;
            }

            @Override
            protected int rows = 0, cols = 0;
            @Override
            protected WeightedMeasurement[] measurements;
            @Override
            protected EstimatedParameter[] parameters;
            @Override
            protected double[] jacobian;
            @Override
            protected double[] residuals;
            @Override
            protected double cost;
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[] {createWeightedMeasurement(1), createWeightedMeasurement(2)};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(1, estimator.getJacobianEvaluations());
        assertArrayEquals(new double[] {-residuals[0], -residuals[1]}, estimator.jacobian, 0.001);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 0;
            }
        };
        assertEquals(0, estimator.getRMS(createEstimationProblem()), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0;
            }
        };
        assertEquals(0, estimator.getChiSquare(createEstimationProblem()), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                return new double[0][0];
            }
        };
        assertNull(estimator.getCovariances(createEstimationProblem()));
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
        assertArrayEquals(new double[0], estimator.guessParametersErrors(createEstimationProblem()));
    }

    @Test
    public void testEstimateThrowsException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("Test Exception");
            }
        };
        assertThrows(EstimationException.class, () -> estimator.estimate(createEstimationProblem()));
    }

    private EstimationProblem createEstimationProblem() {
        WeightedMeasurement wm = createWeightedMeasurement(1);
        return new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {wm};
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };
    }

    private WeightedMeasurement createWeightedMeasurement(double weight) {
        EstimatedParameter parameter = new EstimatedParameter(1);
        return new WeightedMeasurement(new double[] {1}, new double[] {1}, weight, parameter);
    }
}