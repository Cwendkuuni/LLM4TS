package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.FastMath;

public class BaseSecantSolverTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testBaseSecantSolverNullFunction() {
        new BaseSecantSolver(0.0001, RegulaFalsiSolver.REGULA_FALSI).solve(1, null, 0, 1);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBaseSecantSolverNullMinMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        new BaseSecantSolver(0.0001, RegulaFalsiSolver.REGULA_FALSI).solve(1, f, null, null);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBaseSecantSolverInvalidAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        new BaseSecantSolver(0.0001, RegulaFalsiSolver.REGULA_FALSI).solve(1, f, 0, 1, null);
    }

    @Test
    public void testBaseSecantSolverRegulaFalsi() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, RegulaFalsiSolver.REGULA_FALSI);

        double result = solver.solve(1, f, 0, 1);
        assertEquals(-1, result, 0.0001);
    }

    @Test
    public void testBaseSecantSolverIllinois() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, RegulaFalsiSolver.ILLINOIS);

        double result = solver.solve(1, f, 0, 1);
        assertEquals(-1, result, 0.0001);
    }

    @Test
    public void testBaseSecantSolverPegasus() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, RegulaFalsiSolver.PEGASUS);

        double result = solver.solve(1, f, 0, 1);
        assertEquals(-1, result, 0.0001);
    }

    @Test
    public void testBaseSecantSolverRelativeAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, RegulaFalsiSolver.REGULA_FALSI, 1e-8);

        double result = solver.solve(1, f, -3.1415926536, 3.1415926536);
        assertNotNull(result);
        assertTrue(Math.abs(Math.PI - result) < 0.01);
    }

    @Test(expected = MathInternalError.class)
    public void testBaseSecantSolverInvalidMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        new BaseSecantSolver(0.0001, Method.INVALID_METHOD).solve(1, f, 0, 1);
    }

}