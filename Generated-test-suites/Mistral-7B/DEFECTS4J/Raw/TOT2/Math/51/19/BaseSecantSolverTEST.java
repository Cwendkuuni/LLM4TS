package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test solving a simple quadratic function using Regula Falsi method.
     */
    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root is not found.", -2.0, root, 1e-6);
    }

    /**
     * Test solving a simple quadratic function using Illinois method.
     */
    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root is not found.", -2.0, root, 1e-6);
    }

    /**
     * Test solving a simple quadratic function using Pegasus method.
     */
    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root is not found.", -2.0, root, 1e-6);
    }

    /**
     * Test handling maxEval exception.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(f, -5.0, 5.0, 0);
    }

    /**
     * Test handling root not found exception.
     */
    @Test(expected = RootFindersException.class)
    public void testRootNotFoundException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(f, 0, 0);
    }

    /**
     * Test handling number is too small exception.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 1;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(f, 1, 1);
        fail("Number is too small exception should be thrown.");
    }
}