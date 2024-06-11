package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /** The estimation problem to use for testing. */
    private static final EstimationProblem PROBLEM = new EstimationProblem() {
        @Override
        public RealVector getMeasurements() {
            return new ArrayRealVector(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
        }

        @Override
        public RealVector getUnboundParameters() {
            return new ArrayRealVector(new double[] { 1.0, 1.0 });
        }

        @Override
        public RealVector getAllParameters() {
            return new ArrayRealVector(new double[] { 1.0, 1.0, 1.0 });
        }
    };

    /** The AbstractEstimator under test. */
    private AbstractEstimator estimator;

    @Before
    public void setUp() throws Exception {
        estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(PROBLEM);
    }

    /**
     * Test {@link AbstractEstimator#setMaxCostEval(int)}.
     */
    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testGetCostEvaluations() {
        assertEquals(0, estimator.getCostEvaluations());
    }

    /**
     * Test {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    /**
     * Test {@link AbstractEstimator#updateJacobian()}.
     */
    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        assertEquals(5, estimator.getJacobianEvaluations());
    }

    /**
     * Test {@link AbstractEstimator#updateResidualsAndCost()}.
     */
    @Test
    public void testUpdateResidualsAndCost() {
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(1, estimator.getJacobianEvaluations());
        assertEquals(Math.sqrt(30.0), estimator.cost, 1e-15);
    }

    /**
     * Test {@link AbstractEstimator#getRMS(EstimationProblem)}.
     */
    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(PROBLEM);
        assertEquals(Math.sqrt(30.0 / 5.0), rms, 1e-15);
    }

    /**
     * Test {@link AbstractEstimator#getChiSquare(EstimationProblem)}.
     */
    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(PROBLEM);
        assertEquals(30.0, chiSquare, 1e-15);
    }

    /**
     * Test {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     */
    @Test
    public void testGetCovariances() throws Exception {
        double[][] covarianceMatrix = estimator.getCovariances(PROBLEM);
        SingularValueDecomposition svd = new SingularValueDecomposition(new RealMatrixImpl(covarianceMatrix));
        double[] singularValues = svd.getSingularValues();
       assertEquals(Math.sqrt(1.0 / 2.0), singularValues[0], 1e-15);
        assertEquals(Math.sqrt(1.0 / 2.0), singularValues[1], 1e-15);
    }

    /**
     * Test {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() throws Exception {
        double[] errors = estimator.guessParametersErrors(PROBLEM);
        assertEquals(Math.sqrt(15.0 / 4.0), errors[0], 1e-15);
        assertEquals(Math.sqrt(15.0 / 4.0), errors[1], 1e-15);
    }

    /**
     * Test {@link AbstractEstimator#estimate(EstimationProblem)} with a successful estimation.
     */
    @Test
    public void testEstimateSuccess() throws Exception {
        estimator.estimate(PROBLEM);
        RealVector estimate = PROBLEM.getAllParameters();
        RealVector actualEstimate = PROBLEM.getAllParameters();
        assertEquals(estimate, actualEstimate);
    }

    /**
     * Test {@link AbstractEstimator#estimate(EstimationProblem)} with a failure due to the number of cost evaluations
     * exceeding the limit.
     */
    @Test
    public void testEstimateFailureCostEvaluations() throws Exception {
        estimator.setMaxCostEval(1);
        assertThrows(EstimationException.class, () -> estimator.estimate(PROBLEM));
    }

    /**
     * Test {@link AbstractEstimator#estimate(EstimationProblem)} with a failure due to the jacobian being singular.
     */
    @Test
    public void testEstimateFailureSingularJacobian() throws Exception {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
            }

            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0 });
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0, 1.0 });
            }
        };
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.setMaxCostEval(5);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }
}

class MyAbstractEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        throw new UnsupportedOperationException("This is a test case, do not call this method!");
    }

}