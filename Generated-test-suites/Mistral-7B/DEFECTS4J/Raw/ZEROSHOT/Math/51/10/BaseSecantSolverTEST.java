package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS);
        function = f -> Math.pow(Math.E, -x) - x;
    }

    @Test
    public void testSolver_MinMax() {
        double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, root, 1.0e-8);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolver_NoBracketing() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS);
        double[] roots = {0.0, 3.0};
        UnivariateRealFunction function = x -> (x - roots[0]) * (x - roots[1]);
        solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_MaxEvalExceeded() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = x -> 1 / (Math.sqrt(Math.abs(x)) + 0.001);
        solver.solve(1, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolver_AllowedSolution() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
        Assert.assertLessEqual(root, 0.0);
    }

    @Test
    public void testSolver_AbsoluteAccuracy() {
        solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, root, 1.0e-9);
    }

    @Test
    public void testSolver_RelativeAccuracy() {
        solver = new BaseSecantSolver(1.0e-7, 1.0e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        double relError = Precision.abs(root - 0.0) / Precision.abs(0.0);
        Assert.assertLessThan(1.0e-7, relError);
    }

    @Test
    public void testSolver_FunctionValueAccuracy() {
        solver = new BaseSecantSolver(1.0e-6, 1.0e-6, 1.0e-8, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        double fAbsError = Precision.abs(computeObjectiveValue(root) - 0.0);
        Assert.assertLessThan(1.0e-8, fAbsError);
    }
}