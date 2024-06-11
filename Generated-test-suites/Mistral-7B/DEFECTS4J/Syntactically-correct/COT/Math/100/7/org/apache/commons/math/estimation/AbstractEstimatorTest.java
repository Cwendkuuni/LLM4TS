import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.estimation.WeightedMeasurement;
import org.apache.commons.math3.estimation.EstimatedParameter;

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
        assertEquals(10, estimator.getMaxCostEval());
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
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testGetRms() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 4.0, 1.0),
            new WeightedMeasurement(3.0, 9.0, 2.0),
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double actual = estimator.getRMS(problem);
        double expected = Math.sqrt(2.0 / 3.0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 4.0, 1.0),
            new WeightedMeasurement(3.0, 9.0, 2.0),
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double actual = estimator.getChiSquare(problem);
        double expected = 2.0 / 2.0;
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesWithSingularProblem() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 4.0, 1.0),
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public int getParameterDimension() {
                return 3;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[][] covariances = estimator.getCovariances(problem);
        fail("Expected EstimationException");
    }

    @Test
    public void testGuessParametersErrors() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 4.0, 1.0),
            new WeightedMeasurement(3.0, 9.0, 2.0),
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                EstimatedParameter[] parameters = new EstimatedParameter[3];
                parameters[0] = new EstimatedParameter("p1", 1.0);
                parameters[1] = new EstimatedParameter("p2", 1.0);
                parameters[2] = new EstimatedParameter("p3", 1.0);
                return parameters;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(problem);
        Array2DRowRealMatrix covariances = new Array2DRowRealMatrix(3, 3);
        covariances.setEntry(0, 0, 1.0);
        covariances.setEntry(1, 1, 1.0);
        covariances.setEntry(2, 2, 1.0);
        covariances.setEntry(0, 2, 0.5);
        covariances.setEntry(1, 2, 0.5);
        covariances.setEntry(2, 0, 0.5);
        covariances.setEntry(2, 1, 0.5);
        RealMatrixImpl inverse = new RealMatrixImpl(covariances.transpose().multiply(covariances).inverse());
        double[] expected = new double[]{Math.sqrt(inverse.getEntry(0, 0) / 2.0),
                                          Math.sqrt(inverse.getEntry(1, 1) / 2.0),
                                          Math.sqrt(inverse.getEntry(2, 2) / 2.0)};
        assertArrayEquals(expected, errors, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsWithNegativeDegreesOfFreedom() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 4.0, 1.0),
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                EstimatedParameter[] parameters = new EstimatedParameter[3];
                parameters[0] = new EstimatedParameter("p1", 1.0);
                parameters[1] = new EstimatedParameter("p2", 1.0);
                parameters[2] = new EstimatedParameter("p3", 1.0);
                return parameters;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.guessParametersErrors(problem);
        fail("Expected EstimationException with negative degrees of freedom");
    }

    @Test
    public void testInitializeEstimate() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 4.0, 1.0),
        };

        EstimatedParameter[] parameters = new EstimatedParameter[]{
            new EstimatedParameter("p1", 0.0),
            new EstimatedParameter("p2", 0.0),
            new EstimatedParameter("p3", 0.0),
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return parameters;
            }
        });
        assertArrayEquals(measurements, estimator.measurements);
        assertArrayEquals(parameters, estimator.parameters);
        assertEquals(measurements.length, estimator.rows);
        assertEquals(parameters.length, estimator.cols);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithMaxCostEvalReached() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[]{
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 4.0, 1.0),
            new WeightedMeasurement(3.0, 9.0, 2.0),
            new WeightedMeasurement(4.0, 16.0, 3.0),
            new WeightedMeasurement(5.0, 25.0, 4.0),
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.setMaxCostEval(2);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.estimate(problem);
        estimator.estimate(problem);
        estimator.estimate(problem);
        estimator.estimate(problem);
    }
}