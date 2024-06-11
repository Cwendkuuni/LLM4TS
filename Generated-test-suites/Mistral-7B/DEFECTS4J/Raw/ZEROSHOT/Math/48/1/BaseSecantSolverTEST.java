package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestDoubleComparator;
import org.apache.commons.math3.util.TestUtil;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolverBasic() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS);
        try {
            double root = solver.solve(10, function, -5.0, 5.0, 0.0);
            Assert.assertEquals(0.0, root, 1.0e-6);
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testBaseSecantSolverBracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);

        try {
            double root = solver.solve(10, function, -10.0, 10.0, 0.0);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        TestUtil.checkBracketing(function, -3.14159265359, -2.71828182846, 0.0, 2.71828182846, 3.14159265359, 0.001, new TestDoubleComparator());

        try {
            root = solver.solve(10, function, -3.14159265359, 3.14159265359, 0.0);
            Assert.assertEquals(0.0, root, 1.0e-6);
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBaseSecantSolverInvalidAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public AllowedSolution getAllowedSolution() {
                return AllowedSolution.BELOW_SIDE;
            }
        };

        solver.solve(10, function, -5.0, 5.0, 0.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testBaseSecantSolverMaxEvalExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (x * x);
            }
            };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);

        try {
            solver.solve(0, function, 1.0, 2.0, 1.0);
            Assert.fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // expected
        }
    }
}