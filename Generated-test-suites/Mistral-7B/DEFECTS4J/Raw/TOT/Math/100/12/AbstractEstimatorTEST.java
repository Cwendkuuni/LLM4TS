package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new DummyAbstractEstimator();
        problem = new DummyEstimationProblem();
    }

    @Test
    public void testSetAndGetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testSetAndGetCostEvaluations() {
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testSetAndGetJacobianEvaluations() {
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        RealArray2D expectedJacobian = new RealArray2D(2, 2);
        expectedJacobian.setEntry(0, 0, -1.0);
        expectedJacobian.setEntry(0, 1, -1.0);
        expectedJacobian.setEntry(1, 0, -1.0);
        expectedJacobian.setEntry(1, 1, -1.0);
        assertArrayEquals(expectedJacobian.getData(), estimator.jacobian, 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double[] expectedResiduals = {1.0, 1.0};
        assertArrayEquals(expectedResiduals, estimator.residuals, 0.0);
        assertEquals(Math.sqrt(2.0), estimator.cost, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() throws EstimationException {
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] expectedRMS = {Math.sqrt(2.0), Math.sqrt(2.0), Math.sqrt(2.0)};
        problem = new DummyEstimationProblem(data);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertArrayEquals(expectedRMS, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double expectedChiSquare = 14.0;
        problem = new DummyEstimationProblem(data);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] expectedCovariances = {1.0, 0.5, 0.5, 1.0};
        problem = new DummyEstimationProblem(data);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] actualCovariances = estimator.getCovariances(problem);
        assertArrayEquals(expectedCovariances, actualCovariances, 0.0);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double expectedStdDeviation = Math.sqrt(0.5);
        problem = new DummyEstimationProblem(data);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] actualErrors = estimator.guessParametersErrors(problem);
        for (int i = 0; i < actualErrors.length; i++) {
            assertEquals(expectedStdDeviation, actualErrors[i], 0.0);
        }
    }

}

class DummyAbstractEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // Do nothing
    }

}

class DummyEstimationProblem implements EstimationProblem {

    private double[][] measurementsData;

    public DummyEstimationProblem() {}

    public DummyEstimationProblem(double[][] measurementsData) {
        this.measurementsData = measurementsData;
    }

    @Override
    public WeightedMeasurement[] getMeasurements() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[measurementsData.length];
        for (int i = 0; i < measurementsData.length; i++) {
            measurements[i] = new WeightedMeasurement(measurementsData[i]);
        }
        return measurements;
    }

    @Override
    public EstimatedParameter[] getUnboundParameters() {
        return new EstimatedParameter[0];
    }

}