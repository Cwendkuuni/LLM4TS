package org.apache.commons.math.estimation.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.estimation.EstimatedParameter;
import org.apache.commons.math.estimation.AbstractEstimator.EstimationException;

/**
 * Tests for {@link AbstractEstimator} class.
 */
public class AbstractEstimatorTest {

    /** Test problem with 2 measurements and 3 parameters. */
    private EstimationProblem problem;

    /** Test estimator instance. */
    private AbstractEstimator estimator;

    /** Test estimator parameters. */
    private double[] estimatorParameters;

    /** Test estimator covariances. */
    private double[][] estimatorCovariances;

    /** Test error estimation. */
    private double[] estimatorErrors;

    /** Test RMS value. */
    private double rms;

    /** Test chi-square value. */
    private double chiSquare;

    /** Test covariance matrix. */
    private RealMatrix covariances;

    /** Test errors. */
    private double[] errors;

    /** Test jacobian. */
    private double[][] jacobian;

    /** Test residuals. */
    private double[] residuals;

    @Before
    public void setup() {
        // Initialize test problem
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public double[] getAllParameters() {
                return allParameters;
            }

            private WeightedMeasurement[] measurements = {
                new WeightedMeasurement(1, 2, 1, 3),
                new WeightedMeasurement(4, 5, 2, 6),
                new WeightedMeasurement(7, 8, 3, 9)
            };

            private EstimatedParameter[] parameters = {
                new EstimatedParameter("p1", 0, 1),
                new EstimatedParameter("p2", 0, 1),
                new EstimatedParameter("p3", 0, 1)
            };

            private double[] allParameters = { 0, 0, 0 };
        };

        // Initialize test estimator
        estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
                measurements = problem.getMeasurements();
                parameters = problem.getUnboundParameters();
            }

            @Override
            protected void updateJacobian() {
                super.updateJacobian();
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                super.updateResidualsAndCost();
            }

            @Override
            public double getRMS(EstimationProblem problem) {
                return rms;
            }

            @Override
            public double getChiSquare(EstimationProblem problem) {
                return chiSquare;
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                return covariances.getData();
            }

            @Override
            public double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                return errors;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimatorParameters = problem.getAllParameters();
            }
        };

        // Initialize test data
        estimatorParameters = new double[] { 1, 2, 3 };
        estimatorCovariances = new double[][] {
            { 1, 0, -1 }, { 0, 1, 0 }, { -1, 0, 2 }
        };
        rms = 1.2;
        chiSquare = 0.5;
        covariances = MatrixUtils.createRealMatrix(estimatorCovariances);
        SingularValueDecomposition svd = covariances.getSingularValueDecomposition();
        errors = new double[] { StatUtils.std(estimatorParameters),
                               StatUtils.std(estimatorParameters),
                               StatUtils.std(estimatorParameters) };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
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
        estimator.initializeEstimate(problem);
        estimator.estimate(problem); // needed to set jacobian
        estimator.updateJacobian();
        assertNotNull(jacobian);
        assertArrayEquals(estimatorCovariances[0], jacobian, 0.001);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem); // needed to set jacobian and residuals
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
        assertArrayEquals(new double[] { 1e-6, 1e-6, 1e-6 }, estimator.residuals, 0.001);
        assertEquals(rms, estimator.getRMS(problem), 0.001);
        assertEquals(chiSquare, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetRMS() {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(rms, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(chiSquare, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        RealMatrix expected = MatrixUtils.createRealMatrix(estimatorCovariances);
        RealMatrix actual = estimator.getCovariances(problem);
        SingularValueDecomposition svdExpected = expected.getSingularValueDecomposition();
        SingularValueDecomposition svdActual = actual.getSingularValueDecomposition();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(svdExpected.getSingularValues(), svdActual.getSingularValues(), 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertArrayEquals(errors, estimator.guessParametersErrors(problem), 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithMaxCostEvalReached() throws EstimationException {
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.estimate(problem);
    }

}