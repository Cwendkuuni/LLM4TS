package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorFactory;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class AbstractEstimatorTest {

    private static final double DELTA = 1e-10;

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new MyEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new MyEstimator();
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new MyEstimator();
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new MyEstimator();
        estimator.setMaxCostEval(1);
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 2.0, 3.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        estimator.updateJacobian();
        double[] jacobian = estimator.jacobian;
        assertEquals(1, estimator.jacobianEvaluations);
        assertArrayEquals(new double[]{-3.0}, jacobian, DELTA);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        estimator.setMaxCostEval(1);
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 2.0, 3.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        estimator.updateResidualsAndCost();
        double cost = estimator.cost;
        assertEquals(3.0, cost, DELTA);
        double[] residuals = estimator.residuals;
        assertEquals(-1.0, residuals[0], DELTA);
    }

    @Test
    public void testGetRMS() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        measurements[1] = new WeightedMeasurement(2.0, 5.0, 25.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        double rms = estimator.getRMS(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        assertEquals(5.477225575051661, rms, DELTA);
    }

    @Test
    public void testGetChiSquare() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        measurements[1] = new WeightedMeasurement(2.0, 5.0, 25.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        double chiSquare = estimator.getChiSquare(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        assertEquals(55.0, chiSquare, DELTA);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        measurements[1] = new WeightedMeasurement(2.0, 5.0, 25.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[]{new EstimatedParameter(1), new EstimatedParameter(2)};
            }
        });
        estimator.updateJacobian();
        double[][] covariances = estimator.getCovariances(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[]{new EstimatedParameter(1), new EstimatedParameter(2)};
            }
        });
        RealMatrix covarianceMatrix = new RealMatrixImpl(covariances);
        RealMatrix expectedCovarianceMatrix = new RealMatrixImpl(new double[][]{
                {2.5, -0.5},
                {-0.5, 2.5}
        });
        assertEquals(expectedCovarianceMatrix.getRowDimension(), covarianceMatrix.getRowDimension());
        assertEquals(expectedCovarianceMatrix.getColumnDimension(), covarianceMatrix.getColumnDimension());
        assertEquals(expectedCovarianceMatrix, covarianceMatrix);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        measurements[1] = new WeightedMeasurement(2.0, 5.0, 25.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[]{new EstimatedParameter(1), new EstimatedParameter(2)};
            }
        });
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[]{new EstimatedParameter(1), new EstimatedParameter(2)};
            }
        });
        assertEquals(2, errors.length);
        assertEquals(5.477225575051661, errors[0], DELTA);
        assertEquals(5.477225575051661, errors[1], DELTA);
    }

    @Test
    public void testInitializeEstimateNullProblem() {
        AbstractEstimator estimator = new MyEstimator();
        assertThrows(NullPointerException.class, () -> estimator.initializeEstimate(null));
    }

    @Test
    public void testEstimateNullProblem() {
        AbstractEstimator estimator = new MyEstimator();
        assertThrows(NullPointerException.class, () -> estimator.estimate(null));
    }

    @Test
    public void testGetCovariancesSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        assertThrows(EstimationException.class, () -> estimator.getCovariances(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        }));
    }

    @Test
    public void testGuessParametersErrorsSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        assertThrows(EstimationException.class, () -> estimator.guessParametersErrors(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        }));
    }

    @Test
    public void testEstimateNoSolution() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        measurements[1] = new WeightedMeasurement(2.0, 5.0, 10.0);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        });
        assertFalse(estimator.estimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }
        }));
        assertEquals(Double.POSITIVE_INFINITY, estimator.getParameters()[0].getValue(), DELTA);
    }

    @Test
    public void testEstimateMultipleSolutions() throws EstimationException {
        AbstractEstimator estimator = new MyEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement(1.0, 4.0, 9.0);
        measurements[1] = new WeightedMeasurement(2.0, 5.0, 10.0);
        measurements[2] = new WeightedMeasurement(3.0, 6.0, 11.0);
        EstimatedParameter[] parameters = new EstimatedParameter[]{new EstimatedParameter(1), new EstimatedParameter(2)};
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        });
        estimator.estimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        });
        RealVector solution1 = estimator.getParameters();
        RealVector solution2 = new SimpleRegression().estimateRegressionParameters(measurements).getEstimate();
        assertEquals(solution2.getDimension(), solution1.getDimension());
        assertArrayEquals(solution2.toArray(), solution1.toArray(), DELTA);
    }

    private static class MyEstimator extends AbstractEstimator {
        // Empty
    }

}