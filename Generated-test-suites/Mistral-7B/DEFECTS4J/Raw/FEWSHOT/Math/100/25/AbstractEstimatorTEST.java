package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInitializeEstimateWithNullProblem() {
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(null);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 3), new WeightedMeasurement(1, 3, 5)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(10.0 / 3.0), rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 3), new WeightedMeasurement(1, 3, 5)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(10.0, chiSquare, 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 3), new WeightedMeasurement(1, 3, 5)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(3, errors.length);
        assertArrayEquals(new double[] {1.5811388300841898d, 1.5811388300841898d, 1.5811388300841898d}, errors, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 3), new WeightedMeasurement(1, 3, 5)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][] {{0.20266112284816886, 0.0, 0.0}, {0.0, 0.20266112284816886, 0.0}, {0.0, 0.0, 0.20266112284816886}});
        assertEquals(3, covariances.length);
        assertEquals(3, covariances[0].length);
        assertEquals(3, covariances[1].length);
        assertEquals(3, covariances[2].length);
        assertArrayEquals(expectedCovariances.getData(), covariances, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesWithSingularProblem() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {new WeightedMeasurement(1, 1, 1), new WeightedMeasurement(1, 2, 3), new WeightedMeasurement(1, 2, 3)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.getCovariances(problem);
    }
}

class MyAbstractEstimator extends AbstractEstimator {
}