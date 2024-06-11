package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.estimation.WeightedMeasurement;
import org.apache.commons.math3.estimation.EstimatedParameter;
import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.exception.EstimationException;

/**
 * Test class for AbstractEstimator.
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)}.
     */
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test method for {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                costEvaluations++;
            }
        };
        estimator.setMaxCostEval(10);
        estimator.estimate(createTestProblem());
        assertEquals(1, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;
            @Override
            protected void updateJacobian() {
                jacobianEvaluations++;
            }
        };
        estimator.estimate(createTestProblem());
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#updateJacobian()}.
     */
    @Test(expected = EstimationException.class)
    public void testUpdateJacobianWithTooManyCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                costEvaluations++;
            }
            @Override
            protected void updateJacobian() {
                if (++costEvaluations > 5) {
                    throw new EstimationException("maximal number of evaluations exceeded (5)");
                }
                // not implemented
            }
        };
        estimator.setMaxCostEval(5);
        estimator.estimate(createTestProblem());
    }

    /**
     * Test method for {@link AbstractEstimator#updateResidualsAndCost()}.
     */
    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithTooManyCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                costEvaluations++;
                updateResidualsAndCost();
            }
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                if (++costEvaluations > 5) {
                    throw new EstimationException("maximal number of evaluations exceeded (5)");
                }
                // not implemented
            }
        };
        estimator.setMaxCostEval(5);
        estimator.estimate(createTestProblem());
    }

    /**
     * Test method for {@link AbstractEstimator#getRMS(EstimationProblem)}.
     */
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }
        };
        EstimationProblem problem = createTestProblem();
        double rms = estimator.getRMS(problem);
        assertEquals(0.0, rms, 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#getChiSquare(EstimationProblem)}.
     */
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }
        };
        EstimationProblem problem = createTestProblem();
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(0.0, chiSquare, 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     */
    @Test(expected = EstimationException.class)
    public void testGetCovariancesWithSingularProblem() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // empty implementation
            }

            @Override
            protected void updateJacobian() {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] {0.0, 0.0});
            }

            @Override
            public RealMatrix getMeasurementsMatrix() {
                return new Array2DRowRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
            }

            @Override
            public RealVector getResiduals() {
                return new ArrayRealVector(new double[] {0.0, 0.0});
            }

            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public int getNumberOfMeasurements() {
                return 2;
            }
        };
        try {
            estimator.estimate(problem);
            estimator.getCovariances(problem);
            fail("Expected EstimationException");
        } catch (EstimationException e) {
            // expected
        }
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }
        };
        EstimationProblem problem = createTestProblem();
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2, errors.length);
        for (int i = 0; i < errors.length; i++) {
            assertEquals(0.0, errors[i], 1e-10);
        }
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     */
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                fail("Unexpected call to initializeEstimate");
            }
        };
        EstimationProblem problem = createTestProblem();
        estimator.estimate(problem);
        assertFalse("Unexpected call to initializeEstimate", estimator.initializeEstimateCalled);
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateWithSingularProblem() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not called
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // empty implementation
            }

            @Override
            protected void updateJacobian() {
                // empty implementation
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] {0.0, 0.0});
            }

            @Override
            public RealMatrix getMeasurementsMatrix() {
                return new Array2DRowRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
            }

            @Override
            public RealVector getResiduals() {
                return new ArrayRealVector(new double[] {0.0, 0.0});
            }

            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public int getNumberOfMeasurements() {
                return 2;
            }
        };
        try {
            estimator.estimate(problem);
            fail("Expected EstimationException");
        } catch (EstimationException e) {
            // expected
        }
    }

    /**
     * Returns a test estimation problem.
     *
     * @return a test estimation problem
     */
    private EstimationProblem createTestProblem() {
        RealVector parameters = new ArrayRealVector(new double[] {1.0, 2.0});
        RealMatrix measurementsMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
        RealVector residuals = new ArrayRealVector(new double[] {1.0, 1.0});
        return new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return parameters;
            }

            @Override
            public RealMatrix getMeasurementsMatrix() {
                return measurementsMatrix;
            }

            @Override
            public RealVector getResiduals() {
                return residuals;
            }

            @Override
            public int getNumberOfParameters() {
                return parameters.getDimension();
            }

            @Override
            public int getNumberOfMeasurements() {
                return residuals.getDimension();
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[2];
                for (int i = 0; i < measurements.length; i++) {
                    measurements[i] = new WeightedMeasurement() {
                        @Override
                        public double getWeight() {
                            return 1.0;
                        }

                        @Override
                        public double getResidual() {
                            return residuals.getEntry(i);
                        }

                        @Override
                        public double getPartial(double parameter) {
                            return measurementsMatrix.getEntry(i, 1) * parameter;
                        }
                    };
                    measurements[i].setResidual(1.0);
                }
                return measurements;
            }
        };
    }

}