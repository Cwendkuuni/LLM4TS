package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveSimple() {
        final UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final double root = solver.solve(100, func, -3.0, 3.0);
        PrecisionUtils.assertClose(root, 1.0, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveMinimalOrder() {
        final UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
        solver.solve(100, func, -3.0, 3.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveNegativeMaximalOrder() {
        final UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, -5);
        solver.solve(100, func, -3.0, 3.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() {
        final UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, func, -2.0, 2.0);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        final UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.LEFT_SIDE);
        final double root = solver.solve(100, func, -3.0, 3.0);
        PrecisionUtils.assertClose(root, 1.0, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        final UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final double root = solver.solve(100, func, -3.0, 3.0, 2.0);
        PrecisionUtils.assertClose(root, 1.0, 1e-6);
    }

}