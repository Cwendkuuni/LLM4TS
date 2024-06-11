package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.stat.regression.WeightedMeasurement;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test of setMaxCostEval method, of class AbstractEstimator.
     */
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator instance = new MyAbstractEstimator();
        instance.setMaxCostEval(10);
        assertEquals(10, instance.getMaxCostEval());
    }

    /**
     * Test of getCostEvaluations method, of class AbstractEstimator.
     */
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator instance = new MyAbstractEstimator();
        assertEquals(0, instance.getCostEvaluations());
    }

    /**
     * Test of getJacobianEvaluations method, of class AbstractEstimator.
     */
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator instance = new MyAbstractEstimator();
        assertEquals(0, instance.getJacobianEvaluations());
    }

    /**
     * Test of updateJacobian method, of class AbstractEstimator.
     * @throws EstimationException
     */
    @Test
    public void testUpdateJacobian() throws EstimationException {
        AbstractEstimator instance = new MyAbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(2.0, 3.0);
        EstimatedParameter[] parameters = new EstimatedParameter[1];
        parameters[0] = new EstimatedParameter(1.0);
        instance.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        });
        instance.updateJacobian();
        RealMatrix expectedJacobian = new RealMatrixImpl(new double[][] {
            {-3.0}
        });
        RealMatrix actualJacobian = new RealMatrixImpl(instance.jacobian, 1, instance.cols);
        assertEquals(expectedJacobian, actualJacobian);
    }

    /**
     * Test of initializeEstimate method, of class AbstractEstimator.
     * @throws EstimationException
     */
    @Test
    public void testInitializeEstimate() throws EstimationException {
        AbstractEstimator instance = new MyAbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(2.0, 3.0);
        EstimatedParameter[] parameters = new EstimatedParameter[1];
        parameters[0] = new EstimatedParameter(1.0);
        instance.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        });
        assertEquals(1, instance.rows);
        assertEquals(1, instance.cols);
        assertEquals(1, instance.measurements.length);
        assertEquals(1, instance.parameters.length);
        assertArrayEquals(new double[] {2.0}, instance.measurements[0].getMeasurement());
        assertEquals(1.0, instance.parameters[0].getParameter(), 0.0);
        assertEquals(0.0, instance.cost, 0.0);
        assertEquals(0, instance.costEvaluations);
        assertEquals(0, instance.jacobianEvaluations);
    }

    /**
     * Test of estimate method, of class AbstractEstimator.
     * @throws EstimationException
     */
    @Test
    public void testEstimate() throws EstimationException {
        AbstractEstimator instance = new MyAbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        EstimatedParameter[] parameters = new EstimatedParameter[1];
        parameters[0] = new EstimatedParameter(1.0);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        };
        instance.initializeEstimate(problem);
        instance.estimate(problem);
        RealVector expectedParameters = new ArrayRealVector(new double[] {1.5});
        RealVector actualParameters = problem.getAllParameters();
        assertEquals(expectedParameters, actualParameters);
    }

    /**
     * Test of getRMS method, of class AbstractEstimator.
     */
    @Test
    public void testGetRMS() {
        AbstractEstimator instance = new MyAbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(2.0, 3.0);
        EstimatedParameter[] parameters = new EstimatedParameter[1];
        parameters[0] = new EstimatedParameter(1.0);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        };
        instance.initializeEstimate(problem);
        assertEquals(Math.sqrt(3.0), instance.getRMS(problem), 0.0);
    }

    /**
     * Test of getChiSquare method, of class AbstractEstimator.
     */
    @Test
    public void testGetChiSquare() {
        AbstractEstimator instance = new MyAbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(2.0, 3.0);
        EstimatedParameter[] parameters = new EstimatedParameter[1];
        parameters[0] = new EstimatedParameter(1.0);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        };
        instance.initializeEstimate(problem);
        double expectedChiSquare = 9.0;
        assertEquals(expectedChiSquare, instance.getChiSquare(problem), 0.0);
    }

    /**
     * Test of getCovariances method, of class AbstractEstimator.
     * @throws EstimationException
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator instance = new MyAbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        measurements[2] = new WeightedMeasurement(3.0, 9.0);
        EstimatedParameter[] parameters = new EstimatedParameter[1];
        parameters[0] = new EstimatedParameter(1.0);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        };
        instance.initializeEstimate(problem);
        instance.estimate(problem);
        RealMatrix expectedCovariances = new RealMatrixImpl(new double[][] {
            {2.3029319387755436, -1.633757291037313},
            {-1.633757291037313, 1.3826623830256183}
        });
        RealMatrix actualCovariances = instance.getCovariances(problem);
        assertEquals(expectedCovariances, actualCovariances);
    }

    /**
     * Test of guessParametersErrors method, of class AbstractEstimator.
     * @throws EstimationException
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator instance = new MyAbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        measurements[2] = new WeightedMeasurement(3.0, 9.0);
        EstimatedParameter[] parameters = new EstimatedParameter[1];
        parameters[0] = new EstimatedParameter(1.0);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }
        };
        instance.initializeEstimate(problem);
        instance.estimate(problem);
        double[] expectedErrors = new double[] {1.5433070875129123};
        double[] actualErrors = instance.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, actualErrors);
    }

    /**
     * Test of estimate method, of class AbstractEstimator with invalid maxCostEval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEstimateWithInvalidMaxCostEval() {
        AbstractEstimator instance = new MyAbstractEstimator();
        instance.setMaxCostEval(-1);
    }

    private static class MyAbstractEstimator extends AbstractEstimator {

        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            SimpleRegression estimator = new SimpleRegression();
            estimator.estimate(problem.getMeasurements(), problem.getUnboundParameters());
            System.arraycopy(estimator.estimateRealParameters(), 0, problem.getAllParameters(), 0, problem.getAllParameters().length);
        }

    }

}