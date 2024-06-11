package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import static org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method.ILLINOIS;
import static org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method.PEGASUS;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, null);
        assertNotNull(solver);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIllegalConstructorWithOneArgument() {
        new BaseSecantSolver(1.0);
    }

    @Test
    public void testConstructorWithTwoArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, null);
        assertNotNull(solver);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIllegalConstructorWithThreeArguments() {
        new BaseSecantSolver(0.01, 1.0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIllegalConstructorWithFourArguments() {
        new BaseSecantSolver(0.01, 1.0, 1.0, null);
    }

    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(BaseSecantSolver.AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(BaseSecantSolver.AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllowedSolutionWithInvalidValue() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowedSolution(123);
    }

    @Test
    public void testSolveWithRegulaFalsiMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -1.0, 1.0, 0.0);
        assertApproximatelyEquals(0.0, root, 0.001);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveWithRegulaFalsiMethodMaxEvalExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMaxEvaluations(1);
        solver.solve(f, -1.0, 1.0, 0.0);
    }

    @Test
    public void testSolveWithIllinoisMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, f, -1.0, 1.0, 0.0);
        assertApproximatelyEquals(0.0, root, 0.001);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveWithIllinoisMethodMaxEvalExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        solver.setMaxEvaluations(1);
        solver.solve(f, -1.0, 1.0, 0.0);
    }

    @Test
    public void testSolveWithPegasusMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(100, f, -1.0, 1.0, 0.0);
        assertApproximatelyEquals(0.0, root, 0.001);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolveWithPegasusMethodMaxEvalExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.PEGASUS);
        solver.setMaxEvaluations(1);
        solver.solve(f, -1.0, 1.0, 0.0);
    }

    @Test
    public void testSolveWithCustomStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -1.0, 1.0, 1.0);
        assertApproximatelyEquals(0.0, root, 0.001);
    }

    @Test
    public void testSolveWithCustomAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double root = solver.solve(100, f, -1.0, 1.0, 0.0);
        assertApproximatelyEquals(0.0, root, 0.001);
        assertTrue(root < 0);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, 0.01, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -1.0, 1.0, 0.0);
        assertApproximatelyEquals(0.0, root, 0.0001);
    }

    private void assertApproximatelyEquals(double expected, double actual, double delta) {
        assertEquals("Expected: " + Precision.format(expected, 10), Precision.format(actual, 10), Precision.format(expected, 10) + "+/- " + Precision.format(delta, 10));
    }
}