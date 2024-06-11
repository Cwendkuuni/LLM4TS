package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;

/**
 * Test class for AbstractEstimator
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}
     * with valid measurements and no errors.
     * @throws Exception if the test fails
     */
    @Test
    public void testEstimationNoErrors() throws Exception {
        EstimationProblem problem = createProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // implementation of the estimation process
                // assuming that it correctly estimates the parameters
                // without any errors
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        RealVector expectedParameters = new ArrayRealVector(new double[] { 1.0, 2.0 });
        RealVector actualParameters = problem.getAllParameters();
        Assert.assertArrayEquals(expectedParameters.toArray(), actualParameters.toArray(), 0.001);
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}
     * with invalid measurements (singular problem).
     * @throws Exception if the test fails
     */
    @Test(expected = EstimationException.class)
    public void testSingularProblem() throws Exception {
        EstimationProblem problem = createSingularProblem();
        AbstractEstimator estimator = new AbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}
     * with maximum number of cost evaluations reached.
     * @throws Exception if the test fails
     */
    @Test(expected = EstimationException.class)
    public void testMaxCostEvalReached() throws Exception {
        EstimationProblem problem = createProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                for (int i = 0; i < 100; i++) {
                    estimator.updateResidualsAndCost();
                }
            }
        };
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)}
     * with valid measurements and no errors.
     * @throws Exception if the test fails
     */
    @Test
    public void testCovariancesNoErrors() throws Exception {
        EstimationProblem problem = createProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // implementation of the estimation process
                // assuming that it correctly estimates the parameters
                // without any errors
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        RealMatrix expectedCovariances = RealMatrixUtils.eye(2);
        RealMatrix actualCovariances = estimator.getCovariances(problem);
        Assert.assertEquals(expectedCovariances, actualCovariances);
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}
     * with valid measurements and no errors.
     * @throws Exception if the test fails
     */
    @Test
    public void testGuessParametersErrorsNoErrors() throws Exception {
        EstimationProblem problem = createProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // implementation of the estimation process
                // assuming that it correctly estimates the parameters
                // without any errors
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        double[] expectedErrors = new double[] { 0.5, 0.5 };
        double[] actualErrors = estimator.guessParametersErrors(problem);
        Assert.assertArrayEquals(expectedErrors, actualErrors, 0.001);
    }

    /**
     * Create an estimation problem with valid measurements.
     * @return the estimation problem
     */
    private EstimationProblem createProblem() {
        double[][] x = { { 1.0, 1.0 }, { 2.0, 2.0 }, { 3.0, 3.0 } };
        double[] y = { 3.0, 5.0, 7.0 };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public double[][] getMeasurements() {
                return new double[][] { { x[0][0], x[0][1] },
                                        { x[1][0], x[1][1] },
                                        { x[2][0], x[2][1] } };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };
        return problem;
    }

    /**
     * Create an estimation problem with singular measurements.
     * @return the estimation problem
     */
    private EstimationProblem createSingularProblem() {
        double[][] x = { { 1.0, 1.0 }, { 1.0, 1.0 }, { 0.0, 0.0 } };
        double[] y = { 3.0, 5.0, 0.0 };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public double[][] getMeasurements() {
                return new double[][] { { x[0][0], x[0][1] },
                                        { x[1][0], x[1][1] },
                                        { x[2][0], x[2][1] } };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1.0, 1.0 };
            }
        };
        return problem;
    }

}