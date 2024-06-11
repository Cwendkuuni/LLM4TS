package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFunction() {
        new BaseSecantSolver(0.0, Method.REGULA_FALSI);
    }

    @Test
    public void testConstructor() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertNotNull(solver);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeRelativeAccuracy() {
        new BaseSecantSolver(-1e-6, 1e-6, Method.REGULA_FALSI);
    }

    @Test
    public void testSolveRegulaFalsi() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double result = solver.solve(10, function, -5.0, 5.0, 2.5);
        assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolveIllinois() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double result = solver.solve(10, function, -5.0, 5.0, 2.5);
        assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolvePegasus() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        double result = solver.solve(10, function, -5.0, 5.0, 2.5);
        assertEquals(-2.0, result, 1e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalExceededRegulaFalsi() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(0, function, -5.0, 5.0, 2.5);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalExceededIllinois() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(0, function, -5.0, 5.0, 2.5);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalExceededPegasus() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(0, function, -5.0, 5.0, 2.5);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketingRegulaFalsi() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 3;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(10, function, -5.0, 5.0, 2.5);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketingIllinois() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 3;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(10, function, -5.0, 5.0, 2.5);
    }

    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver2.getAllowedSolution());
    }

    @Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testVerifyBracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double min = -5.0;
        double max = 5.0;
        solver.verifyBracketing(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketingInvalidInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.verifyBracketing(Double.NaN, Double.NaN);
    }

    @Test
    public void testVerifyBracketingRootInside() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double min = -3.0;
        double max = 3.0;
        solver.verifyBracketing(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketingNonMonotonicFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 3 * x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double min = -5.0;
        double max = 5.0;
        solver.verifyBracketing(min, max);
    }
}

enum AllowedSolution {
    ANY_SIDE, LEFT_SIDE, RIGHT_SIDE, BELOW_SIDE, ABOVE_SIDE;
}