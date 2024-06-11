package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtilities;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test for the {@link AbstractEstimator#setMaxCostEval(int)} and
     * {@link AbstractEstimator#getCostEvaluations()} methods.
     */
    @Test
    public void testMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        estimator.setMaxCostEval(10);
        assertEquals(0, estimator.getCostEvaluations());

        estimator.estimate(createSimpleRegressionProblem());
        assertEquals(10, estimator.getCostEvaluations());
    }

    /**
     * Test for the {@link AbstractEstimator#getJacobianEvaluations()} method.
     */
    @Test
    public void testJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        estimator.estimate(createSimpleRegressionProblem());
        assertEquals(1, estimator.getJacobianEvaluations());

        estimator.updateJacobian();
        estimator.estimate(createSimpleRegressionProblem());
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    /**
     * Test for the {@link AbstractEstimator#getRMS(EstimationProblem)} and
     * {@link AbstractEstimator#getChiSquare(EstimationProblem)} methods.
     */
    @Test
    public void testRMSAndChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = createSimpleRegressionProblem();
        estimator.estimate(problem);

        double rms = estimator.getRMS(problem);
        double chiSquare = estimator.getChiSquare(problem);

        SimpleRegression regression = new SimpleRegression();
        regression.estimate(problem.getMeasurements());
        double rmsExpected = regression.estimateResidualStandardError();
        double chiSquareExpected = regression.estimateCovarianceMatrix().getEntry(0, 0);

        assertEquals("RMS: {0} != {1}", rmsExpected, rms, 1e-6);
        assertEquals("Chi-square: {0} != {1}", chiSquareExpected, chiSquare, 1e-12);
    }

    /**
     * Test for the {@link AbstractEstimator#getCovariances(EstimationProblem)}
     * method.
     */
    @Test
    public void testCovariances() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = createSimpleRegressionProblem();
        estimator.estimate(problem);

        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 0.00063275, -0.00033115 },
            { -0.00033115, 0.00155832 }
        });

        assertEquals("Covariances:\n", expected, new RealMatrixImpl(covariances));
    }

    /**
     * Test for the {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}
     * method.
     */
    @Test
    public void testGuessParametersErrors() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = createSimpleRegressionProblem();
        estimator.estimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);
        double[] expected = new double[] { 0.005377, 0.003355 };

        assertEquals("Errors: {0} != {1}", VectorUtilities.format(expected), VectorUtilities.format(errors), 1e-3);
    }

    /**
     * Test for the {@link AbstractEstimator#initializeEstimate(EstimationProblem)}
     * method.
     */
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        EstimationProblem problem = createSimpleRegressionProblem();

        estimator.initializeEstimate(problem);

        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertEquals(new double[] {}, estimator.jacobian);
        assertEquals(new double[] {}, estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
    }

    /**
     * Test for the {@link AbstractEstimator#estimate(EstimationProblem)} method
     * with a problem that can be solved.
     */
    @Test
    public void testEstimateWithSolvableProblem() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();

        EstimationProblem problem = createSimpleRegressionProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        assertEquals(5, estimator.getCostEvaluations());
        assertEquals(5, estimator.getJacobianEvaluations());
        assertArrayEquals(new double[] { 3.0, 2.0 }, estimator.parameters, 1e-6);

        double[] expected = new double[] { 0.005377, 0.003355 };
        double[] errors = estimator.guessParametersErrors(problem);

        assertEquals("Errors: {0} != {1}", VectorUtilities.format(expected), VectorUtilities.format(errors), 1e-3);
    }

    /**
     * Test for the {@link AbstractEstimator#estimate(EstimationProblem)} method
     * with a singular problem (number of measurements equals number of parameters).
     */
    @Test(expected = EstimationException.class)
    public void testEstimateWithSingularProblem() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();

        EstimationProblem problem = createSingularRegressionProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    /**
     * Test for the {@link AbstractEstimator#estimate(EstimationProblem)} method
     * with a problem with negative weights.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeWeights() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();

        EstimationProblem problem = createProblemWithNegativeWeights();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    private static EstimationProblem createSimpleRegressionProblem() {
        double[][] data = {
            { 1.0, 2.0 }, { 2.0, 4.0 }, { 3.0, 6.0 }, { 4.0, 8.0 }, { 5.0, 10.0 }
        };

        return new SimpleRegressionProblem(data);
    }

    private static EstimationProblem createSingularRegressionProblem() {
        double[][] data = {
            { 1.0, 2.0 }, { 2.0, 2.0 }, { 3.0, 3.0 }, { 4.0, 4.0 }, { 5.0, 5.0 }
        };

        return new SimpleRegressionProblem(data);
    }

    private static EstimationProblem createProblemWithNegativeWeights() {
        double[][] data = {
            { 1.0, 2.0, -1.0 }, { 2.0, 4.0, -1.0 }, { 3.0, 6.0, -1.0 }, { 4.0, 8.0, -1.0 }, { 5.0, 10.0, -1.0 }
        };

        return new SimpleRegressionProblem(data);
    }

}