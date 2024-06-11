package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.regression.WeightedLeastSquares;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {
    private AbstractEstimator estimator;
    private EstimationProblem problem;

    /**
     * Set up the test case.
     * @throws Exception if initialization fails
     */
    @Before
    public void setUp() throws Exception {
        estimator = new LevenbergMarquardtEstimator();
        problem = new EstimationProblem() {
            @Override
            public int getMeasurementDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return 1;
            }

            @Override
            public RealMatrix getMeasurementMatrix() {
                return RealMatrixUtils.identityMatrix(2);
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[][]{{1.0, 1.5}, {2.0, 2.5}});
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(new double[]{0.0});
            }

            @Override
            public RealVector getWeights() {
                return new ArrayRealVector(new double[]{1.0, 1.0});
            }
        };

        estimator.setMaxCostEval(10);
    }

    /**
     * Test initialization.
     * @throws Exception if initialization fails
     */
    @Test
    public void testInitialization() throws Exception {
        estimator.initializeEstimate(problem);
        WeightedMeasurement[] measurements = problem.getMeasurements();
        EstimatedParameter[] parameters = problem.getUnboundParameters();
        assertEquals(2, measurements.length);
        assertEquals(1, parameters.length);
        assertEquals(2, measurements[0].getDimension());
        assertEquals(1, parameters[0].getDimension());
        assertEquals(2, measurements[0].getWeight());
        assertEquals(1, parameters[0].getParameterIndex());
    }

    /**
     * Test RMS value.
     * @throws Exception if initialization or estimation fails
     */
    @Test
    public void testGetRMS() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        SimpleRegression regression = new SimpleRegression();
        RealVector predicted = regression.estimateRegressionParameters(problem.getMeasurements())
                .getParameterVector();
        double expected = Math.sqrt(predicted.getEntry(0) * (2.0 / 3.0));
        assertEquals(expected, rms, 0.001);
    }

    /**
     * Test Chi-Square value.
     * @throws Exception if initialization or estimation fails
     */
    @Test
    public void testGetChiSquare() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        SimpleRegression regression = new SimpleRegression();
        RealVector predicted = regression.estimateRegressionParameters(problem.getMeasurements())
                .getParameterVector();
        double expected = 5.0 / 3.0;
        assertEquals(expected, chiSquare, 0.001);
    }

    /**
     * Test covariance matrix.
     * @throws Exception if initialization or estimation fails
     */
    @Test
    public void testGetCovariances() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        WeightedLeastSquares wls = new WeightedLeastSquares();
        RealVector predicted = wls.estimateRegressionParameters(problem.getMeasurements())
                .getParameterCovarianceMatrix().getColumnVector(0);
        RealMatrix expectedCovariance = RealMatrixUtils.createIdentityMatrix(1);
        expectedCovariance. scaling(1.0 / 3.0);
        assertEquals(expectedCovariance.getEntry(0, 0), covariances[0][0], 0.001);
        assertEquals(expectedCovariance.getEntry(0, 0), covariances[0][0], 0.001);
    }

    /**
     * Test guessing parameters error.
     * @throws Exception if initialization or estimation fails
     */
    @Test
    public void testGuessParametersErrors() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        double expected = 0.5;
        assertEquals(expected, errors[0], 0.001);
    }
}