package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
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
                // do nothing
            }
        };
        estimator.setMaxCostEval(10);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }

            @Override
            protected void updateJacobian() {
                // do nothing
            }
        };
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        estimator.setMaxCostEval(1);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {1, 2});
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return new Array2DRowRealMatrix(new double[][] {
                        {1, 1},
                        {2, 2},
                        {3, 3}
                });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(Math.sqrt(3), estimator.cost, 1e-6);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {1, 2});
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return new Array2DRowRealMatrix(new double[][] {
                        {1, 1},
                        {2, 2},
                        {3, 3}
                });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(3), rms, 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {1, 2});
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return new Array2DRowRealMatrix(new double[][] {
                        {1, 1},
                        {2, 2},
                        {3, 3}
                });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(3, chiSquare, 1e-6);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {1, 2});
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return new Array2DRowRealMatrix(new double[][] {
                        {1, 1},
                        {2, 2},
                        {3, 3}
                });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(new double[][] {
                {1, 0.5},
                {0.5, 1}
        });
        assertEquals(expectedCovarianceMatrix, covarianceMatrix);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {1, 2});
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return new Array2DRowRealMatrix(new double[][] {
                        {1, 1},
                        {2, 2},
                        {3, 3}
                });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2, errors.length);
        double expectedError = Math.sqrt(3) / Math.sqrt(3. - 2.);
        for (int i = 0; i < errors.length; ++i) {
            assertEquals(expectedError, errors[i], 1e-6);
        }
    }

    @Test(expected = EstimationException.class)
    public void testEstimateThrowsException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("test exception");
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {1, 2});
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return new Array2DRowRealMatrix(new double[][] {
                        {1, 1},
                        {2, 2},
                        {3, 3}
                });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }
        };
        estimator.estimate(problem);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] {1, 2});
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return new Array2DRowRealMatrix(new double[][] {
                        {1, 1},
                        {2, 2},
                        {3, 3}
                });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }
        };
        estimator.initializeEstimate(problem);
        assertEquals(3, estimator.rows);
        assertEquals(2, estimator.cols);
        assertEquals(3, estimator.measurements.length);
        assertEquals(2, estimator.parameters.length);
        assertArrayEquals(new double[] {1, 1}, estimator.measurements[0].getData(), 1e-6);
        assertArrayEquals(new double[] {1, 2}, estimator.measurements[1].getData(), 1e-6);
        assertArrayEquals(new double[] {1, 3}, estimator.measurements[2].getData(), 1e-6);
        assertArrayEquals(new double[] {1, 2}, estimator.parameters[0].getData(), 1e-6);
        assertArrayEquals(new double[] {0, 2}, estimator.parameters[1].getData(), 1e-6);
    }
}