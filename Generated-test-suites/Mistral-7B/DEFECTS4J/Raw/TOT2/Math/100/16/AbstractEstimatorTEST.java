package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.VectorUtils;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)} and {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(0, estimator.getCostEvaluations());
        estimator.estimate(createProblem(1.0, 1.0));
        assertEquals(1, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)} and {@link AbstractEstimator#getCostEvaluations()} when the maximum number of cost evaluations is exceeded.
     */
    @Test
    public void testExceedMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.setMaxCostEval(1);
        try {
            estimator.estimate(createProblem(1.0, 2.0));
            fail();
        } catch (EstimationException ex) {
            assertEquals("maximal number of evaluations exceeded (1)", ex.getMessage());
        }
        assertEquals(0, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateJacobian();
            }
        };
        estimator.estimate(createProblem(1.0, 1.0));
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#updateJacobian()} and {@link AbstractEstimator#getJacobian()}.
     */
    @Test
    public void testUpdateJacobianAndGetJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateJacobian();
            }
        };
        estimator.estimate(createProblem(1.0, 1.0));
        RealMatrix expectedJacobian = new SingularValueDecomposition(new RealMatrix(new double[][]{
                {-1.0, -1.0},
                {-1.0, 1.0}
        }))
                .getS().transpose();
        RealMatrix actualJacobian = new RealMatrixImpl(estimator.jacobian);
        assertEquals(expectedJacobian.getRowDimension(), actualJacobian.getRowDimension());
        assertEquals(expectedJacobian.getColumnDimension(), actualJacobian.getColumnDimension());
        assertEquals(expectedJacobian, actualJacobian);
    }

    /**
     * Test method for {@link AbstractEstimator#updateResidualsAndCost()} and {@link AbstractEstimator#getCost()}.
     */
    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateResidualsAndCost();
            }
        };
        estimator.estimate(createProblem(1.0, 1.0));
        double expectedCost = Math.sqrt(0.5);
        double actualCost = estimator.getCost();
        assertEquals(expectedCost, actualCost, 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#getRMS(EstimationProblem)} and {@link AbstractEstimator#getChiSquare(EstimationProblem)}.
     */
    @Test
    public void testGetRMSAndChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateJacobian();
                estimator.updateResidualsAndCost();
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return VectorUtils.createRealVector(new double[]{1.0});
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(0.0, 1.0, 1.0),
                        new WeightedMeasurement(1.0, 1.0, 1.0)
                };
            }
        };
        estimator.estimate(problem);
        double expectedRMS = Math.sqrt(0.5);
        double actualRMS = estimator.getRMS(problem);
        assertEquals(expectedRMS, actualRMS, 1e-10);
        double expectedChiSquare = 1.0;
        double actualChiSquare = estimator.getChiSquare(problem);
        assertEquals(expectedChiSquare, actualChiSquare, 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)} and {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGetCovariancesAndGuessParametersErrors() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateJacobian();
                estimator.updateResidualsAndCost();
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return VectorUtils.createRealVector(new double[]{1.0, 2.0});
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(0.0, 1.0, 1.0),
                        new WeightedMeasurement(1.0, 1.0, 1.0),
                        new WeightedMeasurement(2.0, 1.0, 1.0),
                        new WeightedMeasurement(3.0, 1.0, 1.0)
                };
            }
        };
        estimator.estimate(problem);
        double[] expectedCovariances = {0.5, 0.5};
        double[] actualCovariances = estimator.getCovariances(problem)[0];
        assertArrayEquals(expectedCovariances, actualCovariances, 1e-10);
        double[] expectedErrors = {Math.sqrt(0.5), Math.sqrt(0.5)};
        double[] actualErrors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, actualErrors, 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     */
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateJacobian();
                estimator.updateResidualsAndCost();
            }
        };
        estimator.initializeEstimate(createProblem(1.0, 1.0));
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertEquals(1.0, estimator.getJacobian()[0], 1e-10);
        assertEquals(1.0, estimator.getJacobian()[1], 1e-10);
        assertEquals(1.0, estimator.getResiduals()[0], 1e-10);
        assertEquals(1.0, estimator.getResiduals()[1], 1e-10);
        assertEquals(Math.sqrt(0.5), estimator.getCost(), 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)} when the number of degrees of freedom is not positive.
     */
    @Test
    public void testNegativeDegreesOfFreedom() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateJacobian();
                estimator.updateResidualsAndCost();
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return VectorUtils.createRealVector(new double[]{1.0, 1.0});
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(0.0, 1.0, 1.0),
                        new WeightedMeasurement(1.0, 1.0, 1.0)
                };
            }
        };
        try {
            estimator.estimate(problem);
            fail();
        } catch (EstimationException ex) {
            assertEquals("number of degrees of freedom ({0} measurements, {1} parameters) is not positive",
                         ex.getMessage(), new Object[]{new Integer(2), new Integer(2)});
        }
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)} when the problem is singular.
     */
    @Test
    public void testSingularProblem() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateJacobian();
                estimator.updateResidualsAndCost();
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return VectorUtils.createRealVector(new double[]{1.0, 1.0});
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(0.0, 0.0, 0.0),
                        new WeightedMeasurement(1.0, 0.0, 0.0)
                };
            }
        };
        try {
            estimator.estimate(problem);
            fail();
        } catch (EstimationException ex) {
            assertEquals("unable to compute covariances: singular problem", ex.getMessage());
        }
    }

    private EstimationProblem createProblem(double... values) {
        return new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return VectorUtils.createRealVector(values);
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(0.0, 1.0, 1.0),
                        new WeightedMeasurement(1.0, 1.0, 1.0)
                };
            }
        };
    }

}