package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new DummyEstimationProblem(2, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // implementation not relevant for this test
            }
        };
        estimator.initializeEstimate(problem);

        assertNotNull(estimator.measurements);
        assertNotNull(estimator.parameters);
        assertArrayEquals(new double[2 * 2], estimator.jacobian);
        assertNotNull(estimator.residuals);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializeEstimateWithInvalidProblem() {
        EstimationProblem problem = new DummyEstimationProblem(-1, 2);
        AbstractEstimator estimator = new AbstractEstimator();
        estimator.initializeEstimate(problem);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new DummyEstimationProblem(2, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem estimationProblem) {
                return 0;
            }

            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // implementation not relevant for this test
            }
        };
        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(0, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new DummyEstimationProblem(2, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem estimationProblem) {
                return 0;
            }

            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // implementation not relevant for this test
            }
        };
        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(0, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem(2, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem estimationProblem)
                    throws EstimationException {
                return new double[2][2];
            }

            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // implementation not relevant for this test
            }
        };
        estimator.initializeEstimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem(2, 2);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem estimationProblem)
                    throws EstimationException {
                return new double[]{0.1, 0.2};
            }

            @Override
            public void estimate(EstimationProblem estimationProblem) throws EstimationException {
                // implementation not relevant for this test
            }
        };
        estimator.initializeEstimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2, errors.length);
        assertTrue(errors[0] > 0);
        assertTrue(errors[1] > 0);
    }

}

class DummyEstimationProblem implements EstimationProblem {

    private final int rows;
    private final int cols;

    public DummyEstimationProblem(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[rows];
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[cols];
    }

}