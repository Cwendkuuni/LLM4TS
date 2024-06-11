package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                counter++;
            }
        };
        estimator.estimate(new EstimationProblem() {
            // empty
        });
        estimator.estimate(new EstimationProblem() {
            // empty
        });
        assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateJacobian() {
                counter++;
            }
        };
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianIndex = 0;

            @Override
            protected void updateJacobian() {
                estimator.jacobian[jacobianIndex++] = 1;
            }
        };
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertArrayEquals(new double[] {1, 1}, estimator.jacobian);
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                counter++;
            }

            @Override
            protected void updateJacobian() {
                // not called
            }
        };
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            double cost = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                cost += 1;
            }
        };
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
        assertEquals(2 * Math.sqrt(2), estimator.cost, 0.001);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement() {
                    double weight = 1;
                    double residual = -1;
                },
                new WeightedMeasurement() {
                    double weight = 4;
                    double residual = 1;
                }
            };

            EstimatedParameter[] parameters = new EstimatedParameter[] {};
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return Math.sqrt(problem.getMeasurements().length) * Math.sqrt(super.getChiSquare(problem) / problem.getMeasurements().length);
            }
        };
        assertEquals(Math.sqrt(5 / 2), estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement() {
                    double weight = 1;
                    double residual = -1;
                },
                new WeightedMeasurement() {
                    double weight = 4;
                    double residual = 1;
                }
            };

            EstimatedParameter[] parameters = new EstimatedParameter[] {};
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                double sumSquareResiduals = 0;
                for (WeightedMeasurement wm : problem.getMeasurements()) {
                    sumSquareResiduals += wm.getResidual() * wm.getResidual() / wm.getWeight();
                }
                return sumSquareResiduals;
            }
        };
        assertEquals(11 / 2, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement() {
                    double weight = 1;
                    double residual = 1;
                    double[] partials = new double[] {1, 2};
                },
                new WeightedMeasurement() {
                    double weight = 1;
                    double residual = 1;
                    double[] partials = new double[] {2, 1};
                }
            };

            EstimatedParameter[] parameters = new EstimatedParameter[] {
                new EstimatedParameter() {
                    String name = "p1";
                    double initialValue = 0;
                    double initialVariance = 1;
                },
                new EstimatedParameter() {
                    String name = "p2";
                    double initialValue = 0;
                    double initialVariance = 1;
                }
            };

            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                double[][] jTj = new double[2][2];
                for (int i = 0; i < 2; ++i) {
                    for (int j = 0; j < 2; ++j) {
                        double sum = 0;
                        for (int k = 0; k < 4; ++k) {
                            sum += problem.jacobian[k] * problem.jacobian[k + i * 2 + j];
                        }
                        jTj[i][j] = sum;
                    }
                }
                return jTj;
            }
        };
        RealMatrix jacobian = new Array2DRowRealMatrix(new double[][] {
            {1, 2},
            {2, 1}
        });
        RealMatrix covarianceMatrix = estimator.getCovariances(problem);
        assertEquals(jacobian.transpose().multiply(jacobian).getEntry(0, 0), covarianceMatrix.getEntry(0, 0), 0.001);
        assertEquals(jacobian.transpose().multiply(jacobian).getEntry(1, 1), covarianceMatrix.getEntry(1, 1), 0.001);
        assertEquals(jacobian.transpose().multiply(jacobian).getEntry(0, 1), covarianceMatrix.getEntry(0, 1), 0.001);
        assertEquals(jacobian.transpose().multiply(jacobian).getEntry(1, 0), covarianceMatrix.getEntry(1, 0), 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement() {
                    double weight = 1;
                    double residual = 1;
                },
                new WeightedMeasurement() {
                    double weight = 4;
                    double residual = 1;
                }
            };

            EstimatedParameter[] parameters = new EstimatedParameter[] {
                new EstimatedParameter() {
                    String name = "p1";
                    double initialValue = 0;
                    double initialVariance = 1;
                },
                new EstimatedParameter() {
                    String name = "p2";
                    double initialValue = 0;
                    double initialVariance = 1;
                }
            };

            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                double mad = Math.sqrt(problem.getChiSquare(problem) / (problem.getMeasurements().length - problem.getAllParameters().length));
                double[] errors = new double[2];
                for (EstimatedParameter parameter : problem.getAllParameters()) {
                    errors[parameter.getIndex()] = mad * Math.sqrt(problem.getCovariances(problem)[parameter.getIndex()][parameter.getIndex()]);
                }
                return errors;
            }
        };
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(Math.sqrt(11 / 6), errors[0], 0.001);
        assertEquals(Math.sqrt(11 / 6), errors[1], 0.001);
    }

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[] {};
            EstimatedParameter[] parameters = new EstimatedParameter[] {};
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                problem.jacobian = new double[0][];
                problem.residuals = new double[0];
                this.measurements = problem.measurements;
                this.parameters = problem.parameters;
            }
        };
        estimator.initializeEstimate(problem);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertArrayEquals(problem.measurements, estimator.measurements);
        assertArrayEquals(problem.parameters, estimator.parameters);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeNumberOfMeasurements() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[] {};
            EstimatedParameter[] parameters = new EstimatedParameter[] {
                new EstimatedParameter() {
                    String name = "p1";
                    double initialValue = 0;
                    double initialVariance = 1;
                }
            };

            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        };
        AbstractEstimator estimator = new AbstractEstimator();
        estimator.estimate(problem);
    }
}