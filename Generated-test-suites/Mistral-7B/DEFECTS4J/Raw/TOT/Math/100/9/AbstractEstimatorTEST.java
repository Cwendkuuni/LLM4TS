package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Tests for the {@link AbstractEstimator} class.
 */
public class AbstractEstimatorTest {

    /**
     * Test the {@link AbstractEstimator#setMaxCostEval(int)} and
     * {@link AbstractEstimator#getCostEvaluations()} methods.
     */
    @Test
    public void testCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        estimator.setMaxCostEval(5);
        assertEquals(0, estimator.getCostEvaluations());

        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());

        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        assertEquals(6, estimator.getCostEvaluations());
    }

    /**
     * Test the {@link AbstractEstimator#setMaxCostEval(int)} and
     * {@link AbstractEstimator#getCostEvaluations()} methods with an exception.
     */
    @Test(expected = EstimationException.class)
    public void testCostEvaluationsException() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        estimator.setMaxCostEval(0);
        estimator.updateResidualsAndCost();
    }

    /**
     * Test the {@link AbstractEstimator#updateJacobian()} method.
     */
    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        estimator.initializeEstimate(createEstimationProblem());

        estimator.updateJacobian();
        assertEquals(0, estimator.getJacobianEvaluations());

        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        assertEquals(3, estimator.getJacobianEvaluations());
    }

    /**
     * Test the {@link AbstractEstimator#updateResidualsAndCost()} method.
     */
    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        estimator.initializeEstimate(createEstimationProblem());

        estimator.updateResidualsAndCost();
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getResiduals().length);
        assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 0.001);

        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        assertArrayNotSame(new double[0], estimator.getResiduals());
        assertEquals(-1.0, estimator.getCost(), 0.001);

        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
        assertArrayNotSame(new double[0], estimator.getResiduals());
        assertEquals(-1.5, estimator.getCost(), 0.001);
    }

    /**
     * Test the {@link AbstractEstimator#getRMS(EstimationProblem)} method.
     */
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        EstimationProblem problem = createEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();

        double rms = estimator.getRMS(problem);
        assertEquals(3.0, rms, 0.001);
    }

    /**
     * Test the {@link AbstractEstimator#getChiSquare(EstimationProblem)} method.
     */
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        EstimationProblem problem = createEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();

        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(9.0, chiSquare, 0.001);
    }

    /**
     * Test the {@link AbstractEstimator#getCovariances(EstimationProblem)} method.
     */
    @Test
    public void testGetCovariances() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        EstimationProblem problem = createEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();

        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix covarianceMatrix = new SimpleRegression().covariance(problem.getMeasurements());

        assertArrayEquals(covarianceMatrix.getData(), covariances, 0.001);
    }

    /**
     * Test the {@link AbstractEstimator#guessParametersErrors(EstimationProblem)} method.
     */
    @Test
    public void testGuessParametersErrors() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        EstimationProblem problem = createEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();

        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[] {1.0, 1.4142135625, 1.4142135625};

        assertArrayEquals(expectedErrors, errors, 0.001);
    }

    /**
     * Test the {@link AbstractEstimator#initializeEstimate(EstimationProblem)} method.
     */
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        EstimationProblem problem = createEstimationProblem();

        estimator.initializeEstimate(problem);

        assertArrayNotSame(new double[0], estimator.getMeasurements());
        assertArrayNotSame(new double[0], estimator.getParameters());
        assertEquals(3, estimator.getRows());
        assertEquals(2, estimator.getCols());
        assertArrayNotSame(new double[0], estimator.getJacobian());
        assertArrayNotSame(new double[0], estimator.getResiduals());
        assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 0.001);
    }

    /**
     * Test the {@link AbstractEstimator#estimate(EstimationProblem)} method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("estimate should not be called in this test");
            }
        };
        EstimationProblem problem = createEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    /**
     * Creates a simple estimation problem for testing.
     *
     * @return the estimation problem
     */
    private EstimationProblem createEstimationProblem() {
        double[][] measurementsData = new double[][] {{1.0, 2.0}, {2.0, 4.0}, {3.0, 6.0}};
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                return new SimpleRegression().getParameterVector(measurementsData);
            }

            @Override
            public double[] getAllParameters() {
                return new double[] {1.0, 1.0};
            }
        };
        return problem;
    }

}