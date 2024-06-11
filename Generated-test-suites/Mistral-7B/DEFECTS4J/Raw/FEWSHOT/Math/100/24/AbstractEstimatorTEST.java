package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }
        };
        estimator.initializeEstimate(problem);

        double rms = estimator.getRMS(problem);
        assertEquals(0.0, rms, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }
        };
        estimator.initializeEstimate(problem);

        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(0.0, chiSquare, 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }
        };
        estimator.initializeEstimate(problem);

        double[][] covariances = estimator.getCovariances(problem);
        assertNull(covariances);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsWithNegativeDegreesOfFreedom() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[1];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[2];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }
        };
        estimator.initializeEstimate(problem);

        estimator.guessParametersErrors(problem);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeMaxCostEval() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[1];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }

            @Override
            public void setMaxCostEval(int maxCostEval) {
                super.setMaxCostEval(maxCostEval - 1);
            }
        };
        estimator.initializeEstimate(problem);

        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithMaxCostEvalReached() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[Integer.MAX_VALUE];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }
        };
        estimator.initializeEstimate(problem);

        estimator.estimate(problem);
    }

    @Test
    public void testGetCovariancesPositiveDegreesOfFreedom() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {new WeightedMeasurement(new double[] {1.0, 2.0}, 1.0), new WeightedMeasurement(new double[] {2.0, 3.0}, 1.0)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter(1), new EstimatedParameter(2)};
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }
        };
        estimator.initializeEstimate(problem);

        double[][] covariances = estimator.getCovariances(problem);

        RealMatrix expectedCovariances = new RealMatrixImpl(new double[][] {
            {1.0, 0.5},
            {0.5, 1.0}
        });

        assertEquals(expectedCovariances, new RealMatrixImpl(covariances));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {new WeightedMeasurement(new double[] {1.0, 2.0}, 1.0), new WeightedMeasurement(new double[] {3.0, 2.0}, 1.0)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter(1), new EstimatedParameter(2)};
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // Empty body
            }
        };
        estimator.initializeEstimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);

        double expectedError = 1.0;
        for (int i = 0; i < errors.length; i++) {
            assertEquals(expectedError, errors[i], 0.01);
            expectedError *= Math.sqrt(2.0);
        }
    }
}