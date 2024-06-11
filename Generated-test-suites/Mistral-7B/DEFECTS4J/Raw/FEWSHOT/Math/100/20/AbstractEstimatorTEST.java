package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

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

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.setMaxCostEval(-1);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                costEvaluations++;
            }
        };
        estimator.estimate(createTestEstimationProblem());
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;
            @Override
            protected void incrementJacobianEvaluationsCounter() {
                jacobianEvaluations++;
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        estimator.estimate(createTestEstimationProblem());
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            double[] jacobian = new double[10];
            @Override
            protected void updateJacobian() {
                Arrays.fill(jacobian, 1.0);
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        estimator.estimate(createTestEstimationProblem());
        assertArrayEquals(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, estimator.jacobian, 0.01);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int maxCostEval = 0;
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
            }
        };
        estimator.setMaxCostEval(0);
        estimator.estimate(createTestEstimationProblem());
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 0.0;
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        assertEquals(0.0, estimator.getRMS(problem), 0.01);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0.0;
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        assertEquals(0.0, estimator.getChiSquare(problem), 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                return new double[0][];
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        assertNull(estimator.getCovariances(problem));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[0];
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        assertArrayEquals(new double[0], estimator.guessParametersErrors(problem));
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // empty implementation
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        estimator.initializeEstimate(problem);

        assertArrayEquals(problem.getMeasurements(), estimator.measurements);
        assertArrayEquals(problem.getAllParameters(), estimator.parameters);
        assertEquals(problem.getMeasurements().length, estimator.rows);
        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }

    @Test
    public void testEstimateWithEmptyProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        assertEquals(0, estimator.estimate(new EstimationProblem(new WeightedMeasurement[0], new EstimatedParameter[0])));
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeNumberOfMeasurements() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[1], new EstimatedParameter[1]);
        problem.setMeasurements(new WeightedMeasurement[2]);
        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeNumberOfParameters() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[1], new EstimatedParameter[1]);
        problem.setAllParameters(new EstimatedParameter[2]);
        estimator.estimate(problem);
    }

    @Test
    public void testEstimateWithEmptyJacobian() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                // empty implementation
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        estimator.estimate(problem);
        assertArrayEquals(new double[0][], estimator.jacobian);
    }

    @Test
    public void testEstimateWithNonSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                Arrays.fill(jacobian, 1.0);
            }
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                return new double[1][1];
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
                RealMatrix covar = getCovariances(problem);
                jacobian = covar.transpose().multiply(jacobian).getData();
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        estimator.estimate(problem);
        assertTrue(AbstractEstimator.isNearlyIdentity(jacobian));
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                Arrays.fill(jacobian, 0.0);
                jacobian[0] = 1.0;
            }
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("unable to compute covariances: singular problem");
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
                getCovariances(problem);
            }
        };
        EstimationProblem problem = createTestEstimationProblem();
        estimator.estimate(problem);
    }

    private static EstimationProblem createTestEstimationProblem() {
        double[][] measurementsData = {
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
        };
        double[] parametersData = {1.0, 1.0};
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; ++i) {
            measurements[i] = new WeightedMeasurement(measurementsData[i], 1.0);
        }
        EstimatedParameter[] parameters = new EstimatedParameter[2];
        for (int i = 0; i < parameters.length; ++i) {
            parameters[i] = new EstimatedParameter(i, parametersData[i]);
        }
        return new EstimationProblem(measurements, parameters);
    }

    private static boolean isNearlyIdentity(double[][] matrix) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                if (Math.abs(matrix[i][j] - (i == j ? 1.0 : 0.0)) > 1e-9) {
                    return false;
                }
            }
        }
        return true;
    }
}