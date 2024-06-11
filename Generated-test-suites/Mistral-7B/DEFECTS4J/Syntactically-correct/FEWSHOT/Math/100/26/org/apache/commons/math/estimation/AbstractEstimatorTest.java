package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
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
            }
        };
        estimator.costEvaluations = 5;
        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.jacobianEvaluations = 7;
        assertEquals(7, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        estimator.initializeEstimate(new EstimationProblem(measurements, new EstimatedParameter[0]));

        estimator.updateJacobian();
        double[] expectedJacobian = new double[]{
            1.0 / Math.sqrt(1.0),  1.0 / Math.sqrt(1.0),  1.0 / Math.sqrt(1.0),
            1.0 / Math.sqrt(1.0),  1.0 / Math.sqrt(1.0),  1.0 / Math.sqrt(1.0),
            1.0 / Math.sqrt(1.0),  1.0 / Math.sqrt(1.0),  1.0 / Math.sqrt(1.0)
        };
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        estimator.initializeEstimate(new EstimationProblem(measurements, new EstimatedParameter[0]));

        estimator.updateResidualsAndCost();
        double expectedCost = Math.sqrt(6.0);
        assertEquals(expectedCost, estimator.cost, 0.0);
        assertArrayEquals(new double[]{Math.sqrt(1.0), Math.sqrt(1.0), Math.sqrt(1.0)}, estimator.residuals, 0.0);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[0], new EstimatedParameter[0]);
        double expectedRMS = 0.0;
        assertEquals(expectedRMS, estimator.getRMS(problem), 0.0);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        problem = new EstimationProblem(measurements, new EstimatedParameter[0]);
        expectedRMS = Math.sqrt(6.0 / 3.0);
        assertEquals(expectedRMS, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[0], new EstimatedParameter[0]);
        assertEquals(0.0, estimator.getChiSquare(problem), 0.0);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        problem = new EstimationProblem(measurements, new EstimatedParameter[0]);
        double expectedChiSquare = 6.0;
        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[0], new EstimatedParameter[0]);
        double[][] expectedCovariances = new double[1][1];
        assertArrayEquals(expectedCovariances, estimator.getCovariances(problem), 0.0);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        problem = new EstimationProblem(measurements, new EstimatedParameter[]{new EstimatedParameter(0, 1.0), new EstimatedParameter(1, 2.0)});
        double[][] expectedCovariances2 = new double[][]{{1.0 / 3.0, 1.0 / 3.0}, {1.0 / 3.0, 1.0 / 3.0}};
        RealMatrix expectedCovariancesRM = new RealMatrixImpl(expectedCovariances2);
        RealMatrix actualCovariancesRM = new RealMatrixImpl(estimator.getCovariances(problem));
        assertArrayEquals(expectedCovariancesRM.getData(), actualCovariancesRM.getData(), 0.0);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[0], new EstimatedParameter[0]);
        double[] expectedErrors = new double[0];
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 0.0);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        problem = new EstimationProblem(measurements, new EstimatedParameter[]{new EstimatedParameter(0, 1.0), new EstimatedParameter(1, 2.0)});
        double[] expectedErrors2 = new double[]{Math.sqrt(1.0 / 3.0), Math.sqrt(1.0 / 3.0)};
        assertArrayEquals(expectedErrors2, estimator.guessParametersErrors(problem), 0.0);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[0], new EstimatedParameter[0]);
        estimator.initializeEstimate(problem);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        problem = new EstimationProblem(measurements, new EstimatedParameter[]{new EstimatedParameter(0, 1.0), new EstimatedParameter(1, 2.0)});
        estimator.initializeEstimate(problem);

        WeightedMeasurement[] measurements3 = new WeightedMeasurement[4];
        for (int i = 0; i < measurements3.length; i++) {
            measurements3[i] = new WeightedMeasurement(i, 1.0);
        }
        assertThrows(IllegalArgumentException.class, () -> estimator.initializeEstimate(new EstimationProblem(measurements3, new EstimatedParameter[0])));
    }

    @Test
    public void testEstimate() throws EstimationException {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new EstimationProblem(new WeightedMeasurement[0], new EstimatedParameter[0]);
        assertThrows(UnsupportedOperationException.class, () -> estimator.estimate(problem));

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0);
        }
        problem = new EstimationProblem(measurements, new EstimatedParameter[]{new EstimatedParameter(0, 1.0), new EstimatedParameter(1, 2.0)});
        estimator.estimate(problem);
        EstimatedParameter[] expectedParameters = new EstimatedParameter[]{new EstimatedParameter(0, 1.0 + Math.pow(0.1, 0.5)), new EstimatedParameter(1, 2.0 + Math.pow(0.1, 0.5))};
        assertArrayEquals(expectedParameters, problem.getAllParameters(), 0.0);
    }
}