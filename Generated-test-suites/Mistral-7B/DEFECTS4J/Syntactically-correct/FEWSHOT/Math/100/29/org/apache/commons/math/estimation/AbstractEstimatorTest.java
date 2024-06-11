package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateResidualsAndCost();
            }
        };
        assertEquals(0, estimator.getCostEvaluations());
        estimator.estimate(createTestProblem());
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateJacobian();
            }
        };
        assertEquals(0, estimator.getJacobianEvaluations());
        estimator.estimate(createTestProblem());
        estimator.estimate(createTestProblem());
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateJacobian();
                estimator.updateResidualsAndCost();
            }
        };
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(createTestProblem());
        estimator.estimate(createTestProblem());
        double[][] expectedJacobian = new double[][]{
                {-1.0, -1.0},
                {-1.0, 1.0},
                {1.0, -1.0},
                {1.0, 1.0}
        };
        assertArrayEquals(expectedJacobian, estimator.jacobian);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateResidualsAndCost();
            }
        };
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(createTestProblem());
        estimator.estimate(createTestProblem());
        double expectedCost = 4.814361141413655;
        assertEquals(expectedCost, estimator.cost, 1e-6);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateResidualsAndCost();
            }
        };
        EstimationProblem problem = createTestProblem();
        double expectedRms = 2.0;
        assertEquals(expectedRms, estimator.getRMS(problem), 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateResidualsAndCost();
            }
        };
        EstimationProblem problem = createTestProblem();
        double expectedChiSquare = 12.0;
        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 1e-6);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateJacobian();
                estimator.estimate(problem);
            }
        };
        EstimationProblem problem = createTestProblem();
        double[][] expectedCovariances = new double[][]{
                {1.41421373, 0.0},
                {0.0, 1.41421373}
        };
        double[][] covariances = estimator.getCovariances(problem);
        assertArrayEquals(expectedCovariances, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateJacobian();
            }
        };
        EstimationProblem problem = createTestProblem();
        double[] expectedErrors = new double[]{1.24073331, 1.24073331};
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, errors);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateResidualsAndCost();
            }
        };
        EstimationProblem problem = createTestProblem();
        estimator.initializeEstimate(problem);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        WeightedMeasurement[] measurements = problem.getMeasurements();
        EstimatedParameter[] parameters = problem.getAllParameters();
        assertEquals(measurements.length, estimator.rows);
        assertEquals(parameters.length, estimator.cols);
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
    }

    @Test
    public void testEstimateThrowsException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                estimator.updateResidualsAndCost();
                throw new EstimationException("test");
            }
        };
        EstimationProblem problem = createTestProblem();
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createTestProblem() {
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, new double[]{1.0, 1.0}, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, new double[]{1.0, -1.0}, 1.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, new double[]{-1.0, 1.0}, 1.0);
        WeightedMeasurement wm4 = new WeightedMeasurement(1.0, new double[]{-1.0, -1.0}, 1.0);
        EstimatedParameter param1 = new EstimatedParameter("param1", new double[]{1.0});
        EstimatedParameter param2 = new EstimatedParameter("param2", new double[]{1.0});
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{wm1, wm2, wm3, wm4};
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{param1, param2};
            }
        };
        return problem;
    }
}