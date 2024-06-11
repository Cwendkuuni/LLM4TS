package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

public class AbstractEstimatorTest {

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 2), new WeightedMeasurement(1, 3, 3) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // Do nothing
            }
        };

        estimator.setMaxCostEval(10);
        assertEquals(1.5, estimator.getRMS(problem), 0.01);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 2), new WeightedMeasurement(1, 3, 3) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // Do nothing
            }
        };

        estimator.setMaxCostEval(10);
        assertEquals(7.0, estimator.getChiSquare(problem), 0.01);
    }

    @Test
    public void testGetCovariancesNull() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // Do nothing
            }
        };

        assertNull(estimator.getCovariances(problem));
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesSingular() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 2) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter(1), new EstimatedParameter(2) };
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // Do nothing
            }
        };

        estimator.setMaxCostEval(10);
        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 2), new WeightedMeasurement(1, 3, 3) };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter(1), new EstimatedParameter(2), new EstimatedParameter(3) };
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // Do nothing
            }
        };

        estimator.setMaxCostEval(10);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(3, errors.length);
        for (int i = 0; i < errors.length; ++i) {
            assertTrue(errors[i] > 0);
        }
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsDegreesOfFreedomNegative() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[2];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[3];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // Do nothing
            }
        };

        estimator.setMaxCostEval(10);
        estimator.guessParametersErrors(problem);
    }
}