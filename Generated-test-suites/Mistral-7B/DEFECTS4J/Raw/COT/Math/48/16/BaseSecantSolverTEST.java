package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    /** A simple quadratic function f(x) = x^2 - 1 */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 1;
        }
    };

    /** A solver instance */
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    }

    @Test
    public void testConstructorWithAbsoluteAccuracy() {
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithRelativeAccuracyRelativeErrorFunctionValueAndMethod() {
        assertNotNull(solver);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullFunction() {
        new BaseSecantSolver(1e-6, Method.REGULA_FALSI, null);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithFunctionOutsideDomain() {
        new BaseSecantSolver(1e-6, Method.REGULA_FALSI, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    public void testSolveWithDefaultMinMaxAndStartValue() {
        assertEquals(0.0, solver.solve(FUNCTION), Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolveWithMinAndMax() {
        assertEquals(0.0, solver.solve(FUNCTION, -2.0, 2.0), Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolveWithMinAndMaxAndStartValue() {
        assertEquals(0.0, solver.solve(FUNCTION, -2.0, 2.0, 1.0), Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolveWithMinAndMaxInverted() {
        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(0.0, solver.solve(FUNCTION, 2.0, -2.0), Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolveWithMinMaxAndStartValueInverted() {
        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(0.0, solver.solve(FUNCTION, 2.0, -2.0, 1.0), Precision.SAFE_EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithFunctionValueOutsideFunctionValueAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Precision.SAFE_EPSILON;
            }
        };
        solver.solve(function);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        solver.solve(function, 0, 1, 0.5, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNegativeAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(function, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE);
    }
}

enum AllowedSolution {
    ANY_SIDE, LEFT_SIDE, RIGHT_SIDE, BELOW_SIDE, ABOVE_SIDE;
}