package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.special.ChiSquare;
import org.junit.Test;

public class AbstractEstimatorTest {
    @Test
    public void testGetRMS() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[] expected = {1.0, 1.58113883239836, 2.23606797749979};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[3];
                for (int i = 0; i < measurements.length; i++) {
                    measurements[i] = new WeightedMeasurement(new ArrayRealVector(new double[]{data[i][0], data[i][1], data[i][2]}), 1.0);
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        assertArrayEquals(expected, new double[]{(double) Math.sqrt(ChiSquare.chiSquare(3, rms * rms, true) / 2.0), rms, rms}, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[3];
                for (int i = 0; i < measurements.length; i++) {
                    measurements[i] = new WeightedMeasurement(new ArrayRealVector(new double[]{data[i][0], data[i][1], data[i][2]}), 1.0);
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(17.0 / 2.0, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[3];
                for (int i = 0; i < measurements.length; i++) {
                    measurements[i] = new WeightedMeasurement(new ArrayRealVector(new double[]{data[i][0], data[i][1], data[i][2]}), 1.0);
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        RealMatrix covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{
                {5.25, 0.5, 0.5},
                {0.5, 5.25, 0.5},
                {0.5, 0.5, 5.25}
        });
        assertTrue(covariances.equals(expectedCovariances, 0.001));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[3];
                for (int i = 0; i < measurements.length; i++) {
                    measurements[i] = new WeightedMeasurement(new ArrayRealVector(new double[]{data[i][0], data[i][1], data[i][2]}), 1.0);
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[]{2.24744871391552, 2.24744871391552, 2.24744871391552};
        assertArrayEquals(expectedErrors, errors, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesSingularProblem() throws EstimationException {
        double[][] data = new double[][]{{1, 2}, {1, 2}};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[2];
                for (int i = 0; i < measurements.length; i++) {
                    measurements[i] = new WeightedMeasurement(new ArrayRealVector(new double[]{data[i][0], data[i][1]}), 1.0);
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2")};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator();
        estimator.initializeEstimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsNoDegreesOfFreedom() throws EstimationException {
        double[][] data = new double[][]{{1, 2}, {1, 2}};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] measurements = new WeightedMeasurement[2];
                for (int i = 0; i < measurements.length; i++) {
                    measurements[i] = new WeightedMeasurement(new ArrayRealVector(new double[]{data[i][0], data[i][1]}), 1.0);
                }
                return measurements;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2"), new EstimatedParameter("p3")};
            }
        };
        AbstractEstimator estimator = new AbstractEstimator();
        estimator.initializeEstimate(problem);
    }
}