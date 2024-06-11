package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtils;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no implementation here
            }
        };
        problem = new MockEstimationProblem(2, 2);
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCostEvalNegative() {
        estimator.setMaxCostEval(-1);
    }

    @Test
    public void testGetCostEvaluations() {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                // no implementation here
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                // no implementation here
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                // no implementation here
            }

            @Override
            protected void estimate(EstimationProblem problem) throws EstimationException {
                // no implementation here
            }
        };

        double[][] measurements = problem.getMeasurements();
        RealMatrix jacobian = new Array2DRowRealMatrix(2, 2);
        for (int row = 0; row < measurements.length; row++) {
            double[] wm = measurements[row];
            for (int col = 0; col < 2; col++) {
                jacobian.setEntry(row, col, -Math.sqrt(wm[1]) * wm[col + 2]);
            }
        }

        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        assertArrayEquals(jacobian.getData(), estimator.jacobian, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                // no implementation here
            }

            @Override
            public void setMaxCostEval(int maxCostEval) {
                this.maxCostEval = maxCostEval;
            }

            @Override
            public int getMaxCostEval() {
                return maxCostEval;
            }
        };

        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        assertThrows(EstimationException.class, () -> estimator.updateResidualsAndCost());
    }

    @Test
    public void testGetRMS() {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                // no implementation here
            }
        };

        double[][] measurements = problem.getMeasurements();
        double[] expectations = new double[]{0.1, 0.2};
        double[] residuals = new double[]{0.05, 0.15};
        double[] weights = new double[]{1.0, 1.0};

        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new double[]{weights[i], expectations[i], residuals[i]};
        }

        estimator.initializeEstimate(new EstimationProblem(measurements));
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        double rms = estimator.getRMS(new EstimationProblem(measurements));
        assertEquals(0.1061, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                // no implementation here
            }
        };

        double[][] measurements = problem.getMeasurements();
        double[] expectations = new double[]{0.1, 0.2};
        double[] residuals = new double[]{0.05, 0.15};
        double[] weights = new double[]{1.0, 1.0};

        for (int i = 0; i < measurements.length; i++) {
            measurements[i][2] = residuals[i];
        }

        estimator.initializeEstimate(new EstimationProblem(measurements));
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        double chiSquare = estimator.getChiSquare(new EstimationProblem(measurements));
        assertEquals(0.41, chiSquare, 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                // no implementation here
            }
        };

        double[][] measurements = problem.getMeasurements();
        double[][] expectations = new double[][]{
                {1.0, 1.0},
                {2.0, 2.0}
        };
        double[][] jacobian = new double[][]{
                {-1.0, -2.0},
                {-2.0, -3.0}
        };

        estimator.initializeEstimate(new EstimationProblem(measurements, expectations));
        estimator.updateJacobian();

        double[][] covariances = estimator.getCovariances(new EstimationProblem(measurements, expectations));
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(2, 2);
        expectedCovariances.setEntry(0, 0, 1.5);
        expectedCovariances.setEntry(1, 1, 3.0);
        expectedCovariances.setEntry(0, 1, -1.5);
        expectedCovariances.setEntry(1, 0, -1.5);

        assertArrayEquals(expectedCovariances.getData(), covariances, 0.0);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                // no implementation here
            }

            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                double[] errors = super.guessParametersErrors(problem);
                double[] parameters = problem.getAllParameters();
                for (int i = 0; i < errors.length; i++) {
                    assertEquals(Math.sqrt(1.0 / (measurements.length - parameters.length)) * Math.sqrt(0.41), errors[i], 0.01);
                }
                return errors;
            }
        };

        double[][] measurements = problem.getMeasurements();
        double[] errors = estimator.guessParametersErrors(new EstimationProblem(measurements));

        assertEquals(2.0, errors[0], 0.01);
        assertEquals(2.8, errors[1], 0.01);
    }

}

class MockEstimationProblem implements EstimationProblem {

    private final int rows;
    private final int cols;

    public MockEstimationProblem(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public WeightedMeasurement[] getMeasurements() {
        double[][] measurements = new double[rows][3];
        for (int i = 0; i < rows; i++) {
            measurements[i] = new double[]{1.0, i, Math.sqrt(i + 1.0)};
        }
        return VectorUtils.toObject(measurements);
    }

    @Override
    public RealVector getAllParameters() {
        return VectorUtils.createDenseVector(new double[]{0.0, 1.0});
    }

}