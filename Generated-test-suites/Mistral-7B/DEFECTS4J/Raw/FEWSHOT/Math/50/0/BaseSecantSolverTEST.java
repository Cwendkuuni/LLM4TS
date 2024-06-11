package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorFactory;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.BrentSolver;

public class BaseSecantSolverTest {
    private static final double EPS = 1e-8;

    @Test
    public void testSolver_RegulaFalsi() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3), 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, EPS);
    }

    @Test
    public void testSolver_Illinois() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3), 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, EPS);
    }

    @Test
    public void testSolver_Pegasus() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3), 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, EPS);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_MaxEvalExceeded_RegulaFalsi() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.REGULA_FALSI, 0);
        solver.solve(f, 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_MaxEvalExceeded_Illinois() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.ILLINOIS, 0);
        solver.solve(f, 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_MaxEvalExceeded_Pegasus() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS, 0);
        solver.solve(f, 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = PrecisionException.class)
    public void testSolver_PrecisionException_RegulaFalsi() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(f, 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
        fail("Expected PrecisionException not thrown.");
    }

    @Test(expected = PrecisionException.class)
    public void testSolver_PrecisionException_Illinois() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(f, 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
        fail("Expected PrecisionException not thrown.");
    }

    @Test(expected = PrecisionException.class)
    public void testSolver_PrecisionException_Pegasus() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(f, 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
        fail("Expected PrecisionException not thrown.");
    }

    @Test
    public void testSolver_NoBracketing() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0}, 1);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(f, 1.0, 0.0, 1.0, AllowedSolution.ANY_SIDE);
            fail("Expected IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    @Test
    public void testSolver_Bracketing() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0}, 1);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(f, 0.0, 1.0, 0.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, EPS);
    }

    @Test
    public void testSolver_Bracketing_LeftSide() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0}, 1);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(f, 0.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, EPS);
    }

    @Test
    public void testSolver_Bracketing_RightSide() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0}, 1);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(f, 0.0, 1.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, root, EPS);
    }

    @Test
    public void testSolver_Bracketing_BelowSide() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0}, 1);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(f, 0.0, 1.0, 0.5, AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, root, EPS);
    }

    @Test
    public void testSolver_Bracketing_AboveSide() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0}, 1);

        BaseSecantSolver solver = new BaseSecantSolver(EPS, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(f, 0.0, 1.0, 0.5, AllowedSolution.ABOVE_SIDE);
        assertEquals(0.0, root, EPS);
    }

    @Test
    public void testSolver_Bracketing_Brent() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -2.0}, 3);

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BrentSolver(EPS);
        double root = solver.solve(f, 1.0, 3.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, EPS);
    }
}