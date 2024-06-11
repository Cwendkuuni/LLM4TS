package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullFunction() {
        new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public UnivariateRealFunction getFunction() {
                return null;
            }
        }.solve(10, getFunction(), 0.0, 1.0, 0.5);
    }

    @Test
    public void testSolveRegulaFalsi() {
        UnivariateRealFunction func = new PolynomialFunction(new double[]{-1.0, 4.0, 6.0, 4.0, 1.0}, new int[]{1, 2, 2, 1, 0});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public UnivariateRealFunction getFunction() {
                return func;
            }
        };

        double root = solver.solve(10, getFunction(), 0.0, 1.0, 0.5);
        assertTrue(Precision.isClose(root, 1.0, 1e-6));
    }

    @Test
    public void testSolveIllinois() {
        UnivariateRealFunction func = new PolynomialFunction(new double[]{-1.0, 4.0, 6.0, 4.0, 1.0}, new int[]{1, 2, 2, 1, 0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public UnivariateRealFunction getFunction() {
                return func;
            }
        };

        double root = solver.solve(10, getFunction(), 0.0, 1.0, 0.5);
        assertTrue(Precision.isClose(root, 1.0, 1e-6));
    }

    @Test
    public void testSolvePegasus() {
        UnivariateRealFunction func = new PolynomialFunction(new double[]{-1.0, 4.0, 6.0, 4.0, 1.0}, new int[]{1, 2, 2, 1, 0});
        BaseSecantSolver solver = new PegasusSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            public UnivariateRealFunction getFunction() {
                return func;
            }
        };

        double root = solver.solve(10, getFunction(), 0.0, 1.0, 0.5);
        assertTrue(Precision.isClose(root, 1.0, 1e-6));
    }

    @Test
    public void testSolveWithAllowedSolutionLeftSide() {
        UnivariateRealFunction func = new PolynomialFunction(new double[]{-1.0, 4.0, 6.0, 4.0, 1.0}, new int[]{1, 2, 2, 1, 0});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.LEFT_SIDE) {
            @Override
            public UnivariateRealFunction getFunction() {
                return func;
            }
        };

        double root = solver.solve(10, getFunction(), 0.0, 1.0, 0.5);
        assertTrue(Precision.isClose(root, 1.0, 1e-6));
    }

    @Test
    public void testSolveWithAllowedSolutionRightSide() {
        UnivariateRealFunction func = new PolynomialFunction(new double[]{-1.0, 4.0, 6.0, 4.0, 1.0}, new int[]{1, 2, 2, 1, 0});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.RIGHT_SIDE) {
            @Override
            public UnivariateRealFunction getFunction() {
                return func;
            }
        };

        double root = solver.solve(10, getFunction(), 0.0, 1.0, 0.5);
        assertTrue(Precision.isClose(root, 1.0, 1e-6));
    }

    @Test(expected = MathInternalError.class)
    public void testSolveException() {
        UnivariateRealFunction func = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 0.0, 1.0}, new int[]{1, 0, 1, 0, 1});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public UnivariateRealFunction getFunction() {
                return func;
            }
        };

        solver.solve(10, getFunction(), 0.0, 1.0, 0.5);
    }

    private UnivariateRealFunction getFunction() {
        return null;
    }
}