package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.ChiSquare;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /** Test data for the {@link AbstractEstimator#updateJacobian()} method. */
    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.setMaxCostEval(10);
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        estimator.initializeEstimate(new EstimationProblem(measurements));
        estimator.updateJacobian();
        double[] jacobian = estimator.jacobian;
        int rows = estimator.rows;
        int cols = estimator.cols;
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(new double[][] {
                { -2.0, -1.0 },
                { -4.0, -2.0 }
        });
        assertEquals(expectedJacobian, new RealMatrix(jacobian), 1e-15);
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    /** Test data for the {@link AbstractEstimator#updateResidualsAndCost()} method. */
    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.setMaxCostEval(10);
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        estimator.initializeEstimate(new EstimationProblem(measurements));
        estimator.updateResidualsAndCost();
        double[] residuals = estimator.residuals;
        double cost = estimator.cost;
        double expectedResiduals = new double[] { 1.0, 3.0 };
        double expectedCost = Math.sqrt(ChiSquare.chiSquare(new RealVector(new double[] { 1.0, 1.0 }),
                new RealVector(new double[] { 1.0, 1.0 }))) + Erf.erf(1.0 / Math.sqrt(2.0)) / (2 * Math.sqrt(Math.PI));
        assertEquals(expectedResiduals, estimator.residuals, 1e-15);
        assertEquals(expectedCost, estimator.cost, 1e-15);
        assertEquals(1, estimator.getCostEvaluations());
    }

    /** Test data for the {@link AbstractEstimator#getRMS(EstimationProblem)} method. */
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        double[] parameters = new double[] { 1.0, 1.0 };
        estimator.initializeEstimate(new EstimationProblem(measurements, parameters));
        double rms = estimator.getRMS(new EstimationProblem(measurements, parameters));
        double expectedRMS = Math.sqrt(1.0 / 2.0);
        assertEquals(expectedRMS, rms, 1e-15);
    }

    /** Test data for the {@link AbstractEstimator#getChiSquare(EstimationProblem)} method. */
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        double[] parameters = new double[] { 1.0, 1.0 };
        estimator.initializeEstimate(new EstimationProblem(measurements, parameters));
        double chiSquare = estimator.getChiSquare(new EstimationProblem(measurements, parameters));
        double expectedChiSquare = 3.0;
        assertEquals(expectedChiSquare, chiSquare, 1e-15);
    }

    /** Test data for the {@link AbstractEstimator#getCovariances(EstimationProblem)} method. */
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        double[] parameters = new double[] { 1.0, 1.0 };
        estimator.initializeEstimate(new EstimationProblem(measurements, parameters));
        double[][] covariances = estimator.getCovariances(new EstimationProblem(measurements, parameters));
        double[][] expectedCovariances = new double[][] { { 0.5, 0.5 }, { 0.5, 0.5 } };
        assertEquals(expectedCovariances, covariances);
    }

    /** Test data for the {@link AbstractEstimator#guessParametersErrors(EstimationProblem)} method. */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        measurements[2] = new WeightedMeasurement(3.0, 9.0);
        double[] parameters = new double[] { 1.0, 1.0 };
        estimator.initializeEstimate(new EstimationProblem(measurements, parameters));
        double[] errors = estimator.guessParametersErrors(new EstimationProblem(measurements, parameters));
        double[] expectedErrors = new double[] { 2.23606798, 2.23606798 };
        assertEquals(expectedErrors, errors);
    }

    /** Test data for the {@link AbstractEstimator#estimate(EstimationProblem)} method with a valid problem. */
    @Test
    public void testEstimateValidProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        double[] parameters = new double[] { 1.0, 1.0 };
        estimator.setMaxCostEval(10);
        estimator.initializeEstimate(new EstimationProblem(measurements, parameters));
        estimator.estimate(new EstimationProblem(measurements, parameters));
        double[] estimatedParameters = new EstimationProblem(measurements, parameters).getAllParameters();
        assertEquals(new double[] { 1.0, 1.0 }, estimatedParameters);
    }

    /** Test data for the {@link AbstractEstimator#estimate(EstimationProblem)} method with an invalid problem. */
    @Test(expected = EstimationException.class)
    public void testEstimateInvalidProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 4.0);
        double[] parameters = new double[] { 1.0, 1.0, 1.0 };
        estimator.setMaxCostEval(10);
        estimator.initializeEstimate(new EstimationProblem(measurements, parameters));
        estimator.estimate(new EstimationProblem(measurements, parameters));
    }

}