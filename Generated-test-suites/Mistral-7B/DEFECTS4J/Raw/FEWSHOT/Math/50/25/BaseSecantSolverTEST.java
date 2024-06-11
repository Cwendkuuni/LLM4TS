package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.FastMath;

import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.RegulaFalsiSolver;
import org.apache.commons.cli.IllinoisSolver;
import org.apache.commons.cli.PegasusSolver;

public class BaseSecantSolverTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArguments() {
        new BaseSecantSolver(Double.NaN, RegulaFalsiSolver.INSTANCE);
        new BaseSecantSolver(0.0, null);
        new BaseSecantSolver(Double.POSITIVE_INFINITY, RegulaFalsiSolver.INSTANCE);
        new BaseSecantSolver(Double.NEGATIVE_INFINITY, RegulaFalsiSolver.INSTANCE);
        new BaseSecantSolver(-1, RegulaFalsiSolver.INSTANCE);
        new BaseSecantSolver(1, -1, RegulaFalsiSolver.INSTANCE);
    }

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(RegulaFalsiSolver.INSTANCE);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testConstructorWithAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 0.001, RegulaFalsiSolver.INSTANCE);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
        assertEquals(0.01, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 0.001, 0.0001, RegulaFalsiSolver.INSTANCE);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
        assertEquals(0.01, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.0001, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testSolveWithMinAndMax() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 0.001);
    }

    @Test
    public void testSolveWithMinAndMaxAndStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        double root = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 0.001);
    }

    @Test
    public void testSolveWithMinAndMaxAndStartValueAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        double root = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 0.001);
    }

    @Test
    public void testSolveWithMinAndMaxAndStartValueAndAllowedSolutionAndFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        double root = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 0.001);
    }

    @Test
    public void testSolveWithMinAndMaxAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 0.001);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithMinAndMaxAndAllowedSolutionAndInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        solver.setMethod(new Method("INVALID"));
        solver.solve(10, f, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
    }

    @Test
    public void testIllinoisSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, IllinoisSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double root = solver.solve(10, f, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI, root, 0.01);
    }

    @Test
    public void testPegasusSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, PegasusSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        double root = solver.solve(10, f, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.log(2.0), root, 0.01);
    }

    @Test
    public void testRegulaFalsiSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 0.001);
    }
}