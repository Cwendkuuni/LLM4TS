package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestDouble;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testConstructors() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        BaseSecantSolver solver4 = new BaseSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.ILLINOIS);
        BaseSecantSolver solver5 = new BaseSecantSolver(1e-6, 1e-8, 1e-8, BaseSecantSolver.Method.PEGASUS);

        assertEquals("Default constructor should call the constructor with default accuracy and RegulaFalsi method",
                new BaseSecantSolver(), solver1);

        assertEquals("Constructor with absolute accuracy and RegulaFalsi method",
                new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI), solver1);

        assertEquals("Constructor with relative accuracy and RegulaFalsi method",
                new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, 1e-8), solver1);

        assertEquals("Constructor with absolute, relative and function value accuracy and Illinois method",
                new BaseSecantSolver(1e-6, 1e-8, 1e-8, BaseSecantSolver.Method.ILLINOIS), solver4);

        assertEquals("Constructor with absolute, relative and function value accuracy and Pegasus method",
                new BaseSecantSolver(1e-6, 1e-8, 1e-8, BaseSecantSolver.Method.PEGASUS), solver5);
    }

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10, 0.0);

        assertEquals("The root of x^2 - 4 should be approximately 2", 2.0, root, 1e-6);
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, f, -10, 10, 2.0);

        assertEquals("The root of sin(x) - 0.5 should be approximately 1.27", 1.27, root, 1e-6);
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(100, f, -10, 10, 1.0);

        assertEquals("The root of exp(x) - 2 should be approximately 0.6931", 0.6931, root, 1e-6);
    }

    @Test
    public void testSolve_OutOfBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, f, 10, -1, AllowedSolution.ANY_SIDE));
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, f, -1, 10, AllowedSolution.ANY_SIDE));
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, f, 10, 10, AllowedSolution.ANY_SIDE));
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, f, -1, -1, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testSolve_MaxEvalReached() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        assertThrows(MaxCountExceededException.class, () -> solver.solve(1, f, -10, 10, 0.0));
    }

    @Test
    public void testSolve_FunctionValueTooSmall() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1e-16 * (x - 2);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(100, f, -10, 10, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testDegenerateCase() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Precision.SAFE_MIN * (x - 2);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        double root = solver.solve(100, f, -10, 10, AllowedSolution.ANY_SIDE);

        assertEquals("The root of a degenerate function should be the minimum", 2.0, root, 1e-6);
    }

    @Test
    public void testMethodEnum() {
        assertEquals("RegulaFalsi method string representation", "RegulaFalsi", BaseSecantSolver.Method.REGULA_FALSI.name());
        assertEquals("Illinois method string representation", "Illinois", BaseSecantSolver.Method.ILLINOIS.name());
        assertEquals("Pegasus method string representation", "Pegasus", BaseSecantSolver.Method.PEGASUS.name());
    }
}