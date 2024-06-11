package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
            }
        };
        estimator.initializeEstimate(problem);
        assertEquals(estimator.getRows(), problem.getMeasurements().length);
        assertEquals(estimator.getCols(), problem.getAllParameters().length);
        assertNull(estimator.getJacobian());
        assertNull(estimator.getResiduals());
        assertEquals(estimator.getCost(), Double.POSITIVE_INFINITY);
    }

    @Test
    public void testUpdateJacobianAndResiduals() {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                estimator.updateJacobian();
                estimator.updateResidualsAndCost();
            }
        };
        estimator.initializeEstimate(problem);
        double[][] expectedJacobian = new double[][]{
                {-1.0 / Math.sqrt(1.0), 1.0 / Math.sqrt(1.0)},
                {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                {-1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)}
        };
        double[] expectedResiduals = new double[]{-0.5, 0.5, 1.5};
        estimator.estimate(problem);
        assertArrayEquals(expectedJacobian, estimator.getJacobian());
        assertArrayEquals(expectedResiduals, estimator.getResiduals());
        assertEquals(estimator.getCost(), 2.3333333333333335);
    }

    @Test
    public void testRMSAndChiSquare() {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem estimationProblem) {
                return 1.0;
            }

            @Override
            public double getChiSquare(EstimationProblem estimationProblem) {
                return 2.0;
            }
        };
        estimator.initializeEstimate(problem);
        assertEquals(estimator.getRMS(problem), Math.sqrt(2.0 / 3.0));
        assertEquals(estimator.getChiSquare(problem), 2.0);
    }

    @Test
    public void testCovariances() {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem estimationProblem)
                    throws EstimationException {
                double[][] covariances = new double[][]{
                        {1.0, 0.5},
                        {0.5, 1.0}
                };
                return covariances;
            }
        };
        estimator.initializeEstimate(problem);
        RealMatrix expectedCovariances = new RealMatrixImpl(new double[][]{
                {1.0, 0.5},
                {0.5, 1.0}
        });
        double[][] actualCovariances = estimator.getCovariances(problem).getData();
        assertEquals(expectedCovariances.getRowDimension(), actualCovariances.length);
        assertEquals(expectedCovariances.getColumnDimension(), actualCovariances[0].length);
        assertArrayEquals(expectedCovariances.getData(), actualCovariances);
    }

    @Test
    public void testGuessParametersErrors() {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem estimationProblem)
                    throws EstimationException {
                return new double[]{1.0, 1.0};
            }
        };
        estimator.initializeEstimate(problem);
        double[] expectedErrors = new double[]{Math.sqrt(2.0 / 3.0), Math.sqrt(2.0 / 3.0)};
        double[] actualErrors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, actualErrors);
    }

    @Test
    public void testMaxCostEvalException() {
        EstimationProblem problem = createEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void setMaxCostEval(int maxCostEval) {
                this.maxCostEval = maxCostEval;
            }

            @Override
            public int getCostEvaluations() {
                return costEvaluations;
            }

            @Override
            public void estimate(EstimationProblem estimationProblem)
                    throws EstimationException {
                if (costEvaluations > maxCostEval) {
                    throw new EstimationException("maximal number of evaluations exceeded");
                }
                costEvaluations++;
            }
        };
        estimator.initializeEstimate(problem);
        estimator.setMaxCostEval(1);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createEstimationProblem() {
        double[][] measurementsData = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {0.5, 1.5, 2.5}};
        RealVector measurementsVector = new ArrayRealVector(new double[]{1.0, 1.5, 0.5}, false);
        double[] parameters = new double[]{0.5, 1.0};
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(measurementsVector.getVector(), i);
        }
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("param1", parameters[0]),
                        new EstimatedParameter("param2", parameters[1])};
            }
        };
        return problem;
    }
}