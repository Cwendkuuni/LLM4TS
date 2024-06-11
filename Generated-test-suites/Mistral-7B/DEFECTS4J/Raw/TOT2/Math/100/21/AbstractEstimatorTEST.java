package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(createTestProblem());
        estimator.updateJacobian();
        RealMatrix jacobian = estimator.getJacobian();
        assertEquals(6, jacobian.getRowDimension());
        assertEquals(3, jacobian.getColumnDimension());
        RealVector v = jacobian.getColumnVector(0);
        assertEquals(0.75, v.getEntry(0), 1e-8);
        assertEquals(1.25, v.getEntry(1), 1e-8);
        assertEquals(-0.25, v.getEntry(2), 1e-8);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(createTestProblem());
        estimator.updateResidualsAndCost();
        assertEquals(3.2, estimator.getCost(), 1e-5);
        RealVector residuals = estimator.getResiduals();
        assertEquals(0.5, residuals.getEntry(0), 1e-5);
        assertEquals(-0.1, residuals.getEntry(1), 1e-5);
        assertEquals(1.2, residuals.getEntry(2), 1e-5);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new TestEstimator();
        EstimationProblem problem = createTestProblem();
        assertEquals(1.2, estimator.getRMS(problem), 1e-5);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new TestEstimator();
        EstimationProblem problem = createTestProblem();
        assertEquals(1.5, estimator.getChiSquare(problem), 1e-5);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new TestEstimator();
        EstimationProblem problem = createTestProblem();
        RealMatrix cov = estimator.getCovariances(problem);
        assertEquals(3, cov.getRowDimension());
        assertEquals(3, cov.getColumnDimension());
        SingularValueDecomposition svd = new SingularValueDecomposition(cov);
        assertEquals(1.0 / Math.sqrt(3), svd.getSvetta()[0], 1e-5);
        assertEquals(1.0 / Math.sqrt(3), svd.getSvetta()[1], 1e-5);
        assertEquals(1.0 / Math.sqrt(3), svd.getSvetta()[2], 1e-5);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new TestEstimator();
        EstimationProblem problem = createTestProblem();
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(1.0 / Math.sqrt(3), errors[0], 1e-5);
        assertEquals(1.0 / Math.sqrt(3), errors[1], 1e-5);
        assertEquals(1.0 / Math.sqrt(3), errors[2], 1e-5);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeMaxCostEval() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.setMaxCostEval(-1);
        EstimationProblem problem = createTestProblem();
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new TestEstimator();
        EstimationProblem problem = new SingularProblem();
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createTestProblem() {
        WeightedMeasurements measurements = new WeightedMeasurements(new double[][]{{1.0, 2.0},
                                                                                       {2.5, 3.0},
                                                                                       {3.5, 4.0}},
                                                                        new double[]{1.0, 1.0, 1.0});
        EstimationProblem problem = new EstimationProblem(new TestFunction(), measurements);
        return problem;
    }

    private static class TestFunction implements Function {
        @Override
        public double value(double[] x) {
            double sum = 0.0;
            for (int i = 0; i < x.length; ++i) {
                sum += x[i] * x[i] + 1.0;
            }
            return sum;
        }

        @Override
        public double[] gradient(double[] x) {
            double[] grad = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                grad[i] = 2.0 * x[i] + 1.0;
            }
            return grad;
        }
    }

    private static class TestEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            initializeEstimate(problem);
            for (int i = 0; i < 100; ++i) {
                double[] xOld = parameters;
                update(problem);
                assertEquals(StatUtils.distance(xOld, parameters), 0.1, 1e-5);
            }
        }
    }

    private static class SingularProblem implements EstimationProblem {
        @Override
        public RealVector getAllParameters() {
            return new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0},
                    {1.0, 1.0}}).getColumnVector(0);
        }

        @Override
        public RealVector[] getMeasurements() {
            return new RealVector[]{new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0},
                    {1.0, 1.0}}).getColumnVector(0),
                    new Array2DRowRealMatrix(new double[][]{
                            {1.0, 1.0},
                            {1.0, 1.0},
                            {1.0, 1.0}}).getColumnVector(1),
                    new Array2DRowRealMatrix(new double[][]{
                            {1.0, 1.0},
                            {1.0, 1.0},
                            {1.0, 1.0}}).getColumnVector(2)};
        }

        @Override
        public Function getFunction() {
            return new TestFunction();
        }
    }
}