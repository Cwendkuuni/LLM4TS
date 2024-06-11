package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.stat.descript.Statistics;
import org.junit.After;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @After
    public void after() {
        if (estimator != null) {
            estimator.dispose();
            estimator = null;
            problem = null;
        }
    }

    @Test
    public void testSetAndGetMaxCostEval() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }
        };

        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        estimator.initializeEstimate(createProblem());
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(0.0, estimator.cost, 1e-9);
    }

    @Test
    public void testGetRMS() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        EstimationProblem problem = createProblem();
        double[] expectedRMS = new double[]{1.0, 1.0, 1.0};
        double[] actualRMS = estimator.getRMS(problem);
        assertArrayEquals(expectedRMS, actualRMS);
    }

    @Test
    public void testGetChiSquare() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        EstimationProblem problem = createProblem();
        double expectedChiSquare = 3.0;
        double actualChiSquare = estimator.getChiSquare(problem);
        assertEquals(expectedChiSquare, actualChiSquare, 1e-9);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        EstimationProblem problem = createProblem();
        double[][] expectedCovariances = new double[][]{
                {1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        double[][] actualCovariances = estimator.getCovariances(problem);
        assertArrayEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        EstimationProblem problem = createProblem();
        double[] expectedErrors = new double[]{1.0, 1.0, 1.0};
        double[] actualErrors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, actualErrors);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithMaxCostEvalReached() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
                setMaxCostEval(1);
                updateResidualsAndCost();
            }
        };

        problem = createProblem();
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testSingularCovarianceMatrix() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        EstimationProblem problem = createProblemWithSingularCovarianceMatrix();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testInvalidNumberOfDegreesOfFreedom() throws EstimationException {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };

        EstimationProblem problem = createProblemWithInvalidNumberOfDegreesOfFreedom();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    private EstimationProblem createProblem() {
        RealVector measurementsVector = VectorUtils.createVector(new double[]{1.0, 2.0, 3.0});
        RealMatrix parametersMatrix = VectorUtils.createMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {0.0, 0.0}});
        return new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                return measurementsVector;
            }

            @Override
            public RealMatrix getAllParameters() {
                return parametersMatrix;
            }
        };
    }

    private EstimationProblem createProblemWithSingularCovarianceMatrix() {
        RealVector measurementsVector = VectorUtils.createVector(new double[]{1.0, 1.0, 1.0});
        RealMatrix parametersMatrix = VectorUtils.createMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {0.0, 0.0}});
        return new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                return measurementsVector;
            }

            @Override
            public RealMatrix getAllParameters() {
                return parametersMatrix;
            }

            @Override
            public RealMatrix getCovarianceMatrix() {
                return new RealMatrixImpl(VectorUtils.createMatrix(new double[][]{
                        {0.0, 0.0},
                        {0.0, 0.0},
                        {0.0, 0.0}}));
            }
        };
    }

    private EstimationProblem createProblemWithInvalidNumberOfDegreesOfFreedom() {
        RealVector measurementsVector = VectorUtils.createVector(new double[]{1.0, 2.0});
        RealMatrix parametersMatrix = VectorUtils.createMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {0.0, 0.0}});
        return new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                return measurementsVector;
            }

            @Override
            public RealMatrix getAllParameters() {
                return parametersMatrix;
            }

            @Override
            public int getNumberOfDegreesOfFreedom() {
                return 2;
            }
        };
    }
}