package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.AllowedSolution;
import org.apache.commons.cli.BaseSecantSolver.Method;

public class BaseSecantSolverTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverNullFunction() {
        new BaseSecantSolver(0.01, 0.001, Method.PEGASUS).solve(1, null, 0.0, 1.0, 0.5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverInvalidMaxEval() {
        UnivariateRealFunction f = x -> x * x - 4.0;
        new BaseSecantSolver(0.01, 0.001, Method.PEGASUS).solve(-1, f, 0.0, 1.0, 0.5);
    }

    @Test(expected = MathInternalError.class)
    public void testSolverUnsupportedMethod() {
        UnivariateRealFunction f = x -> x * x - 4.0;
        new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.UNSUPPORTED, f)
                .solve(1, f, 0.0, 1.0, 0.5);
    }

    @Test
    public void testSolverRegulaFalsi() {
        UnivariateRealFunction f = x -> x * x - 4.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.REGULA_FALSI);
        double root = solver.solve(1, f, 0.0, 1.0, 0.5);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolverIllinois() {
        UnivariateRealFunction f = x -> x * x - 4.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.ILLINOIS);
        double root = solver.solve(1, f, 0.0, 1.0, 0.5);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolverPegasus() {
        UnivariateRealFunction f = x -> x * x - 4.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.PEGASUS);
        double root = solver.solve(1, f, 0.0, 1.0, 0.5);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolverAnySide() {
        UnivariateRealFunction f = x -> FastMath.sin(x) - 0.5;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.PEGASUS);

        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertNotNull(root);
    }

    @Test
    public void testSolverLeftSide() {
        UnivariateRealFunction f = x -> FastMath.sin(x) - 0.5;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.PEGASUS, AllowedSolution.LEFT_SIDE);

        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertLessEquals(root, 0.0);
    }

    @Test
    public void testSolverRightSide() {
        UnivariateRealFunction f = x -> FastMath.sin(x) - 0.5;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.PEGASUS, AllowedSolution.RIGHT_SIDE);

        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertGreaterEquals(root, 0.0);
    }

    @Test
    public void testSolverBelowSide() {
        UnivariateRealFunction f = x -> FastMath.sin(x) - 0.5;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.PEGASUS, AllowedSolution.BELOW_SIDE);

        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertLessEquals(root, 0.0);
    }

    @Test
    public void testSolverAboveSide() {
        UnivariateRealFunction f = x -> FastMath.sin(x) - 0.5;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 0.001, Method.PEGASUS, AllowedSolution.ABOVE_SIDE);

        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertGreaterEquals(root, 0.0);
    }
}