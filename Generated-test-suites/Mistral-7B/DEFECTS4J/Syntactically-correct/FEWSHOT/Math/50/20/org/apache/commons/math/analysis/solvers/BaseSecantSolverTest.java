package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.AllowedSolution;
import org.apache.commons.cli.BaseSecantSolver.Method;

public class BaseSecantSolverTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructNullFunction() {
        new BaseSecantSolver(0.01, Method.REGULA_FALSI, null);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructInvalidFunction() {
        UnivariateRealFunction nullFunction = f -> null;
        new BaseSecantSolver(0.01, Method.REGULA_FALSI, nullFunction);
    }

    @Test
    public void testConstructBasic() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI, function);
        assertEquals("The constructor should have set the method to REGULA_FALSI", Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testConstructWithAccuracies() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 1e-8, 1e-6, Method.REGULA_FALSI, function);
        assertEquals("The constructor should have set the method to REGULA_FALSI", Method.REGULA_FALSI, solver.getMethod());
        assertEquals("The constructor should have set the absolute accuracy to 1e-6", 1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals("The constructor should have set the function value accuracy to 1e-8", 1e-8, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testSolveBasicRegulaFalsi() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI, function);
        double result = solver.solve(10, function, -10, 10, 0.0);
        assertEquals("The root should be approximately 2.0", 2.0, result, 0.01);
    }

    @Test
    public void testSolveBasicIllinois() {
        UnivariateRealFunction function = x -> Math.pow(x, 3) - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.ILLINOIS, function);
        double result = solver.solve(10, function, -10, 10, 0.0);
        assertEquals("The root should be approximately -1.0", -1.0, result, 0.01);
    }

    @Test
    public void testSolveBasicPegasus() {
        UnivariateRealFunction function = x -> Math.pow(x, 3) - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.PEGASUS, function);
        double result = solver.solve(10, function, -10, 10, 0.0);
        assertEquals("The root should be approximately -1.0", -1.0, result, 0.01);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveInvalidMethod() {
        UnivariateRealFunction function = x -> Math.pow(x, 3) - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.INVALID, function);
        solver.solve(10, function, -10, 10, 0.0);
    }

    @Test
    public void testSolveMaxEvalReached() {
        UnivariateRealFunction function = x -> Math.pow(x, 3) - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.PEGASUS, function);
        try {
            solver.solve(0, function, -10, 10, 0.0);
            fail("The function should have thrown an exception when maxEval was 0");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolveBeyondBracketing() {
        UnivariateRealFunction function = x -> Math.pow(x, 3) - 1;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.PEGASUS, function);
        try {
            solver.solve(10, function, 0, 1, 0.0);
            fail("The function should have thrown an exception when the initial bounds were not bracketing a root");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolveAllowedSolutionAnySide() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI, function);
        double result = solver.solve(10, function, -10, 10, AllowedSolution.ANY_SIDE);
        assertEquals("The root should be approximately 2.0", 2.0, result, 0.01);
    }

    @Test
    public void testSolveAllowedSolutionLeftSide() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI, function);
        double result = solver.solve(10, function, -10, 10, AllowedSolution.LEFT_SIDE);
        assertEquals("The root should be approximately 2.0", 2.0, result, 0.01);
    }

    @Test
    public void testSolveAllowedSolutionRightSide() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI, function);
        double result = solver.solve(10, function, -10, 10, AllowedSolution.RIGHT_SIDE);
        assertEquals("The root should be approximately 2.0", 2.0, result, 0.01);
    }

    @Test
    public void testSolveAllowedSolutionBelowSide() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI, function);
        double result = solver.solve(10, function, -10, 10, AllowedSolution.BELOW_SIDE);
        assertEquals("The root should be approximately 2.0", 2.0, result, 0.01);
    }

    @Test
    public void testSolveAllowedSolutionAboveSide() {
        UnivariateRealFunction function = x -> x * x - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI, function);
        double result = solver.solve(10, function, -10, 10, AllowedSolution.ABOVE_SIDE);
        assertEquals("The root should be approximately 2.0", 2.0, result, 0.01);
    }

}