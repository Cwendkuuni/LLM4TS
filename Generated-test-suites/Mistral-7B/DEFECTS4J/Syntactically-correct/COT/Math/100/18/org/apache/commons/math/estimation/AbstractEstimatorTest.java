package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.apache.commons.math3.stat.descript.moment.Mean;
import org.apache.commons.math3.stat.descript.moment.Variance;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.apache.commons.math3.stat.descript.moment.Mean;
import org.apache.commons.math3.stat.descript.moment.Variance;
import org.apache.commons.math3.stat.descript.moment.MeanAbsoluteDeviation;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;

import java.util.Arrays;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /** Estimation problem for testing. */
    private EstimationProblem problem;

    /** Abstract estimator for testing. */
    private AbstractEstimator estimator;

    /**
     * Set up the estimation problem and the estimator for testing.
     * @throws EstimationException if initialization fails
     */
    @Before
    public void setUp() throws EstimationException {
        // Set up the estimation problem
        problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                WeightedMeasurement[] wm = new WeightedMeasurement[3];
                for (int i = 0; i < 3; ++i) {
                    wm[i] = new WeightedMeasurement(i, 1.0, i);
                }
                return wm;
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 0.0, 1.0 };
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[] { 0.0, 1.0 };
            }

            @Override
            public double[] getParameters() {
                return new double[] { 1.0, 2.0 };
            }
        };

        // Initialize the estimator
        estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }
        };

        // Set up the maximal number of cost evaluations to 1
        estimator.setMaxCostEval(1);
    }

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)}.
     */
    @Test
    public void testSetMaxCostEval() {
        assertEquals(Integer.MAX_VALUE, estimator.getMaxCostEval());
        estimator.setMaxCostEval(1);
        assertEquals(1, estimator.getMaxCostEval());
    }

    /**
     * Test method for {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testGetCostEvaluations() {
        assertEquals(0, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#updateJacobian()}.
     * @throws EstimationException if the method throws an exception
     */
    @Test
    public void testUpdateJacobian() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        // The jacobian should be identity
        double[][] expectedJacobian = new double[][] {
                { 1.0, 0.0 },
                { 0.0, 1.0 },
                { 1.0, 0.0 },
                { 0.0, 1.0 },
                { 1.0, 0.0 },
                { 0.0, 1.0 }
        };
        RealMatrix jacobianMatrix = new RealMatrixImpl(expectedJacobian);
        RealMatrix expectedJTJ = jacobianMatrix.transpose().multiply(jacobianMatrix);

        assertEquals(expectedJTJ.getEntry(0, 0), estimator.jacobian[0], 1e-12);
        assertEquals(expectedJTJ.getEntry(0, 1), estimator.jacobian[1], 1e-12);
        assertEquals(expectedJTJ.getEntry(1, 0), estimator.jacobian[3], 1e-12);
        assertEquals(expectedJTJ.getEntry(1, 1), estimator.jacobian[4], 1e-12);

    }

    /**
     * Test method for {@link AbstractEstimator#updateResidualsAndCost()}.
     * @throws EstimationException if the method throws an exception
     */
    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();

        // Check that the residuals and cost are correct
        double[] expectedResiduals = new double[] { -1.0, -0.5, 0.5 };
        Arrays.sort(expectedResiduals);
        Arrays.sort(estimator.residuals);
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedResiduals[i], estimator.residuals[i], 1e-12);
        }
        assertEquals(1.5, estimator.cost, 1e-12);

        // Test that the maximal number of cost evaluations is reached
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        try {
            estimator.updateResidualsAndCost();
            fail("An EstimationException should have been thrown");
        } catch (EstimationException e) {
            // Expected
        }

    }

    /**
     * Test method for {@link AbstractEstimator#getRMS(EstimationProblem)}.
     */
    @Test
    public void testGetRMS() {
        try {
            estimator.estimate(problem);
            double rms = estimator.getRMS(problem);
            assertEquals(1.0, rms, 1e-6);
        } catch (EstimationException e) {
            // Ignore
        }
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     */
    @Test
    public void testgetCovariances() throws EstimationException {
        try {
            estimator.estimate(problem);
            double[][] covariances = estimator.getCovariances(problem);
            RealMatrix covarianceMatrix = new RealMatrixImpl(covariances);
            EigenDecomposition eigDecomp = covarianceMatrix.eigenvalueDecomposition();
            double[] eigenvalues = eigDecomp.getEigenvalues();
            Arrays.sort(eigenvalues);
            double lambdaMin = eigenvalues[0];
            double lambdaMax = eigenvalues[1];

            // Check that the covariance matrix is positive definite
            assertTrue(lambdaMin > 0);
            assertTrue(lambdaMax > 0);

            // Check that the eigenvectors are orthogonal
            RealMatrix U = eigDecomp.getV();
            RealMatrix UTranspose = U.transpose();
            RealMatrix UMultiplyUT = U.multiply(UTranspose);
            assertTrue(Arrays.deepEquals(UMultiplyUT.getData(), MatrixUtils.createIdentityMatrix(2).getData()));

            // Check that the diagonal elements of the inverse of the covariance matrix
            // are the reciprocal of the eigenvalues
            RealMatrix invCovarianceMatrix = covarianceMatrix.inverse();
            double[][] invCovariances = invCovarianceMatrix.getData();
            for (int i = 0; i < 2; ++i) {
                assertEquals(1 / lambdaMin, invCovariances[i][i], 1e-12);
            }

        } catch (EstimationException e) {
            // Ignore
        }
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        try {
            estimator.estimate(problem);
            double[] guessParametersErrors = estimator.guessParametersErrors(problem);
            StandardDeviation stdDev = new StandardDeviation(new Mean(new ArrayRealVector(guessParametersErrors)));
            double error = stdDev.getEstimate();
            assertEquals(Math.sqrt(1.5 / 2), error, 1e-6);

        } catch (EstimationException e) {
            // Ignore
        }
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     */
    @Test(expected = EstimationException.class)
    public void testInitializeEstimate() throws EstimationException {
        try {
            estimator.initializeEstimate(problem);
            fail("An EstimationException should have been thrown");
        } catch (EstimationException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}.
     */
    @Test(expected = EstimationException.class)
    public void testEstimate() throws EstimationException {
        try {
            estimator.estimate(problem);
            fail("An EstimationException should have been thrown");
        } catch (EstimationException e) {
            // Expected
        }
    }
}