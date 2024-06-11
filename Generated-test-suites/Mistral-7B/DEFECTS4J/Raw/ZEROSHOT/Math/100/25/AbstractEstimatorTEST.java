package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for AbstractEstimator.
 */
public class AbstractEstimatorTest {

    /**
     * Test of getRMS method, of class AbstractEstimator.
     */
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                //do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0),
                    new WeightedMeasurement(1.0, 0.5),
                    new WeightedMeasurement(1.0, 0.8),
                };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        estimator.initializeEstimate(problem);

        double rms = estimator.getRMS(problem);
        assertEquals(1.0, rms, 1e-8);
    }

    /**
     * Test of getChiSquare method, of class AbstractEstimator.
     */
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                //do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0),
                    new WeightedMeasurement(1.0, 0.5),
                    new WeightedMeasurement(1.0, 0.8),
                };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        estimator.initializeEstimate(problem);

        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(2.4, chiSquare, 1e-8);
    }

    /**
     * Test of getCovariances method, of class AbstractEstimator.
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                //do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0),
                    new WeightedMeasurement(1.0, 0.5),
                    new WeightedMeasurement(1.0, 0.8),
                };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        estimator.initializeEstimate(problem);
        double[][] covariances = estimator.getCovariances(problem);

        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);

        SimpleRegression regression = new SimpleRegression();
        double[][] data = new double[][] {
            {1.0, 1.0},
            {1.0, 0.5},
            {1.0, 0.8},
        };
        double[] parameters = regression.estimateRegressionParameters(data);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {StatUtils.VARIANCE, StatUtils.CORREL},
            {StatUtils.CORREL, StatUtils.VARIANCE},
        });

        RealMatrix actual = new Array2DRowRealMatrix(covariances);

        assertEquals(expected.getEntry(0, 0), actual.getEntry(0, 0), 1e-8);
        assertEquals(expected.getEntry(0, 1), actual.getEntry(0, 1), 1e-8);
        assertEquals(expected.getEntry(1, 0), actual.getEntry(1, 0), 1e-8);
        assertEquals(expected.getEntry(1, 1), actual.getEntry(1, 1), 1e-8);

        assertTrue(Precision.isClose(expected.getEntry(0, 0), actual.getEntry(0, 0), 1e-8));
        assertTrue(Precision.isClose(expected.getEntry(0, 1), actual.getEntry(0, 1), 1e-8));
        assertTrue(Precision.isClose(expected.getEntry(1, 0), actual.getEntry(1, 0), 1e-8));
        assertTrue(Precision.isClose(expected.getEntry(1, 1), actual.getEntry(1, 1), 1e-8));
    }

    /**
     * Test of guessParametersErrors method, of class AbstractEstimator.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                //do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0),
                    new WeightedMeasurement(1.0, 0.5),
                    new WeightedMeasurement(1.0, 0.8),
                };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        estimator.initializeEstimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);

        assertEquals(2, errors.length);

        RealVector expected = new ArrayRealVector(new double[] { 0.65, 0.65 });
        RealVector actual = new ArrayRealVector(errors);

        assertEquals(expected.getEntry(0), actual.getEntry(0), 1e-8);
        assertEquals(expected.getEntry(1), actual.getEntry(1), 1e-8);

        assertTrue(Precision.isClose(expected.getEntry(0), actual.getEntry(0), 1e-8));
        assertTrue(Precision.isClose(expected.getEntry(1), actual.getEntry(1), 1e-8));
    }

    /**
     * Test of estimate method, of class AbstractEstimator.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("Test EstimationException");
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[0];
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        };

        estimator.setMaxCostEval(0);
        estimator.initializeEstimate(problem);

        estimator.estimate(problem);
    }

    /**
     * Test of estimate method, of class AbstractEstimator.
     */
    @Test
    public void testEstimateSuccess() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                //do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0),
                    new WeightedMeasurement(1.0, 0.5),
                    new WeightedMeasurement(1.0, 0.8),
                };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        estimator.setMaxCostEval(10000);
        estimator.initializeEstimate(problem);

        estimator.estimate(problem);

        double[] parameters = problem.getAllParameters();
        double[] estimatedParameters = problem.getAllParameters();

        assertEquals(parameters[0], estimatedParameters[0], 1e-8);
        assertEquals(parameters[1], estimatedParameters[1], 1e-8);

        assertTrue(Precision.isClose(parameters[0], estimatedParameters[0], 1e-8));
        assertTrue(Precision.isClose(parameters[1], estimatedParameters[1], 1e-8));
    }

    /**
     * Test of estimate method, of class AbstractEstimator.
     */
    @Test
    public void testEstimateConvergence() throws EstimationException {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0),
                    new WeightedMeasurement(1.0, 0.5),
                    new WeightedMeasurement(1.0, 0.8),
                };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        estimator.setMaxCostEval(10000);
        estimator.initializeEstimate(problem);

        estimator.estimate(problem);

        double[] parameters = problem.getAllParameters();
        double[] estimatedParameters = problem.getAllParameters();

        assertEquals(parameters[0], estimatedParameters[0], 1e-8);
        assertEquals(parameters[1], estimatedParameters[1], 1e-8);

        assertTrue(Precision.isClose(parameters[0], estimatedParameters[0], 1e-8));
        assertTrue(Precision.isClose(parameters[1], estimatedParameters[1], 1e-8));

        assertFalse(estimator.isIterationFailed());
    }

    /**
     * Test of estimate method, of class AbstractEstimator.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateIterationFailed() throws EstimationException {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0),
                    new WeightedMeasurement(1.0, 0.5),
                    new WeightedMeasurement(1.0, 0.8),
                };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);

        estimator.estimate(problem);
    }
}