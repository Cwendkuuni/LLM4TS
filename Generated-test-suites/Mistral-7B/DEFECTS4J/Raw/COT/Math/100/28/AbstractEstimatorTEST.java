import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals("Incorrect max cost evaluations", 10, maxCostEval);
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
        assertEquals("Incorrect number of cost evaluations", 1, costEvaluations);
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }
        };

        estimator.updateJacobian();
        estimator.updateJacobian();
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        assertEquals("Incorrect number of jacobian evaluations", 2, jacobianEvaluations);
    }

    @Test(expected = EstimationException.class)
    public void testEstimationExceptionWhenMaxCostEvalExceeded() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public void setMaxCostEval(int maxCostEval) {
                this.maxCostEval = maxCostEval;
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                if (++costEvaluations > maxCostEval) {
                    throw new EstimationException("maximal number of evaluations exceeded ({0})",
                            new Object[] { new Integer(maxCostEval) });
                }
                // implementation of the method
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double getRMS(EstimationProblem problem) {
                return 0;
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 0, 1) };
            }
        };

        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals("Incorrect RMS value", 0, rms, 1e-9);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0;
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 0, 1),
                        new WeightedMeasurement(1, 4, 1),
                        new WeightedMeasurement(1, 9, 1) };
            }
        };

        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals("Incorrect chi-square value", 14, chiSquare, 1e-9);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                return new double[0][];
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 0, 1),
                        new WeightedMeasurement(1, 4, 1),
                        new WeightedMeasurement(1, 9, 1) };
            }
        };

        estimator.initializeEstimate(problem);
        double[][] covariances = estimator.getCovariances(problem);

        int rows = problem.getMeasurements().length;
        int cols = problem.getAllParameters().length;
        double[][] expectedCovariances = new double[cols][cols];
        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (i == j) {
                    expectedCovariances[i][j] = 1.0 / (rows - 1);
                } else {
                    expectedCovariances[i][j] = 0.0;
                }
            }
        }

        Array2DRowRealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedCovariances);
        RealMatrixImpl covarianceMatrix = new RealMatrixImpl(covariances);
        assertEquals("Incorrect covariances matrix", expectedMatrix.transpose().multiply(expectedMatrix),
                covarianceMatrix.multiply(covarianceMatrix));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[0];
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 0, 1),
                        new WeightedMeasurement(1, 4, 1),
                        new WeightedMeasurement(1, 9, 1) };
            }
        };

        estimator.initializeEstimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[] { 1, 1 };
        assertArrayEquals("Incorrect guess for parameters errors", expectedErrors, errors);
    }
}