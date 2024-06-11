package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractEstimator} class.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;
    private EstimationProblem problemWithSingularCovariance;
    private EstimationProblem problemWithLessMeasurementsThanParameters;

    @Before
    public void setUp() {
        estimator = new DummyEstimator();
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                    new WeightedMeasurement(1.0, new double[]{0.1, 0.2}),
                    new WeightedMeasurement(1.0, new double[]{0.3, 0.4}),
                    new WeightedMeasurement(1.0, new double[]{0.5, 0.6})
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{
                    new EstimatedParameter("p1", new double[]{1.0, 1.0}),
                    new EstimatedParameter("p2", new double[]{1.0, 1.0})
                };
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(1);
        estimator.estimate(problem);
        estimator.setMaxCostEval(10);
        estimator.estimate(problem);
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.estimate(problem);
        int numCostEvaluations = estimator.getCostEvaluations();
        assertEquals("wrong number of cost evaluations", 1, numCostEvaluations);
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.estimate(problem);
        int numJacobianEvaluations = estimator.getJacobianEvaluations();
        assertEquals("wrong number of jacobian evaluations", 1, numJacobianEvaluations);
    }

    @Test
    public void testUpdateJacobian() {
        estimator.estimate(problem);
        estimator.updateJacobian();
        double[][] jacobian = estimator.getJacobian();
        assertEquals("wrong jacobian size", 2, jacobian.length);
        assertEquals("wrong jacobian columns", 2, jacobian[0].length);
        assertEquals("wrong jacobian elements", 0.1, jacobian[0][0], 0.001);
        assertEquals("wrong jacobian elements", 0.2, jacobian[0][1], 0.001);
        assertEquals("wrong jacobian elements", 0.3, jacobian[1][0], 0.001);
        assertEquals("wrong jacobian elements", 0.4, jacobian[1][1], 0.001);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        estimator.estimate(problem);
        estimator.updateResidualsAndCost();
        double[] residuals = estimator.getResiduals();
        assertEquals("wrong size of residuals array", 3, residuals.length);
        assertEquals("wrong residual value", 0.005, residuals[0], 0.001);
        assertEquals("wrong residual value", -0.005, residuals[1], 0.001);
        assertEquals("wrong residual value", 0.0, residuals[2], 0.001);
        double cost = estimator.getCost();
        assertEquals("wrong cost value", 0.0125, cost, 0.0001);
    }

    @Test
    public void testGetRMS() {
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals("wrong RMS value", 0.0125, rms, 0.0001);
    }

    @Test
    public void testGetChiSquare() {
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals("wrong chi-square value", 0.0125, chiSquare, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        estimator.estimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals("wrong size of covariances matrix", 2, covariances.length);
        assertEquals("wrong size of covariances matrix columns", 2, covariances[0].length);
        assertEquals("wrong covariance value", 0.01, covariances[0][0], 0.001);
        assertEquals("wrong covariance value", 0.01, covariances[1][1], 0.001);
        assertEquals("wrong covariance value", 0.0, covariances[0][1], 0.001);
        assertEquals("wrong covariance value", 0.0, covariances[1][0], 0.001);
    }

    @Test
    public void testGuessParametersErrors() {
        estimator.estimate(problem);
        double[] guessErrors = estimator.guessParametersErrors(problem);
        assertEquals("wrong number of guess errors", 2, guessErrors.length);
        assertEquals("wrong guess error value", 0.125, guessErrors[0], 0.001);
        assertEquals("wrong guess error value", 0.125, guessErrors[1], 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithSingularCovariance() {
        problemWithSingularCovariance = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                    new WeightedMeasurement(1.0, new double[]{0.1, 0.2}),
                    new WeightedMeasurement(1.0, new double[]{0.3, 0.4}),
                    new WeightedMeasurement(1.0, new double[]{0.5, 0.5})
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{
                    new EstimatedParameter("p1", new double[]{1.0, 1.0}),
                    new EstimatedParameter("p2", new double[]{1.0, 1.0})
                };
            }
        };
        estimator.initializeEstimate(problemWithSingularCovariance);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithLessMeasurementsThanParameters() {
        problemWithLessMeasurementsThanParameters = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                    new WeightedMeasurement(1.0, new double[]{0.1, 0.2})
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{
                    new EstimatedParameter("p1", new double[]{1.0, 1.0}),
                    new EstimatedParameter("p2", new double[]{1.0, 1.0})
                };
            }
        };
        estimator.initializeEstimate(problemWithLessMeasurementsThanParameters);
        estimator.estimate(problemWithLessMeasurementsThanParameters);
    }

}

class DummyEstimator extends AbstractEstimator {

    @Override
    protected void initializeEstimate(EstimationProblem problem) {
        super.initializeEstimate(problem);
    }

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // empty implementation
    }

}