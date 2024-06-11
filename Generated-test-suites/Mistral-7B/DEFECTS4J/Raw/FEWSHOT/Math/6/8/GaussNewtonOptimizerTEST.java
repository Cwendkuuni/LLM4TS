package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintViolation;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LsqLinearConstraintChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LsqLinearObjectiveFunction;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    private static final double EPSILON = 1e-13;

    @Test
    public void testGaussNewtonOptimizerLUDecomposition() throws Exception {
        GaussNewtonOptimizer opt = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(EPSILON));

        ObjectiveFunction f = new LsqLinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{1.0, 1.0}, new double[]{-1.0, -1.0}, GoalType.MINIMIZE);
        double[] startPoint = new double[]{1.5, 2.5};
        PointValuePair solution = opt.optimize(f, startPoint);
        double[] expectedSolution = new double[]{0.6666666865348816, 0.3333333134651194};
        assertArrayEquals(expectedSolution, solution.getPoint(), EPSILON);
        assertEquals(-1.0493259598021663, solution.getValue(), EPSILON);
    }

    @Test
    public void testGaussNewtonOptimizerQRDecomposition() throws Exception {
        GaussNewtonOptimizer opt = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(EPSILON));

        ObjectiveFunction f = new LsqLinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{1.0, 1.0}, new double[]{-1.0, -1.0}, GoalType.MINIMIZE);
        double[] startPoint = new double[]{1.5, 2.5};
        PointValuePair solution = opt.optimize(f, startPoint);
        double[] expectedSolution = new double[]{0.6666666865348816, 0.3333333134651194};
        assertArrayEquals(expectedSolution, solution.getPoint(), EPSILON);
        assertEquals(-1.0493259598021663, solution.getValue(), EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullConvergenceChecker() {
        GaussNewtonOptimizer opt = new GaussNewtonOptimizer(true);
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerDivergence() throws Exception {
        GaussNewtonOptimizer opt = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(EPSILON * 100));

        ObjectiveFunction f = new LsqLinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{1.0, 1.0}, new double[]{-1.0, -1.0}, GoalType.MINIMIZE);
        double[] startPoint = new double[]{1.5, 2.5};
        PointValuePair solution = opt.optimize(f, startPoint);
        fail("Convergence should not have been reached.");
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerSingularMatrixException() throws Exception {
        GaussNewtonOptimizer opt = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(EPSILON));

        ObjectiveFunction f = new LsqLinearObjectiveFunction(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, new double[]{1.0, 1.0}, new double[]{-1.0, -1.0}, GoalType.MINIMIZE);
        double[] startPoint = new double[]{1.5, 2.5};
        try {
            opt.optimize(f, startPoint);
        } catch (ConvergenceException e) {
            // Expected exception.
        }

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[][]{{-1.0, 1.0}}, 0.0, null, null, new LsqLinearConstraintChecker()));
        ObjectiveFunction constraintFunction = new LsqLinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{1.0, 1.0}, new double[]{-1.0, -1.0}, GoalType.MINIMIZE, constraints);
        double[] startPointWithConstraint = new double[]{1.5, 2.5};
        try {
            opt.optimize(constraintFunction, startPointWithConstraint);
            fail("An exception should have been thrown.");
        } catch (SingularMatrixException e) {
            // Expected exception.
        }
    }

    @Test(expected = MathInternalError.class)
    public void testGaussNewtonOptimizerMathInternalError() throws Exception {
        GaussNewtonOptimizer opt = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(EPSILON));

        ObjectiveFunction f = new LsqLinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{1.0, 1.0}, new double[]{-1.0, -1.0}, GoalType.MINIMIZE);
        double[] startPoint = new double[]{1.5, 2.5};
        try {
            opt.optimize(f, startPoint);
            fail("An exception should have been thrown.");
        } catch (MathInternalError e) {
            // Expected exception.
        }
    }
}