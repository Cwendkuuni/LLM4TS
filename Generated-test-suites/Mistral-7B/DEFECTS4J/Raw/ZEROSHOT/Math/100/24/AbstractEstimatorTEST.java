package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservedValues;
import org.apache.commons.math3.stat.regression.WeightedRegression;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /** Estimation problem. */
    private EstimationProblem problem;

    /** Estimator under test. */
    private AbstractEstimator estimator;

    /** Result of the estimation. */
    private double[] result;

    /** Covariance matrix of the estimation. */
    private RealMatrix covarianceMatrix;

    /**
     * Sets up the test.
     * @throws Exception if an error occurs during setup
     */
    @Before
    public void setUp() throws Exception {
        problem = new EstimationProblem() {

            @Override
            public double[] getAllParameters() {
                return new double[] { 1, 2 };
            }

            @Override
            public WeightedObservedValues getMeasurements() {
                return new WeightedObservedValues(new double[][] { { 1.1, 1.2 },
                                                                  { 1.5, 1.8 },
                                                                  { 1.6, 1.9 },
                                                                  { 1.7, 2.0 } },
                                                          new double[] { 1.0, 1.0, 1.0, 1.0 });
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParametersDimension() {
                return 2;
            }

        };
        estimator = new AbstractEstimator() {

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

        };
        estimator.setMaxCostEval(1000);
    }

    /**
     * Tears down the test.
     * @throws Exception if an error occurs during teardown
     */
    @After
    public void tearDown() throws Exception {
        // empty implementation
    }

    /**
     * Test the getRMS method.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetRMS() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        double rms = estimator.getRMS(problem);
        double expected = Math.sqrt(problem.getChiSquare(problem) / problem.getMeasurements().length);
        assertEquals(expected, rms, 0.001);
    }

    /**
     * Test the getChiSquare method.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetChiSquare() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        double chiSquare = estimator.getChiSquare(problem);
        double expected = problem.getChiSquare(problem);
        assertEquals(expected, chiSquare, 0.001);
    }

    /**
     * Test the getCovariances method.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetCovariances() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        covarianceMatrix = estimator.getCovariances(problem);
        RealMatrix referenceCovarianceMatrix = problem.getCovarianceMatrix();
        assertEquals(referenceCovarianceMatrix.getRowDimension(), covarianceMatrix.getRowDimension());
        assertEquals(referenceCovarianceMatrix.getColumnDimension(), covarianceMatrix.getColumnDimension());
        assertArrayEquals(referenceCovarianceMatrix.getData(), covarianceMatrix.getData(), 0.001);
    }

    /**
     * Test the guessParametersErrors method.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGuessParametersErrors() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        covarianceMatrix = estimator.getCovariances(problem);
        result = estimator.guessParametersErrors(problem);
        RealVector expected = problem.getStandardDeviations();
        assertArrayEquals(expected.toArray(), result, 0.001);
    }

    /**
     * Test the estimate method (without solver).
     * @throws Exception if an error occurs during the test
     */
    @Test(expected = EstimationException.class)
    public void testEstimateWithoutSolver() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        fail("EstimationException expected but not thrown.");
    }

    /**
     * Test the estimate method (with solver).
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testEstimateWithSolver() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] result = problem.getAllParameters();
        double[] expected = new double[] { 1.45345, 0.54678 };
        assertArrayEquals(expected, result, 0.001);
    }

}