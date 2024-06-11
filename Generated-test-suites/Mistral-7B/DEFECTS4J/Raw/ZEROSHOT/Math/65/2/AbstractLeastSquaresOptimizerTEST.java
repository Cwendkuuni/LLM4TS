package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in this test.");
                return null;
            }
        };

        // Test initialization
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());
        assertEquals(0.0, optimizer.getCost(), 0.0);
        assertEquals(0, optimizer.getIterations());
        assertEquals(0, optimizer.getJacobianEvaluations());
        assertEquals(0, optimizer.getObjectiveEvaluations());

        // Test updating iterations counter
        try {
            optimizer.incrementIterationsCounter();
            optimizer.incrementIterationsCounter();
            optimizer.incrementIterationsCounter();
            assertEquals(3, optimizer.getIterations());
        } catch (OptimizationException e) {
            fail("incrementIterationsCounter should not throw exception.");
        }

        // Test updating jacobian
        try {
            optimizer.updateJacobian();
            fail("updateJacobian should throw exception.");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        // Test updating cost and residuals
        Double[] target = new Double[]{1.0, 2.0};
        Double[] weights = new Double[]{1.0, 1.0};
        Double[] startPoint = new Double[]{0.0, 0.0};
        Double[] expectedResiduals = new Double[]{1.0, 1.0};
        try {
            optimizer.updateResidualsAndCost();
            RealMatrix jacobian = optimizer.getJacobian();
            assertEquals(expectedResiduals[0], optimizer.getResiduals()[0], 0.0);
            assertEquals(expectedResiduals[1], optimizer.getResiduals()[1], 0.0);
            assertEquals(Math.sqrt(2.0), optimizer.getCost(), 0.0);
            assertEquals(2, jacobian.getRowDimension());
            assertEquals(2, jacobian.getColumnDimension());
            assertTrue(jacobian.isSymmetric());
        } catch (FunctionEvaluationException e) {
            fail("updateResidualsAndCost should not throw exception.");
        }

        // Test updating covariance matrix
        try {
            optimizer.getCovariances();
            fail("getCovariances should throw exception.");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        // Test optimizing polynomial fitting
        UnivariateFunction objective = new PolynomialFunction(2);
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        RealVector data = new Array2DRowRealMatrix(new double[][]{
                {0.0, 1.0},
                {1.0, 1.0},
                {2.0, 2.0},
                {3.0, 3.0},
                {4.0, 4.0}}).transpose().toVector();
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(new LinearConstraint(GoalType.MINIMIZE, 1, new RealVector(data), new RealVector(new double[]{1.0, 1.0})));
        UnivariateOptimizer optimizer2 = new LevenbergMarquardtOptimizer();
        optimizer2.setMaxIterations(100);
        optimizer2.setConvergenceChecker(optimizer.getConvergenceChecker());
        optimizer2.setFunction(objective);
        optimizer2.optimize(constraints, startPoint);
        PointValuePair result = optimizer2.getResult();
        assertEquals(expectedResult.getPoint()[0], result.getPoint()[0], 0.001);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.001);

        // Test optimizing when the number of measurements is less than the number of variables
        RealMatrix data2 = new RealArray2DSample(5, 3, 0.0);
        UnivariateFunction objective2 = new PolynomialFunction(3);
        optimizer2 = new LevenbergMarquardtOptimizer();
        optimizer2.setMaxIterations(100);
        optimizer2.setConvergenceChecker(optimizer.getConvergenceChecker());
        optimizer2.setFunction(objective2);
        try {
            optimizer2.optimize(new LinearConstraintSet(), startPoint);
            fail("optimize should have thrown an optimisation exception.");
        } catch (OptimizationException e) {
            // expected
        }

        // Test optimizing with non-square jacobian
        Double[] startPoint3 = new Double[]{0.0, 0.0, 0.0};
        objective = new PolynomialFunction(2);
        optimizer2 = new LevenbergMarquardtOptimizer();
        optimizer2.setMaxIterations(100);
        optimizer2.setConvergenceChecker(optimizer.getConvergenceChecker());
        optimizer2.setFunction(objective);
        try {
            optimizer2.optimize(new LinearConstraintSet(), startPoint3);
            fail("optimize should have thrown a function evaluation exception.");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        // Test optimizing with non-differentiable function
        optimizer2 = new FibonacciSearchOptimizer();
        optimizer2.setMaxIterations(100);
        optimizer2.setConvergenceChecker(optimizer.getConvergenceChecker());
        optimizer2.setFunction(new AbsFunction());
        try {
            optimizer2.optimize(new LinearConstraintSet(), startPoint);
            fail("optimize should have thrown an illegal argument exception.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class AbsFunction implements ObjectiveFunction {
        @Override
        public double value(RealVector x) {
            double[] xa = x.toArray();
            return Math.abs(xa[0]);
        }

        @Override
        public RealVector gradient(RealVector x) {
            double[] xa = x.toArray();
            return new Array2DRowRealMatrix(new double[][]{new double[]{Math.signum(xa[0])}}).transpose().toVector();
        }

        @Override
        public void checkGradient(RealVector x, RealVector gradient) {
            // not implemented
        }

        @Override
        public RealVector valueGradient(RealVector x) {
            double[] xa = x.toArray();
            return new Array2DRowRealMatrix(new double[][]{new double[]{Math.signum(xa[0])}}).transpose().toVector();
        }

        @Override
        public RealVector gradient(RealVector x, int index) {
            double[] xa = x.toArray();
            return new Array2DRowRealMatrix(new double[][]{new double[]{Math.signum(xa[0])}}).transpose().toVector();
        }

        @Override
        public void checkGradient(RealVector x, RealVector gradient, int index) {
            // not implemented
        }

        @Override
        public RealVector valueHessian(RealVector x) {
            return new Array2DRowRealMatrix(new double[][]{new double[]{Math.signum(1.0)}}).transpose().toVector();
        }

        @Override
        public void checkHessian(RealVector x, RealVector hessian) {
            // not implemented
        }

        @Override
        public RealVector gradient(RealVector x, int... indices) {
            double[] xa = x.toArray();
            return new Array2DRowRealMatrix(new double[][]{new double[]{Math.signum(xa[0])}}).transpose().toVector();
        }
    }

}