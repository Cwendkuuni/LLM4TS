package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new StubEstimator();
        problem = new StubEstimationProblem();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(1);
        assertEquals(1, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        RealMatrix expectedJacobian = new RealMatrixImpl(new RealArray2D.Dense2D(3, 2, new double[][]{{-1.0, -1.0},
                {-1.0,  1.0}, {-1.0,  1.0}}));
        assertEquals(expectedJacobian, estimator.getJacobian());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() {
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        for (int i = 0; i < 2; ++i) {
            estimator.updateResidualsAndCost();
        }
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double[] expectedResiduals = new double[]{1.0, 2.0, 3.0};
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(1.0, 1.0, expectedResiduals),
                new WeightedMeasurement(2.0, 1.0, expectedResiduals), new WeightedMeasurement(3.0, 1.0, expectedResiduals)});
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(13.0 / 3.0), rms, 1e-10);
    }

    @Test
    public void testGetChiSquare() {
        double[] expectedResiduals = new double[]{1.0, 2.0, 3.0};
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(1.0, 1.0, expectedResiduals),
                new WeightedMeasurement(2.0, 1.0, expectedResiduals), new WeightedMeasurement(3.0, 1.0, expectedResiduals)});
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(13.0, chiSquare, 1e-10);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[] expectedCovariances = new double[]{1.0, 0.5, 0.5, 1.0};
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(1.0, 1.0, new double[]{1.0, 2.0}),
                new WeightedMeasurement(2.0, 1.0, new double[]{2.0, 3.0}), new WeightedMeasurement(3.0, 1.0, new double[]{3.0, 4.0})});
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        RealMatrix expectedCovarianceMatrix = new RealMatrixImpl(new RealArray2D.Dense2D(4, 4, expectedCovariances));
        RealMatrix covariances = estimator.getCovariances(problem);
        assertEquals(expectedCovarianceMatrix, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] expectedErrors = new double[]{1.0, 0.5};
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(1.0, 1.0, new double[]{1.0, 2.0}),
                new WeightedMeasurement(2.0, 1.0, new double[]{2.0, 3.0}), new WeightedMeasurement(3.0, 1.0, new double[]{3.0, 4.0})});
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double degreesOfFreedom = problem.getMeasurements().length - problem.getAllParameters().length;
        double rms = estimator.getRMS(problem);
        assertFalse(Precision.isClose(rms, 0.0, 1e-10));
        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = rms * rms / degreesOfFreedom;
        assertEquals(expectedChiSquare, chiSquare, 1e-10);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, errors);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsWithSingularProblem() throws EstimationException {
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(1.0, 1.0, new double[]{1.0, 2.0}),
                new WeightedMeasurement(2.0, 1.0, new double[]{2.0, 3.0})});
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

}

class StubEstimationProblem implements EstimationProblem {

    private WeightedMeasurement[] measurements;

    public void setMeasurements(WeightedMeasurement[] measurements) {
        this.measurements = measurements;
    }

    public WeightedMeasurement[] getMeasurements() {
        return measurements;
    }

    public double[] getAllParameters() {
        return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2")}.clone();
    }

}

class StubEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
    }

}