package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.estimation.*;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public int getMeasurementsLength() {
                return 0;
            }

            @Override
            public int getParametersLength() {
                return 0;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
            }
        };

        estimator.initializeEstimate(problem);

        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertArrayEquals(new double[0], estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 0.0);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertEquals(0, estimator.rows);
        assertEquals(0, estimator.cols);
    }

    @Test
    public void testGetRMS() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement(0.1, 1.0, 0.01),
                new WeightedMeasurement(0.2, 1.5, 0.02),
                new WeightedMeasurement(0.3, 0.8, 0.03)
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public int getMeasurementsLength() {
                return measurements.length;
            }

            @Override
            public int getParametersLength() {
                return 0;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 0;
            }
        };

        estimator.initializeEstimate(problem);

        double expectedRMS = Math.sqrt(0.054);

        assertEquals(expectedRMS, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement(0.1, 1.0, 0.01),
                new WeightedMeasurement(0.2, 1.5, 0.02),
                new WeightedMeasurement(0.3, 0.8, 0.03)
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public int getMeasurementsLength() {
                return measurements.length;
            }

            @Override
            public int getParametersLength() {
                return 0;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0;
            }
        };

        estimator.initializeEstimate(problem);

        double expectedChiSquare = 0.108;

        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement(0.1, 1.0, 0.01),
                new WeightedMeasurement(0.2, 1.5, 0.02),
                new WeightedMeasurement(0.3, 0.8, 0.03)
        };

        EstimatedParameter[] parameters = new EstimatedParameter[] {
                new EstimatedParameter("p1", 0.0),
                new EstimatedParameter("p2", 0.0)
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public int getMeasurementsLength() {
                return measurements.length;
            }

            @Override
            public int getParametersLength() {
                return parameters.length;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                return new double[0][0];
            }
        };

        estimator.initializeEstimate(problem);

        double[][] expectedCovariances = new double[][] {
                {0.00253665, 0.00126832},
                {0.00126832, 0.00253665}
        };

        RealMatrix expectedMatrix = new RealMatrixImpl(expectedCovariances);
        RealMatrix actualMatrix = estimator.getCovariances(problem);

        Array2DRowRealMatrix actualArray = (Array2DRowRealMatrix) actualMatrix;

        assertArrayEquals(expectedCovariances[0], actualArray.getRowVector(0).toArray(), 0.0001);
        assertArrayEquals(expectedCovariances[1], actualArray.getRowVector(1).toArray(), 0.0001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement(0.1, 1.0, 0.01),
                new WeightedMeasurement(0.2, 1.5, 0.02),
                new WeightedMeasurement(0.3, 0.8, 0.03)
        };

        EstimatedParameter[] parameters = new EstimatedParameter[] {
                new EstimatedParameter("p1", 0.0),
                new EstimatedParameter("p2", 0.0)
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public int getMeasurementsLength() {
                return measurements.length;
            }

            @Override
            public int getParametersLength() {
                return parameters.length;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                return new double[0];
            }
        };

        estimator.initializeEstimate(problem);

        double[] expectedErrors = new double[] {0.015811025, 0.015811025};

        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 0.001);
    }
}