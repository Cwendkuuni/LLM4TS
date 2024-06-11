package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

public class AbstractEstimatorTest {

    @SuppressWarnings("unchecked")
    private AbstractEstimator estimator = new AbstractEstimator() {
        protected void estimate(EstimationProblem problem) throws EstimationException {
            // empty method to avoid actual estimation
        }
    };

    @SuppressWarnings("unchecked")
    private EstimationProblem problem = new EstimationProblem() {
        protected WeightedMeasurement[] getMeasurements() {
            return new WeightedMeasurement[] {
                new WeightedMeasurement(1, new ArrayRealVector(new double[] { 1, 1 })),
                new WeightedMeasurement(1, new ArrayRealVector(new double[] { 1, -1 })),
                new WeightedMeasurement(1, new ArrayRealVector(new double[] { -1, 1 }))
            };
        }

        protected EstimatedParameter[] getUnboundParameters() {
            return new EstimatedParameter[] { new EstimatedParameter("p1"), new EstimatedParameter("p2") };
        }
    };

    @org.junit.Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @org.junit.Test
    public void testGetCostEvaluations() {
        estimator.setMaxCostEval(10);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        assertEquals(10, estimator.getCostEvaluations());
    }

    @org.junit.Test
    public void testGetJacobianEvaluations() {
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @org.junit.Test(expected = EstimationException.class)
    public void testEstimationException() throws EstimationException {
        estimator.setMaxCostEval(1);
        estimator.estimate(problem);
    }

    @ org.junit.Test
    public void testGetRMS() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(2.44948974278317, rms, 1e-14);
    }

    @org.junit.Test
    public void testGetChiSquare() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(3.0, chiSquare, 1e-14);
    }

    @org.junit.Test
    public void testGetCovariances() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix jTj = new RealMatrixImpl(problem.getAllParameters().length, problem.getAllParameters().length, covariances);
        RealMatrix expectedJTJ = new RealMatrixImpl(new double[][] { { 0.069737245, 0.029039753 },
                                                                       { 0.029039753, 0.069737245 } });
        RealMatrix jTJ = new LUDecomposition(jTj).getSolver().getSolution();
        RealVector expectedEigenvector = new ArrayRealVector(new double[] { 0.14366675, 0.97630662 });
        RealVector eigenvector = jTJ.operate(new ArrayRealVector(new double[] { 1, 1 }));
        assertEquals(Precision.relativeEquals(expectedEigenvector.getEntry(0), eigenvector.getEntry(0), 1e-14), true);
        assertEquals(Precision.relativeEquals(expectedEigenvector.getEntry(1), eigenvector.getEntry(1), 1e-14), true);
    }

    @org.junit.Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[] { 0.14366675, 0.97630662 }, errors, 1e-14);
    }

}