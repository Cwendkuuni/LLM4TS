package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowAccess;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing AbstractEstimator class.
 */
public class AbstractEstimatorTest {

    /**
     * Test initialization of AbstractEstimator.
     * @throws EstimationException if initialization fails
     */
    @Test
    public void testInitializeEstimate() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0, 0.1),
                    new WeightedMeasurement(2.0, 2.0, 0.1),
                    new WeightedMeasurement(3.0, 3.0, 0.1),
                    new WeightedMeasurement(4.0, 4.0, 0.1)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                    new EstimatedParameter("p1", 0.0),
                    new EstimatedParameter("p2", 0.0)
                };
            }
        };

        estimator.initializeEstimate(problem);

        Assert.assertArrayEquals(new WeightedMeasurement[] {
            new WeightedMeasurement(1.0, 1.0, 0.1),
            new WeightedMeasurement(2.0, 2.0, 0.1),
            new WeightedMeasurement(3.0, 3.0, 0.1),
            new WeightedMeasurement(4.0, 4.0, 0.1)
        }, estimator.measurements);

        Assert.assertArrayEquals(new EstimatedParameter[] {
            new EstimatedParameter("p1", 0.0),
            new EstimatedParameter("p2", 0.0)
        }, estimator.parameters);

        Assert.assertEquals(4, estimator.rows);
        Assert.assertEquals(2, estimator.cols);
        Assert.assertEquals(0, estimator.costEvaluations);
        Assert.assertEquals(0, estimator.jacobianEvaluations);
        Assert.assertArrayEquals(new double[4 * 2], estimator.jacobian);
        Assert.assertArrayEquals(new double[4], estimator.residuals);
        Assert.assertTrue(Double.isNaN(estimator.cost));
    }

    /**
     * Test RMS value computation.
     * @throws EstimationException if initialization fails
     */
    @Test
    public void testGetRMS() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0, 0.1),
                    new WeightedMeasurement(2.0, 2.0, 0.1),
                    new WeightedMeasurement(3.0, 3.0, 0.1),
                    new WeightedMeasurement(4.0, 4.0, 0.1)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                    new EstimatedParameter("p1", 0.0),
                    new EstimatedParameter("p2", 0.0)
                };
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        double rms = estimator.getRMS(problem);
        Assert.assertEquals(2.44948974278317, rms, 0.001);
    }

    /**
     * Test Chi-Square value computation.
     * @throws EstimationException if initialization fails
     */
    @Test
    public void testGetChiSquare() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0, 0.1),
                    new WeightedMeasurement(2.0, 2.0, 0.1),
                    new WeightedMeasurement(3.0, 3.0, 0.1),
                    new WeightedMeasurement(4.0, 4.0, 0.1)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                    new EstimatedParameter("p1", 0.0),
                    new EstimatedParameter("p2", 0.0)
                };
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        double chiSquare = estimator.getChiSquare(problem);
        Assert.assertEquals(7.81183235294118, chiSquare, 0.001);
    }

    /**
     * Test covariances computation.
     * @throws EstimationException if initialization fails
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0, 0.1),
                    new WeightedMeasurement(2.0, 2.0, 0.1),
                    new WeightedMeasurement(3.0, 3.0, 0.1),
                    new WeightedMeasurement(4.0, 4.0, 0.1)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                    new EstimatedParameter("p1", 0.0),
                    new EstimatedParameter("p2", 0.0)
                };
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);
        ArrayRealVector expectedCovariances = new ArrayRealVector(new double[] { 0.0349461941241924, 0.0349461941241924, 0.0, 0.0, 0.0349461941241924, 0.0349461941241924 });

        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 0.0349461941241924, 0.0349461941241924 }, { 0.0349461941241924, 0.0349461941241924 }, { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0349461941241924, 0.0349461941241924 }, { 0.0349461941241924, 0.0349461941241924 } });

        SimpleRegression regression = new SimpleRegression();

        RealVector x = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0, 4.0 });
        RealVector y = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0, 4.0 });
        regression.calculateParameters(x, y);
        RealMatrix xx = new Array2DRowRealMatrix(new double[][] { { 1.0, 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0, 1.0 } });

        RealMatrix expectedCovarianceMatrixWithDerivedValues = new Array2DRowRealMatrix(new double[][] { { regression.getParameterEstimate(0), regression.getParameterEstimate(0), 0.0, 0.0 }, { regression.getParameterEstimate(0), regression.getParameterEstimate(0), 0.0, 0.0 }, { 0.0, 0.0, 1.0, 1.0 }, { 0.0, 0.0, 1.0, 1.0 } });

        Assert.assertEquals(expectedCovarianceMatrixWithDerivedValues, covarianceMatrix);
    }

    /**
     * Test guessing errors in parameters.
     * @throws EstimationException if initialization fails
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 1.0, 0.1),
                    new WeightedMeasurement(2.0, 2.0, 0.1),
                    new WeightedMeasurement(3.0, 3.0, 0.1),
                    new WeightedMeasurement(4.0, 4.0, 0.1)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                    new EstimatedParameter("p1", 0.0),
                    new EstimatedParameter("p2", 0.0)
                };
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[] { 1.04381575116321, 1.04381575116321 };

        Assert.assertArrayEquals(expectedErrors, errors);
    }

}