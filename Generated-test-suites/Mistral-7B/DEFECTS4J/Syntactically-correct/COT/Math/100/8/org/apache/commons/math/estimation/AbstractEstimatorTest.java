package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservations;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test setMaxCostEval method.
     */
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }
        };

        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test getCostEvaluations method.
     */
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }
        };

        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        assertEquals(3, estimator.getCostEvaluations());
    }

    /**
     * Test getJacobianEvaluations method.
     */
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }

            @Override
            protected void updateJacobian() {
                // dummy implementation
            }
        };

        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        assertEquals(3, estimator.getJacobianEvaluations());
    }

    /**
     * Test updateJacobian method.
     * @throws EstimationException if the number of cost evaluations
     * exceeds the maximum allowed
     */
    @Test(expected = EstimationException.class)
    public void testUpdateJacobianWithMaxCostEval() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }

            @Override
            protected void updateJacobian() throws EstimationException {
                estimator.updateResidualsAndCost();
            }
        };

        estimator.setMaxCostEval(1);
        estimator.updateJacobian();
        estimator.updateJacobian();
    }

    /**
     * Test updateResidualsAndCost method.
     * @throws EstimationException if the number of cost evaluations
     * exceeds the maximum allowed
     */
    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEval() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                estimator.updateJacobian();
            }
        };

        estimator.setMaxCostEval(1);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    /**
     * Test getRMS method.
     */
    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedObservations getObservations() {
                WeightedObservations observations = new WeightedObservations(new double[][] {
                        {1.0, 2.0},
                        {3.0, 4.0}}, new double[] {1.0, 1.0}, new double[] {-1.0, -1.0});
                return observations;
            }

            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public double[] getParameters() {
                return new double[] {1.1, 1.2};
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] {1.1, 1.2};
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }
        };

        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        double rms = estimator.getRMS(problem);
        double expectedRMS = Math.sqrt(0.5);

        assertEquals(expectedRMS, rms, 0.001);
    }

    /**
     * Test getChiSquare method.
     */
    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedObservations getObservations() {
                WeightedObservations observations = new WeightedObservations(new double[][] {
                        {1.0, 2.0},
                        {3.0, 4.0}}, new double[] {1.0, 1.0}, new double[] {-1.0, -1.0});
                return observations;
            }

            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public double[] getParameters() {
                return new double[] {1.1, 1.2};
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] {1.1, 1.2};
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }
        };

        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = 0.5;

        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    /**
     * Test getCovariances method.
     * @throws EstimationException if the covariance matrix
     * cannot be computed or the number of degrees of freedom is not positive
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedObservations getObservations() {
                WeightedObservations observations = new WeightedObservations(new double[][] {
                        {1.0, 2.0},
                        {3.0, 4.0}}, new double[] {1.0, 1.0}, new double[] {-1.0, -1.0});
                return observations;
            }

            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public double[] getParameters() {
                return new double[] {1.1, 1.2};
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] {1.1, 1.2};
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }
        };

        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        RealMatrix covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][] {
                {0.1, 0},
                {0, 0.1}
        });

        RealMatrixAssert.assertEquals(expectedCovariances, covariances, 0.001);
    }

    /**
     * Test guessParametersErrors method.
     * @throws EstimationException if the covariances matrix cannot be computed
     * or the number of degrees of freedom is not positive
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedObservations getObservations() {
                WeightedObservations observations = new WeightedObservations(new double[][] {
                        {1.0, 2.0},
                        {3.0, 4.0}}, new double[] {1.0, 1.0}, new double[] {-1.0, -1.0});
                return observations;
            }

            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public double[] getParameters() {
                return new double[] {1.1, 1.2};
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] {1.1, 1.2};
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // dummy implementation
            }
        };

        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[] {0.1, 0.1};

        assertArrayEquals(expectedErrors, errors, 0.001);
    }

}