import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.estimation.EstimatedParameter;

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
        int maxCostEval = estimator.getMaxCostEval();
        assertEquals(10, maxCostEval);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.updateResidualsAndCost();
        int costEvaluations = estimator.getCostEvaluations();
        assertEquals(1, costEvaluations);
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.updateJacobian();
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        assertEquals(1, jacobianEvaluations);
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(getProblem());
        estimator.updateJacobian();
        double[] expectedJacobian = new double[] {-1.4142135625, -1.4142135625, 1.4142135625, 1.4142135625};
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(getProblem());
        estimator.updateResidualsAndCost();
        double expectedCost = 1.414213731;
        assertEquals(expectedCost, estimator.cost, 0.0);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = getProblem();
        double rms = estimator.getRMS(problem);
        double expectedRms = 1.414213731;
        assertEquals(expectedRms, rms, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = getProblem();
        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = 2.828427125;
        assertEquals(expectedChiSquare, chiSquare, 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = getProblem();
        double[][] covariances = estimator.getCovariances(problem);
        double[][] expectedCovariances = new double[][] {{0.707106781, 0.707106781}, {0.707106781, 0.707106781}};
        assertArrayEquals(expectedCovariances, covariances, 0.0);
    }

    @Test (expected = EstimationException.class)
    public void testGuessParametersErrorsWithSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = getProblemSingular();
        estimator.guessParametersErrors(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = getProblem();
        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[] {0.707106781, 0.707106781};
        assertArrayEquals(expectedErrors, errors, 0.0);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = getProblem();
        estimator.initializeEstimate(problem);
        WeightedMeasurement[] measurements = estimator.measurements;
        EstimatedParameter[] parameters = estimator.parameters;
        double[] jacobian = estimator.jacobian;
        double[] residuals = estimator.residuals;
        int rows = estimator.rows;
        int cols = estimator.cols;
        int costEvaluations = estimator.costEvaluations;
        int jacobianEvaluations = estimator.jacobianEvaluations;
        assertEquals(problem.getMeasurements(), measurements);
        assertArrayEquals(problem.getAllParameters(), parameters);
        assertEquals(new double[rows * cols], jacobian);
        assertEquals(new double[rows], residuals);
        assertEquals(1, rows);
        assertEquals(2, cols);
        assertEquals(0, costEvaluations);
        assertEquals(0, jacobianEvaluations);
    }

    @Test (expected = EstimationException.class)
    public void testEstimateWithMaxCostEvalReached() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateResidualsAndCost();
                estimator.updateJacobian();
            }
        };
        estimator.setMaxCostEval(1);
        EstimationProblem problem = getProblem();
        estimator.estimate(problem);
    }

    private EstimationProblem getProblem() {
        double[][] x = new double[][] {{0, 1}, {1, 0}};
        double[] b = new double[] {1, 1};
        double[] weights = new double[] {1, 1};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealMatrix getA() {
                return new RealMatrixImpl(x);
            }

            @Override
            public RealVector getB() {
                return new Array2DRowRealMatrix(b).getColumnVector(0);
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[2];
                for (int i = 0; i < measurements.length; ++i) {
                    measurements[i] = new WeightedMeasurement() {
                        @Override
                        public double[] getX() {
                            return new ArrayRealVector(new double[] {i});
                        }

                        @Override
                        public double getWeight() {
                            return weights[i];
                        }

                        @Override
                        public double getResidual() {
                            return b[i] - dotProduct(getX(), getA().operate(getX()).getColumnVector(0));
                        }
                    };
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {new EstimatedParameter() {
                    @Override
                    public double getName() {
                        return "p1";
                    }

                    @Override
                    public double getValue() {
                        return 0;
                    }

                    @Override
                    public double getError() {
                        return 0;
                    }
                }, new EstimatedParameter() {
                    @Override
                    public double getName() {
                        return "p2";
                    }

                    @Override
                    public double getValue() {
                        return 1;
                    }

                    @Override
                    public double getError() {
                        return 0;
                    }
                }};
            }
        };
        return problem;
    }

    private EstimationProblem getProblemSingular() {
        double[][] x = new double[][] {{1, 0}, {0, 0}};
        double[] b = new double[] {1, 1};
        double[] weights = new double[] {1, 1};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealMatrix getA() {
                return new RealMatrixImpl(x);
            }

            @Override
            public RealVector getB() {
                return new Array2DRowRealMatrix(b).getColumnVector(0);
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[2];
                for (int i = 0; i < measurements.length; ++i) {
                    measurements[i] = new WeightedMeasurement() {
                        @Override
                        public double[] getX() {
                            return new ArrayRealVector(new double[] {i});
                        }

                        @Override
                        public double getWeight() {
                            return weights[i];
                        }

                        @Override
                        public double getResidual() {
                            return b[i] - dotProduct(getX(), getA().operate(getX()).getColumnVector(0));
                        }
                    };
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {new EstimatedParameter() {
                    @Override
                    public double getName() {
                        return "p1";
                    }

                    @Override
                    public double getValue() {
                        return 0;
                    }

                    @Override
                    public double getError() {
                        return 0;
                    }
                }, new EstimatedParameter() {
                    @Override
                    public double getName() {
                        return "p2";
                    }

                    @Override
                    public double getValue() {
                        return 0;
                    }

                    @Override
                    public double getError() {
                        return 0;
                    }
                }};
            }
        };
        EigenDecomposition eigenDecomposition = new EigenDecomposition(getA().transpose().operate(getA()));
        if (eigenDecomposition.getSingularValues()[1] <= 0) {
            throw new SingularValueDecompositionException("Singular matrix", eigenDecomposition.getSingularValues());
        }
        return problem;
    }

    private double dotProduct(RealVector a, RealVector b) {
        return a.dotProduct(b);
    }
}