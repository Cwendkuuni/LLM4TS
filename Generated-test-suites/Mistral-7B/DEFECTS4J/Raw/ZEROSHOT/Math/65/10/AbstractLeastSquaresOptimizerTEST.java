package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpace;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.GradientDescent;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer.MaxFunctionCalls;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer.MaxIterations;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer(new PolynomialFunctions.QuadraticFunction(2));
        optimizer.setMaxIterations(10);
        optimizer.setMaxFunctionCalls(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-10));
    }

    @Test
    public void testDefaultSettings() {
        AbstractLeastSquaresOptimizer defaultOptimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, defaultOptimizer.getMaxIterations());
        assertEquals(SimpleVectorialValueChecker.class, defaultOptimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testSettersAndGetters() {
        optimizer.setMaxIterations(5);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-5));
        assertEquals(5, optimizer.getMaxIterations());
        assertEquals(SimpleVectorialValueChecker.class, optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testIterationCounter() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
        assertEquals(3, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceededException() throws Exception {
        optimizer.setMaxIterations(1);
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testMaxFunctionCalls() throws Exception {
        optimizer.setMaxFunctionCalls(1);
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();
    }

    @Test(expected = OptimizationException.class)
    public void testMaxFunctionCallsExceededException() throws Exception {
        optimizer.setMaxFunctionCalls(1);
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        optimizer.updateJacobian();
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        RealMatrix jacobian = optimizer.getJacobian();
        assertEquals(2, jacobian.getRowDimension());
        assertEquals(2, jacobian.getColumnDimension());
        assertArrayEquals(new double[] { -2.0, -2.0 }, jacobian.getEntryMatrix().getData(), 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        assertArrayEquals(new double[] { -0.25, -0.25 }, optimizer.getResiduals(), 0.0);
        assertEquals(0.5, optimizer.getCost(), 0.0);
    }

    @Test
    public void testRMS() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        assertEquals(0.5, optimizer.getRMS(), 0.0);
    }

    @Test
    public void testChiSquare() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        assertEquals(1.0, optimizer.getChiSquare(), 0.0);
    }

    @Test
    public void testCovariances() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        RealMatrix covarianceMatrix = optimizer.getCovariances();
        RealVector covarianceVector = covarianceMatrix.getColumnVector(0);
        assertEquals(1.0 / 3.0, covarianceVector.getEntry(0), 0.0);
        assertEquals(1.0 / 3.0, covarianceVector.getEntry(1), 0.0);
        assertEquals(1.0 / 3.0, covarianceVector.getEntry(2), 0.0);
        assertEquals(1.0 / 3.0, covarianceVector.getEntry(3), 0.0);
        assertEquals(0.0, covarianceVector.getEntry(4), 0.0);
        assertEquals(0.0, covarianceVector.getEntry(5), 0.0);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 });
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(Math.sqrt(5.0 / 3.0), errors[0], 0.0);
        assertEquals(Math.sqrt(5.0 / 3.0), errors[1], 0.0);
        assertEquals(Math.sqrt(5.0 / 3.0), errors[2], 0.0);
        assertEquals(Math.sqrt(5.0 / 3.0), errors[3], 0.0);
    }

    @Test
    public void testOptimize() throws Exception {
        optimizer.minimize(new double[] { 0.0, 0.0 }, new double[] { 1.0, 1.0 });
        PointValuePair solution = optimizer.getPointValuePair();
        RealVector x = optimizer.getPoint();
        RealVector fx = optimizer.getObjective();
        RealVector jacobian = optimizer.getJacobian();
        assertEquals(1.0, solution.getValue(), 0.0);
        assertEquals(0.0, x.getEntry(0), 0.0);
        assertEquals(0.0, x.getEntry(1), 0.0);
        assertEquals(-1.0, fx.getEntry(0), 0.0);
        assertEquals(-1.0, fx.getEntry(1), 0.0);
        assertArrayEquals(new double[] { -1.0, -1.0 }, jacobian.getEntryMatrix().getData(), 0.0);
    }

    @Test
    public void testOptimizeWithCustomInitialGuess() throws Exception {
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 1.0, 1.0 }, new InitialGuess() {
            @Override
            public double[] value() {
                return new double[] { 2.0, 2.0 };
            }
        });
        PointValuePair solution = optimizer.getPointValuePair();
        RealVector x = optimizer.getPoint();
        RealVector fx = optimizer.getObjective();
        RealVector jacobian = optimizer.getJacobian();
        assertEquals(0.5, solution.getValue(), 0.0);
        assertEquals(0.5, x.getEntry(0), 0.0);
        assertEquals(0.5, x.getEntry(1), 0.0);
        assertEquals(-0.25, fx.getEntry(0), 0.0);
        assertEquals(-0.25, fx.getEntry(1), 0.0);
        assertArrayEquals(new double[] { -1.0, -1.0 }, jacobian.getEntryMatrix().getData(), 0.0);
    }

    @Test
    public void testOptimizeWithCustomGradientDescent() throws Exception {
        UnconstrainedMinimizer minimizer = new GradientDescent(1.0e-5);
        optimizer = new LevenbergMarquardtOptimizer(minimizer);
        optimizer.setMaxIterations(1);
        optimizer.minimize(new double[] { 1.0, 1.0 }, new double[] { 1.0, 1.0 });
        PointValuePair solution = optimizer.getPointValuePair();
        RealVector x = optimizer.getPoint();
        RealVector fx = optimizer.getObjective();
        RealVector jacobian = optimizer.getJacobian();
        assertEquals(0.5, solution.getValue(), 0.0);
        assertEquals(0.5, x.getEntry(0), 0.0);
        assertEquals(0.5, x.getEntry(1), 0.0);
        assertEquals(-0.25, fx.getEntry(0), 0.0);
        assertEquals(-0.25, fx.getEntry(1), 0.0);
        assertArrayEquals(new double[] { -1.0, -1.0 }, jacobian.getEntryMatrix().getData(), 0.0);
    }

}