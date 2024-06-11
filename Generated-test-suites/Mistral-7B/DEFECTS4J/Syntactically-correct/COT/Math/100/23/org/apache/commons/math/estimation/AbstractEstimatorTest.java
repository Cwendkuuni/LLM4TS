package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowAccessor;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservedVersusFit;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AbstractEstimator.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setup() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        };
    }

    @Test
    public void testSetAndGetMaxCostEval() {
        estimator.setMaxCostEval(10);
        int maxCostEval = estimator.getMaxCostEval();
        assertEquals(10, maxCostEval);
    }

    @Test
    public void testSetAndGetCostEvaluations() {
        estimator.costEvaluations = 5;
        int costEvaluations = estimator.getCostEvaluations();
        assertEquals(5, costEvaluations);
    }

    @Test
    public void testSetAndGetJacobianEvaluations() {
        estimator.jacobianEvaluations = 7;
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        assertEquals(7, jacobianEvaluations);
    }

    @Test
    public void testUpdateJacobianWithEmptyProblem() {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        assertTrue(estimator.jacobianEvaluations == 0);
        assertTrue(Arrays.equals(new double[0], estimator.jacobian));
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithEmptyProblem() {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        fail("EstimationException expected");
    }

    @Test
    public void testGetRMSWithEmptyProblem() {
        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(Double.NaN, rms, 1e-12);
    }

    @Test
    public void testGetChiSquareWithEmptyProblem() {
        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(Double.NaN, chiSquare, 1e-12);
    }

    @Test
    public void testGetCovariancesWithEmptyProblem() {
        estimator.initializeEstimate(problem);
        try {
            estimator.getCovariances(problem);
            fail("EstimationException expected");
        } catch (EstimationException e) {
            // expected
        }
    }

    @Test
    public void testGuessParametersErrorsWithEmptyProblem() {
        estimator.initializeEstimate(problem);
        try {
            estimator.guessParametersErrors(problem);
            fail("EstimationException expected");
        } catch (EstimationException e) {
            // expected
        }
    }

    @Test
    public void testInitializeEstimateWithEmptyProblem() {
        estimator.initializeEstimate(problem);
        assertTrue(estimator.costEvaluations == 0);
        assertTrue(Arrays.equals(new double[0][0], estimator.jacobian));
        assertTrue(Arrays.equals(new double[0], estimator.residuals));
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 1e-12);
    }

    @Test
    public void testUpdateJacobianWithSingleMeasurement() {
        WeightedMeasurement wm = new WeightedMeasurement(1.0, 1.0, 0.1);
        problem.setMeasurements(new WeightedMeasurement[] {wm});
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        int expectedJacobianEvaluations = 1;
        int actualJacobianEvaluations = estimator.jacobianEvaluations;
        assertEquals(expectedJacobianEvaluations, actualJacobianEvaluations);
        double[] expectedJacobian = new double[] {-0.1, -0.1};
        RealMatrix expectedJacobianMatrix = new Array2DRowRealMatrix(1, 2, expectedJacobian);
        double[] actualJacobian = estimator.jacobian;
        RealMatrix actualJacobianMatrix = new Array2DRowRealMatrix(1, 2, actualJacobian);
        RealArray2DRowAccessor expectedJacobianAccessor = expectedJacobianMatrix.dataAccess();
        RealArray2DRowAccessor actualJacobianAccessor = actualJacobianMatrix.dataAccess();
        for (int i = 0; i < 2; ++i) {
            assertEquals(expectedJacobian[i], actualJacobian[i], 1e-12);
            assertEquals(expectedJacobianAccessor.getEntry(0, i), actualJacobianAccessor.getEntry(0, i), 1e-12);
        }
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithSingleMeasurement() {
        WeightedMeasurement wm = new WeightedMeasurement(1.0, 1.0, 0.1);
        problem.setMeasurements(new WeightedMeasurement[] {wm});
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        fail("EstimationException expected");
    }

    @Test
    public void testGetRMSWithSingleMeasurement() {
        WeightedMeasurement wm = new WeightedMeasurement(1.0, 1.0, 0.1);
        problem.setMeasurements(new WeightedMeasurement[] {wm});
        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        double expectedRms = Math.sqrt(0.1 * Math.pow(1.0, 2) / 1.0);
        assertEquals(expectedRms, rms, 1e-12);
    }

    @Test
    public void testGetChiSquareWithSingleMeasurement() {
        WeightedMeasurement wm = new WeightedMeasurement(1.0, 1.0, 0.1);
        problem.setMeasurements(new WeightedMeasurement[] {wm});
        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = 0.1 * Math.pow(1.0, 2) / 0.1;
        assertEquals(expectedChiSquare, chiSquare, 1e-12);
    }

    @Test
    public void testGetCovariancesWithSingleMeasurement() throws EstimationException {
        WeightedMeasurement wm = new WeightedMeasurement(1.0, 1.0, 0.1);
        problem.setMeasurements(new WeightedMeasurement[] {wm});
        estimator.initializeEstimate(problem);
        double[][] expectedCovariances = new double[][] {{0.1, 0.0}, {0.0, 0.1}};
        double[][] actualCovariances = estimator.getCovariances(problem);
        assertTrue(Arrays.deepEquals(expectedCovariances, actualCovariances));
    }

    @Test
    public void testGuessParametersErrorsWithSingleMeasurement() throws EstimationException {
        WeightedMeasurement wm = new WeightedMeasurement(1.0, 1.0, 0.1);
        problem.setMeasurements(new WeightedMeasurement[] {wm});
        estimator.initializeEstimate(problem);
        double[] expectedErrors = new double[] {Math.sqrt(0.1 / 1.0), Math.sqrt(0.1 / 1.0)};
        double[] actualErrors = estimator.guessParametersErrors(problem);
        assertTrue(Arrays.equals(expectedErrors, actualErrors));
    }

    @Test
    public void testEstimateWithIdentityProblem() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 2.0, 1.0);
        EstimatedParameter[] parameters = new EstimatedParameter[2];
        parameters[0] = new EstimatedParameter(1.0, 0.0);
        parameters[1] = new EstimatedParameter(0.0, 1.0);
        problem.setMeasurements(measurements);
        problem.setAllParameters(parameters);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        EstimatedParameter[] actualParameters = problem.getAllParameters();
        double[] expectedParameters = new double[] {1.0, 1.0};
        assertTrue(Arrays.equals(expectedParameters, actualParameters));
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithSingularProblem() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 2.0, 0.0);
        EstimatedParameter[] parameters = new EstimatedParameter[2];
        parameters[0] = new EstimatedParameter(1.0, 0.0);
        parameters[1] = new EstimatedParameter(0.0, 1.0);
        problem.setMeasurements(measurements);
        problem.setAllParameters(parameters);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        fail("EstimationException expected");
    }

    @Test
    public void testGetCovariancesWithSingularProblem() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 2.0, 0.0);
        EstimatedParameter[] parameters = new EstimatedParameter[2];
        parameters[0] = new EstimatedParameter(1.0, 0.0);
        parameters[1] = new EstimatedParameter(0.0, 1.0);
        problem.setMeasurements(measurements);
        problem.setAllParameters(parameters);
        estimator.initializeEstimate(problem);
        try {
            estimator.getCovariances(problem);
            fail("EstimationException expected");
        } catch (EstimationException e) {
            // expected
        }
    }

}