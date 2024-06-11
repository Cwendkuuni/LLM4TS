package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularFunctionException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        new BaseSecantSolver(0.01, 0.0, new Method[0], f -> null);
    }

    @Test(expected = SingularFunctionException.class)
    public void testSingularFunction() {
        UnivariateRealFunction function = x -> (x - 0.5) * (x - 0.5);
        new BaseSecantSolver(0.01, 0.0, new Method[0], function);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() {
        UnivariateRealFunction function = x -> 1 / (1 + x);
        new BaseSecantSolver(0.0, 0.0, new Method[0], function, 0).solve(1, function, 1.0, 2.0, 1.0);
    }

    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction function = x -> 1 - Math.exp(-x);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.REGULA_FALSI}) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(1.718281828, solver.solve(10, function, 0.0, 3.0), 0.00001);
    }

    @Test
    public void testIllinois() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testPegasus() {
        UnivariateRealFunction function = x -> Math.pow(x, 3) - 2;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.PEGASUS}) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-1.333333333, solver.solve(10, function, -2.0, 2.0), 0.00001);
    }

    @Test
    public void testAnySide() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.ANY_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testLeftSide() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.LEFT_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testRightSide() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.RIGHT_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testBelowSide() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.BELOW_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testAboveSide() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.ABOVE_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testBracketing() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }

            @Override
            protected void verifyBracketing(double x0, double x1) {
                // Do nothing
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testDifferentMethods() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver1 = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        BaseSecantSolver solver2 = new BaseSecantSolver(0.01, new Method[] {Method.PEGASUS}) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertFalse(solver1.equals(solver2));
    }

    @Test
    public void testFunctionValueAccuracy() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.ANY_SIDE, 1e-7) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
        assertEquals(0.00000734, FunctionUtils.value(function, -2.0), 1e-8);
    }

    @Test
    public void testMaxIterations() {
        UnivariateRealFunction function = x -> 1 / (1 + x);
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.REGULA_FALSI}, AllowedSolution.ANY_SIDE, 1e-7, 10) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals(1.718281828, solver.solve(10, function, 0.0, 3.0), 0.00001);
        assertArrayEquals(new double[] {1.718281828, 1.718279255, 1.718281351, 1.718281774, 1.718281812, 1.718281818}, solver.getIterations(), 0.00001);
    }

    @Test
    public void testGetMin() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[0]) {
            @Override
            protected double getMin() {
                return 1.0;
            }
        };
        assertEquals(1.0, solver.getMin(), 0.0);
    }

    @Test
    public void testGetMax() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[0]) {
            @Override
            protected double getMax() {
                return 3.0;
            }
        };
        assertEquals(3.0, solver.getMax(), 0.0);
    }

    @Test
    public void testComputeObjectiveValue() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[0]) {
            @Override
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }
        };
        assertEquals(-2.0, solver.solve(10, function, -3.0, 3.0), 0.00001);
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver1 = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.ANY_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        BaseSecantSolver solver2 = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.LEFT_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertFalse(solver1.equals(solver2));
    }

    @Test
    public void testAllowedSolutionsEquals() {
        assertEquals(AllowedSolution.ANY_SIDE, AllowedSolution.ANY_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.BELOW_SIDE, AllowedSolution.ABOVE_SIDE);
        assertNotEquals(AllowedSolution.ANY_SIDE, AllowedSolution.LEFT_SIDE);
        assertNotEquals(AllowedSolution.LEFT_SIDE, AllowedSolution.RIGHT_SIDE);
        assertNotEquals(AllowedSolution.BELOW_SIDE, AllowedSolution.ABOVE_SIDE);
    }

    @Test
    public void testToString() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BaseSecantSolver solver = new BaseSecantSolver(0.01, new Method[] {Method.ILLINOIS}, AllowedSolution.ANY_SIDE) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        assertEquals("BaseSecantSolver[method=ILLINOIS, absoluteAccuracy=0.00001, functionValueAccuracy=0.0, allowedSolution=ANY_SIDE, maxIterations=0]", solver.toString());
    }
}