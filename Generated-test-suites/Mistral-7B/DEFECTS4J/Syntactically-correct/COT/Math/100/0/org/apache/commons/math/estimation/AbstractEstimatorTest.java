package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservedVersusPredicted;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new TestAbstractEstimator();
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(3.0, 5.0),
                    new WeightedMeasurement(4.0, 6.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                    new EstimatedParameter("p1"),
                    new EstimatedParameter("p2")
                };
            }
        };
        estimator.initializeEstimate(problem);
    }

    @Test
    public void testGetMaxCostEval() {
        int maxCostEval = 10;
        estimator.setMaxCostEval(maxCostEval);
        assertEquals(maxCostEval, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        int rows = problem.getMeasurements().length;
        int cols = problem.getAllParameters().length;
        assertNotNull(estimator.jacobian);
        assertEquals(rows * cols, estimator.jacobian.length);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.costEvaluations);
        assertEquals(3, estimator.jacobianEvaluations);
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(3.0 / 3.0), rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(9.0 / 3.0, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        RealMatrix covarianceMatrix = estimator.getCovariances(problem);
        assertNotNull(covarianceMatrix);
        assertEquals(problem.getAllParameters().length, covarianceMatrix.getRowDimension());
        assertEquals(problem.getAllParameters().length, covarianceMatrix.getColumnDimension());
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] errors = estimator.guessParametersErrors(problem);
        assertNotNull(errors);
        assertEquals(problem.getAllParameters().length, errors.length);
        for (double error : errors) {
            assertEquals(1.5, error, 0.01);
        }
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeNumberOfMeasurements() {
        EstimationProblem problemWithNegativeMeasurements = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {};
            }
        };
        estimator.initializeEstimate(problemWithNegativeMeasurements);
        estimator.estimate(problemWithNegativeMeasurements);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithMaxCostEvalReached() {
        int maxCostEval = 1;
        estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(maxCostEval);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    @Test
    public void testInitializeEstimateWithNullProblem() {
        estimator.initializeEstimate(null);
        assertThrows(IllegalArgumentException.class, () -> estimator.estimate(null));
    }

    @Test
    public void testEstimateWithNullMeasurements() {
        EstimationProblem problemWithNullMeasurements = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return null;
            }
        };
        estimator.initializeEstimate(problemWithNullMeasurements);
        assertThrows(IllegalArgumentException.class, () -> estimator.estimate(problemWithNullMeasurements));
    }

    @Test
    public void testEstimateWithNullParameters() {
        EstimationProblem problemWithNullParameters = new EstimationProblem() {
            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return null;
            }
        };
        estimator.initializeEstimate(problemWithNullParameters);
        assertThrows(IllegalArgumentException.class, () -> estimator.estimate(problemWithNullParameters));
    }

    @Test
    public void testGetCovariancesWithSingularProblem() throws EstimationException {
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(1.0, 2.0)
                };
            }
        };
        estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(problem);
        assertThrows(EstimationException.class, () -> estimator.getCovariances(problem));
    }

    @Test
    public void testGetResiduals() throws EstimationException {
        estimator.estimate(problem);
        RealVector residuals = new SimpleRegression(new WeightedObservedVersusPredicted(problem.getMeasurements(),
                estimator.getEstimatedParameters())).estimateResiduals();
        assertArrayEquals(estimator.residuals, residuals.toArray());
    }

    @Test
    public void testGetParameters() throws EstimationException {
        estimator.estimate(problem);
        double[] parameters = new double[] { 1.5, 1.5 };
        assertArrayEquals(parameters, estimator.getEstimatedParameters());
    }

}

class TestAbstractEstimator extends AbstractEstimator {

    public TestAbstractEstimator() {
        super();
    }

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // Abstract estimator does not provide any implementation for the estimate method
    }

}