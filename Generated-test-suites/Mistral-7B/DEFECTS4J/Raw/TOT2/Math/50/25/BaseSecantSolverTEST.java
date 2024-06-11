package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new SimpleFunction();
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, AllowedSolution.ANY_SIDE);
            }
        };
    }

    @Test
    public void testFunctionality() {
        double solution = solver.solve(100, function, -1.0, 1.0, 0.0);
        assertEquals(0.75, solution, 1.0e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, 0.0, 0.0, startValue);
            }
        };
        solver.solve(100, function, 0.0, 0.0, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFunctionNull() {
        solver.solve(100, null, -1.0, 1.0, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testMinGreaterThanMax() {
        solver.solve(100, function, 1.0, -1.0, 0.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEval() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue) {
                if (maxEval <= 0) {
                    throw new MaxEvalException("Maximum number of evaluations exceeded.");
                }
                return super.solve(maxEval - 1, f, min, max, startValue);
            }
        };
        solver.solve(0, function, -1.0, 1.0, 0.0);
    }

    @Test
    public void testEdgeCases() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, AllowedSolution.ANY_SIDE);
            }
        };

        double solution1 = solver.solve(100, function, -1.0e6, 1.0e6, 0.0);
        assertTrue(Precision.isClose(solution1, 0.75, 1.0e-6));

        double solution2 = solver.solve(100, function, -1.0, 0.0, 0.0);
        assertTrue(Precision.isClose(solution2, 0.0, 1.0e-6));

        double solution3 = solver.solve(100, function, 0.0, 1.0e6, 0.0);
        assertTrue(Precision.isClose(solution3, 0.75, 1.0e-6));

        double solution4 = solver.solve(100, function, -1.0e6, 1.0, 0.0);
        assertTrue(Precision.isClose(solution4, 0.75, 1.0e-6));
    }

    private static class SimpleFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 0.5;
        }
    }
}