package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private class TestFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 4; // f(x) = x^2 - 4
        }
    }

    private BaseSecantSolver.Method method;

    @Before
    public void setUp() {
        method = BaseSecantSolver.Method.REGULA_FALSI;
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10);
        Assert.assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithNoBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        solver.solve(100, new TestFunction(), 0, 2);
    }

    @Test
    public void testSolveWithAllowedSolutionAnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-4, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10);
        Assert.assertEquals(2.0, result, 1e-4);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        solver.setFunctionValueAccuracy(1e-4);
        double result = solver.solve(100, new TestFunction(), -10, 10);
        Assert.assertEquals(2.0, result, 1e-4);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, null) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        solver.solve(100, new TestFunction(), -10, 10);
    }

    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, method) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = solver.solve(100, new TestFunction(), -10, 10);
        Assert.assertEquals(2.0, result, 1e-6);
    }
}