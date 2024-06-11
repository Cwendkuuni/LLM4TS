package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.descript.ArrayComponents;
import org.apache.commons.math3.stat.descript.DescriptiveStatistics;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.junit.Test;

import java.util.Arrays;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
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
                // no-op
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
                // no-op
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
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        EstimatedParameter[] parameters = new EstimatedParameter[3];
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
        estimator.updateJacobian();
        double[] expectedJacobian = new double[]{
                0.0, 1.0, 1.0,
                0.0, 1.0, 2.0,
                1.0, 1.0, 2.0
        };
        assertArrayEquals(expectedJacobian, estimator.jacobian);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        Arrays.stream(measurements).forEach(m -> m = new WeightedMeasurement(1.0, 2.0, 1.0));
        EstimatedParameter[] parameters = new EstimatedParameter[3];
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
        estimator.updateResidualsAndCost();
        double expectedCost = Math.sqrt(Arrays.stream(measurements)
                .mapToDouble(m -> m.getWeight() * m.getResidual() * m.getResidual())
                .sum());
        assertEquals(expectedCost, estimator.cost, 1e-10);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        Arrays.stream(measurements).forEach(m -> m = new WeightedMeasurement(1.0, 2.0, 1.0));
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
        double rms = estimator.getRMS(problem);
        double expectedRms = Math.sqrt(Arrays.stream(measurements)
                .mapToDouble(m -> m.getWeight() * m.getResidual() * m.getResidual())
                .sum() / measurements.length);
        assertEquals(expectedRms, rms, 1e-10);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        Arrays.stream(measurements).forEach(m -> m = new WeightedMeasurement(1.0, 2.0, 1.0));
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
        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = Arrays.stream(measurements)
                .mapToDouble(m -> m.getResidual() * m.getResidual() / m.getWeight())
                .sum();
        assertEquals(expectedChiSquare, chiSquare, 1e-10);
    }

    @Test
    public void testGetCovariances() throws EstimationException, NotPositiveDefiniteMatrixException, SingularMatrixException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        EstimatedParameter[] parameters = new EstimatedParameter[3];
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        });
        estimator.updateJacobian();
        RealMatrix covariances = estimator.getCovariances(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        });
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{
                {1.0 / 2.0, 0.25, 0.25},
                {0.25, 1.0 / 2.0, 0.25},
                {0.25, 0.25, 1.0 / 2.0}
        });
        RealMatrix expectedInvCovariances = new Array2DRowRealMatrix(new double[][]{
                {2.0, -1.0, -1.0},
                {-1.0, 2.0, -1.0},
                {-1.0, -1.0, 2.0}
        });
        RealMatrix invCovariances = expectedInvCovariances.inverse();
        RealMatrix jTj = new Array2DRowRealMatrix(estimator.jacobian).transpose().multiply(new Array2DRowRealMatrix(estimator.jacobian));
        RealMatrix covariancesResult = covariances.multiply(invCovariances);
        RealMatrix jTjExpected = new Array2DRowRealMatrix(expectedJacobian).transpose().multiply(new Array2DRowRealMatrix(expectedJacobian));
        RealMatrix covariancesExpected = jTjExpected.inverse();
        assertTrue(covariancesResult.equals(covariancesExpected, 1e-10));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        EstimatedParameter[] parameters = new EstimatedParameter[3];
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        });
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        double[] errors = estimator.guessParametersErrors(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        });
        double[] expectedErrors = new double[]{1.0, 1.0, 1.0};
        assertArrayEquals(expectedErrors, errors, 1e-10);
    }
}