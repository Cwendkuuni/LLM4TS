import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
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
            int counter = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                counter++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                for (int i = 0; i < 5; i++) {
                    updateResidualsAndCost();
                }
            }
        };
        assertEquals(0, estimator.getCostEvaluations());
        estimator.estimate(new DummyEstimationProblem(5));
        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                counter++;
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                for (int i = 0; i < 3; i++) {
                    updateJacobian();
                }
            }
        };
        assertEquals(0, estimator.getJacobianEvaluations());
        estimator.estimate(new DummyEstimationProblem(3));
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            double[] jacobian = new double[3 * 2];

            @Override
            protected void updateJacobian() {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 2; j++) {
                        jacobian[i * 2 + j] = i + j;
                    }
                }
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(3);
        estimator.estimate(problem);
        RealMatrixImpl expectedJacobian = new RealMatrixImpl(new Array2DRowRealMatrix(3, 2, new double[][]{{1, 1}, {2, 2}, {3, 3}}));
        RealMatrixImpl actualJacobian = new RealMatrixImpl(estimator.jacobian);
        assertEquals(expectedJacobian, actualJacobian);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 42.0;
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(1, new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        assertEquals(42.0, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 42.0;
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(1, new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        assertEquals(42.0, estimator.getChiSquare(problem), 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                double[][] expectedCovariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
                return expectedCovariances;
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(2, new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        double[][] actualCovariances = estimator.getCovariances(problem);
        assertArrayEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                double[] expectedErrors = new double[]{2.1, 1.5};
                return expectedErrors;
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(2, new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        double[] actualErrors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, actualErrors);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithInvalidProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(0, new double[]{1.0}, new double[]{1.0});
        estimator.estimate(problem);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                measurements = problem.getMeasurements();
                parameters = problem.getUnboundParameters();
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new DummyEstimationProblem(2, new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        estimator.initializeEstimate(problem);
        assertArrayEquals(problem.getMeasurements(), estimator.measurements);
        assertArrayEquals(problem.getUnboundParameters(), estimator.parameters);
    }

    private static class DummyEstimationProblem implements EstimationProblem {
        private final int rows;
        private final EstimatedParameter[] parameters;
        private final WeightedMeasurement[] measurements;

        public DummyEstimationProblem(int rows, EstimatedParameter[] parameters, WeightedMeasurement[] measurements) {
            this.rows = rows;
            this.parameters = parameters;
            this.measurements = measurements;
        }

        @Override
        public int getNumberOfMeasurements() {
            return rows;
        }

        @Override
        public EstimatedParameter[] getUnboundParameters() {
            return parameters;
        }

        @Override
        public WeightedMeasurement[] getMeasurements() {
            return measurements;
        }

        @Override
        public double[] getAllParameters() {
            double[] allParameters = new double[parameters.length];
            for (int i = 0; i < allParameters.length; i++) {
                allParameters[i] = parameters[i].getValue();
            }
            return allParameters;
        }
    }
}