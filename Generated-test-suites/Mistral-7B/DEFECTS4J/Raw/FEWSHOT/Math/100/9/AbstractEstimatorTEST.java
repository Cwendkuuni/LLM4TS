package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class AbstractEstimatorTest {

    private final static int ROWS = 3;
    private final static int COLS = 2;
    private final static double EPSILON = 1e-12;

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(5);
        assertEquals(5, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.costEvaluations = 10;
        assertEquals(10, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.jacobianEvaluations = 15;
        assertEquals(15, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            double[] jacobian = new double[10];
            int rows = 3, cols = 2;

            @Override
            protected void updateJacobian() {
                Arrays.fill(jacobian, 0);
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        jacobian[i * cols + j] = 1;
                    }
                }
            }

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                jacobianEvaluations++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        estimator.jacobianEvaluations = 0;
        estimator.updateJacobian();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.getJacobianEvaluations());

        double[] expectedJacobian = new double[10];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                expectedJacobian[i * 2 + j] = 1;
            }
        }
        assertArrayEquals(expectedJacobian, estimator.jacobian);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            double cost = 0;
            double[] residuals = new double[3];

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                costEvaluations++;
                cost = 2;
                for (int i = 0; i < 3; i++) {
                    residuals[i] = 1;
                }
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
            }
        };
        estimator.estimate(createDummyProblem());
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(Math.sqrt(2), estimator.cost, EPSILON);
        assertArrayEquals(new double[]{1, 1, 1}, estimator.residuals);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 1;
            }
        };
        EstimationProblem problem = createDummyProblem();
        assertEquals(1, estimator.getRMS(problem), EPSILON);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 1;
            }
        };
        EstimationProblem problem = createDummyProblem();
        assertEquals(1, estimator.getChiSquare(problem), EPSILON);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                return new double[][]{{1, 0}, {0, 1}};
            }
        };
        EstimationProblem problem = createDummyProblem();
        double[][] expectedCovariances = new double[][]{{1, 0}, {0, 1}};
        RealMatrix covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovarianceMatrix = new RealMatrixImpl(expectedCovariances);
        RealMatrix actualCovarianceMatrix = new RealMatrixImpl(covariances.getData());
        assertEquals(expectedCovarianceMatrix, actualCovarianceMatrix);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                return new double[]{1, 1};
            }
        };
        EstimationProblem problem = createDummyProblem();
        double[] expectedErrors = new double[]{1, 1};
        double[] actualErrors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, actualErrors);
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // empty implementation
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        EstimationProblem problem = createDummyProblem();
        estimator.initializeEstimate(problem);

        WeightedMeasurement[] measurements = problem.getMeasurements();
        EstimatedParameter[] parameters = problem.getAllParameters();
        assertEquals(3, measurements.length);
        assertEquals(2, parameters.length);

        double[] jacobian = new double[6];
        double[] residuals = new double[3];

        estimator.rows = 3;
        estimator.cols = 2;
        estimator.jacobian = jacobian;
        estimator.residuals = residuals;
    }

    @Test
    public void testEstimateShouldThrowExceptionIfMaxCostEvalExceeded()
            throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected int maxCostEval = 1;

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = createDummyProblem();
        estimator.estimate(problem);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createDummyProblem() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        EstimatedParameter[] parameters = new EstimatedParameter[2];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, new LinearConstraint(new LinearFunction(1, new LinearCoefficients(2))));
        }
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = new EstimatedParameter("p" + i, new LinearFunction(1, new LinearCoefficients(1)));
        }
        return new EstimationProblem(new LinearConstraint[]{}, new LinearFunction[]{}, measurements, parameters);
    }

}