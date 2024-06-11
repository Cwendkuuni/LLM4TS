package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
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
                estimator.updateResidualsAndCost();
            }
        };
        assertEquals(0, estimator.getCostEvaluations());

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
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateJacobian();
            }
        };
        assertEquals(0, estimator.getJacobianEvaluations());

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
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateResidualsAndCost();
            }
        };
        estimator.setMaxCostEval(0);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[100];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[1];
            }
        };
        estimator.initializeEstimate(problem);
        for (int i = 0; i < 100; i++) {
            estimator.estimate(problem);
        }
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1.0, 0.1),
                        new WeightedMeasurement(2.0, 1.0),
                        new WeightedMeasurement(3.0, 2.0),
                        new WeightedMeasurement(4.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(0.5), rms, 0.01);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1.0, 0.1),
                        new WeightedMeasurement(2.0, 1.0),
                        new WeightedMeasurement(3.0, 2.0),
                        new WeightedMeasurement(4.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(6.06, chiSquare, 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1.0, 0.1),
                        new WeightedMeasurement(2.0, 1.0),
                        new WeightedMeasurement(3.0, 2.0),
                        new WeightedMeasurement(4.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {
                        new EstimatedParameter("p1", 0.0),
                        new EstimatedParameter("p2", 0.0)
                };
            }
        };
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);
        assertEquals(1.0, covariances[0][0], 0.01);
        assertEquals(0.0, covariances[0][1], 0.01);
        assertEquals(0.0, covariances[1][0], 0.01);
        assertEquals(1.0, covariances[1][1], 0.01);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1.0, 0.1),
                        new WeightedMeasurement(2.0, 1.0),
                        new WeightedMeasurement(3.0, 2.0),
                        new WeightedMeasurement(4.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {
                        new EstimatedParameter("p1", 0.0),
                        new EstimatedParameter("p2", 0.0)
                };
            }
        };
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2, errors.length);
        assertEquals(1.5 * Math.sqrt(0.5), errors[0], 0.01);
        assertEquals(1.5 * Math.sqrt(0.5), errors[1], 0.01);
    }
}