import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

/**
 * Test for {@link AbstractEstimator} class.
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)} with a simple problem.
     */
    @Test
    public void testEstimateWithSimpleProblem() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1, 2, 1, 1),
                        new WeightedMeasurement(2, 3, 3, 2),
                        new WeightedMeasurement(3, 4, 5, 3)
                };
            }

            @Override
            public RealVector getBounds() {
                return null;
            }

            @Override
            public double[] getInitialGuess() {
                return new double[] { 2.5 };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 2.5 };
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                // no need to call super as residuals and cost are already set during initialization
            }
        };

        estimator.estimate(problem);

        double expected = 2.516535134843199;
        Assert.assertArrayEquals(new double[] { expected }, problem.getAllParameters(), 0.0001);
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)} with a simple problem.
     */
    @Test
    public void testCovariancesWithSimpleProblem() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1, 2, 1, 1),
                        new WeightedMeasurement(2, 3, 3, 2),
                        new WeightedMeasurement(3, 4, 5, 3)
                };
            }

            @Override
            public RealVector getBounds() {
                return null;
            }

            @Override
            public double[] getInitialGuess() {
                return new double[] { 2.5 };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 2.5 };
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            protected void updateJacobian() {
                // no need to call super as jacobian is already set during initialization
            }

            @Override
            protected double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                // no need to call super as covariances are already set during initialization
                RealMatrix covarianceMatrix = new RealMatrixImpl(new double[][] {
                        { 0.1, 0 },
                        { 0, 0.2 }
                });
                return covarianceMatrix.getData();
            }
        };

        estimator.estimate(problem);

        double[] expectedCovariances = new double[][] { { 0.1, 0 }, { 0, 0.2 } };
        Assert.assertArrayEquals(expectedCovariances, estimator.getCovariances(problem), 0.001);
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)} with a simple problem.
     */
    @Test
    public void testGuessParametersErrorsWithSimpleProblem() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1, 2, 1, 1),
                        new WeightedMeasurement(2, 3, 3, 2),
                        new WeightedMeasurement(3, 4, 5, 3)
                };
            }

            @Override
            public RealVector getBounds() {
                return null;
            }

            @Override
            public double[] getInitialGuess() {
                return new double[] { 2.5 };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 2.5 };
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }
        };

        try {
            estimator.guessParametersErrors(problem);
        } catch (EstimationException e) {
            // expected as getCovariances is not implemented yet
        }

        double expected = 0.1653513484319926;
        Assert.assertEquals(expected, estimator.guessParametersErrors(problem)[0], 0.0001);
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)} with a singular problem.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateWithSingularProblem() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1, 2, 1, 1),
                        new WeightedMeasurement(1, 2, 1, 1),
                        new WeightedMeasurement(1, 2, 1, 1)
                };
            }

            @Override
            public RealVector getBounds() {
                return null;
            }

            @Override
            public double[] getInitialGuess() {
                return new double[] { 1.0, 1.0 };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };

        AbstractEstimator estimator = new AbstractEstimator();
        estimator.estimate(problem);
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)} with a problem having fewer measurements than parameters.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateWithFewerMeasurementsThanParameters() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 3;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1, 2, 1, 1),
                        new WeightedMeasurement(1, 2, 1, 1)
                };
            }

            @Override
            public RealVector getBounds() {
                return null;
            }

            @Override
            public double[] getInitialGuess() {
                return new double[] { 1.0, 1.0, 1.0 };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 1.0, 1.0, 1.0 };
            }
        };

        AbstractEstimator estimator = new AbstractEstimator();
        estimator.estimate(problem);
    }
}