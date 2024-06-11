package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.stat.statistics.ChiSquareDistribution;
import org.junit.Test;

public class AbstractEstimatorTest {

    private static final double EPSILON = 1e-10;
    private static final int NB_MEASUREMENTS = 5;
    private static final int NB_PARAMETERS = 3;
    private static final double[][] TEST_MATRIX = new double[][] {
            {1.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}, {2.0, 1.0, 1.0}, {2.0, 2.0, 2.0}
    };
    private static final double[] TEST_VECTOR = new double[] {3.0, 4.0, 5.0};
    private static final EstimationProblem TEST_PROBLEM = new EstimationProblem(NB_MEASUREMENTS, NB_PARAMETERS) {
        @Override
        public RealMatrix getMeasurements() {
            return new RealMatrixImpl(NB_MEASUREMENTS, NB_PARAMETERS, TEST_MATRIX);
        }

        @Override
        public RealVector getParameters() {
            return VectorUtils.createRealVector(TEST_VECTOR);
        }

        @Override
        public RealVector getUnboundParameters() {
            return VectorUtils.createRealVector(new double[] {0.0, 0.0, 0.0});
        }
    };

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
            private int counter;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                ++counter;
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            private int counter;

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                ++counter;
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            private double[] jacobianCopy;

            @Override
            protected void updateJacobian() {
                jacobianEvaluations++;
                Arrays.fill(jacobian, 0);
                for (int i = 0; i < rows; i++) {
                    WeightedMeasurement wm = measurements[i];
                    double factor = -Math.sqrt(wm.getWeight());
                    for (int j = 0; j < cols; ++j) {
                        jacobian[jacobianEvaluations * cols + j] = factor * wm.getPartial(parameters[j]);
                    }
                }
                jacobianCopy = Arrays.copyOfRange(jacobian, 0, cols * rows);
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        estimator.updateJacobian();
        estimator.updateJacobian();
        double[] expected = new double[] {-1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0};
        assertArrayEquals(expected, estimator.jacobianCopy);
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            private int counter;
            private double cost;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                ++counter;
                cost = 0;
                for (int i = 0, index = 0; i < rows; i++, index += cols) {
                    WeightedMeasurement wm = measurements[i];
                    double residual = wm.getResidual();
                    residuals[i] = Math.sqrt(wm.getWeight()) * residual;
                    cost += wm.getWeight() * residual * residual;
                }
                cost = Math.sqrt(cost);
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        double expectedCost = Math.sqrt(5.0 * 3.0 * Math.PI / 2.0);
        assertEquals(Math.sqrt(TEST_PROBLEM.getChiSquare()), expectedCost, EPSILON);
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return Math.sqrt(problem.getChiSquare() / problem.getMeasurements().length);
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        double expected = Math.sqrt(TEST_PROBLEM.getChiSquare() / TEST_PROBLEM.getMeasurements().length);
        assertEquals(expected, estimator.getRMS(TEST_PROBLEM), EPSILON);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                ChiSquareDistribution dist = new ChiSquareDistribution(TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length);
                return dist.inverseCumulativeProbability(1.0 - 0.01);
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        double expected = TEST_PROBLEM.getChiSquare() / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length);
        assertEquals(expected, estimator.getChiSquare(TEST_PROBLEM), EPSILON);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
                for (int i = 0; i < rows; ++i) {
                    WeightedMeasurement wm = measurements[i];
                    parameters[i] = new EstimatedParameter(wm.getParameterName(), wm.getInitialValue(), wm.getWeight());
                }
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                // do nothing
                return new double[0][];
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        double[][] expected = new double[][] {
                {1.0 / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length), 0.0, 0.0},
                {0.0, 1.0 / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length), 0.0},
                {0.0, 0.0, 1.0 / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length)}
        };
        double[][] actual = estimator.getCovariances(TEST_PROBLEM);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                double sqrtChiSquare = Math.sqrt(TEST_PROBLEM.getChiSquare() / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length));
                double[] parameters = problem.getAllParameters().toArray();
                RealMatrix invJTJ = new RealMatrixImpl(new double[][] {
                        {1.0 / (1.0 + 2.0 * sqrtChiSquare), -1.0 / (2.0 * sqrtChiSquare), 0.0},
                        {-1.0 / (2.0 * sqrtChiSquare), 1.0 / (1.0 + 2.0 * sqrtChiSquare), 0.0},
                        {0.0, 0.0, 1.0 / Math.sqrt(TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length)}
                });
                return invJTJ.operate(VectorUtils.createRealVector(TEST_VECTOR)).toArray();
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
        double[] expected = new double[] {Math.sqrt(3.0 / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length)),
                Math.sqrt(3.0 / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length)),
                Math.sqrt(3.0 / (TEST_PROBLEM.getMeasurements().length - TEST_PROBLEM.getAllParameters().length))};
        assertArrayEquals(expected, estimator.guessParametersErrors(TEST_PROBLEM), EPSILON);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateThrowsException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("test exception");
            }
        };
        estimator.initializeEstimate(TEST_PROBLEM);
        estimator.estimate(TEST_PROBLEM);
    }

}