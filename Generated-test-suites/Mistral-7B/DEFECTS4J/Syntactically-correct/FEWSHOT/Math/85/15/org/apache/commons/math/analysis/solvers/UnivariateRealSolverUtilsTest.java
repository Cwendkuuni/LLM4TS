package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketedRealRootFinder;
import org.apache.commons.math3.solvers.GoalType;
import org.apache.commons.math3.solvers.LbisRiddersSolver;
import org.apache.commons.math3.solvers.PowellBrentSolver;
import org.apache.commons.math3.solvers.BrentSolver.BrentSolverConfig;
import org.apache.commons.math3.solvers.BrentSolver.BrentSolverConfig.IterationMode;
import org.junit.Test;

import static org.apache.commons.math3.linear.RealVector.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        try {
            double root = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals("Root found correctly", -2.0, root, 1e-6);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was not expected");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was not expected");
        }

        UnivariateRealFunction f2 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f2, x0, x1);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        UnivariateRealFunction f3 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new MathRuntimeException("Not supported yet.");
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f3, x0, x1);
            fail("MathRuntimeException was not expected");
        } catch (MathRuntimeException e) {
            // expected
        }

        UnivariateRealFunction f4 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f4, x0, x1);
            fail("ConvergenceException was expected");
        } catch (ConvergenceException e) {
            // expected
        }

        UnivariateRealFunction f5 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f5, x0, x1);
            fail("ConvergenceException was expected");
        } catch (ConvergenceException e) {
            // expected
        }

        UnivariateRealFunction f6 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f6, x0, x1);
            fail("FunctionEvaluationException was expected");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        UnivariateRealFunction f7 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) / (1 + x * x);
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f7, x0, x1);
            fail("MaxEvalExceededException was expected");
        } catch (MaxEvalExceededException e) {
            // expected
        }

        UnivariateRealFunction f8 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x);
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f8, x0, x1);
            fail("NoBracketingException was expected");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        double[] rootBracket = null;
        try {
            rootBracket = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertEquals("Root bracketing successful", 2, rootBracket.length);
            assertEquals("a", -1.5, rootBracket[0], 1e-6);
            assertEquals("b", -0.5, rootBracket[1], 1e-6);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was not expected");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was not expected");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException was not expected");
        }

        UnivariateRealFunction f2 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f2, initial, lowerBound, upperBound);
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        UnivariateRealFunction f3 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new MathRuntimeException("Not supported yet.");
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f3, initial, lowerBound, upperBound);
            fail("MathRuntimeException was not expected");
        } catch (MathRuntimeException e) {
            // expected
        }

        UnivariateRealFunction f4 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f4, initial, lowerBound, upperBound);
            fail("ConvergenceException was expected");
        } catch (ConvergenceException e) {
            // expected
        }

        UnivariateRealFunction f5 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f5, initial, lowerBound, upperBound);
            fail("FunctionEvaluationException was expected");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        UnivariateRealFunction f6 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f6, initial, lowerBound, upperBound);
            fail("MaxEvalExceededException was expected");
        } catch (MaxEvalExceededException e) {
            // expected
        }

        UnivariateRealFunction f7 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) / (1 + x * x);
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f7, initial, lowerBound, upperBound);
            fail("NoBracketingException was expected");
        } catch (NoBracketingException e) {
            // expected
        }

        UnivariateRealFunction f8 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x + 3);
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f8, initial, lowerBound, upperBound);
            fail("ConvergenceException was expected");
        } catch (ConvergenceException e) {
            // expected
        }

        UnivariateRealFunction f9 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f9, initial, lowerBound, upperBound);
            fail("NoBracketingException was expected");
        } catch (NoBracketingException e) {
            // expected
        }

        UnivariateRealFunction f10 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) * (x - 1) * (x - 2);
            }
        };
        double[] rootBracket10 = UnivariateRealSolverUtils.bracket(
                f10, initial, lowerBound, upperBound, 10);
        assertEquals("Root bracketing successful", 2, rootBracket10.length);
        assertEquals("a", -1.68, rootBracket10[0], 1e-6);
        assertEquals("b", -0.8, rootBracket10[1], 1e-6);

        UnivariateRealFunction f11 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) / (1 + Math.pow(Math.E, x));
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f11, initial, lowerBound, upperBound, 10000);
            fail("MaxEvalExceededException was expected");
        } catch (MaxEvalExceededException e) {
            // expected
        }
    }

    @Test
    public void testBracketWithDifferentSolvers() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;

        UnivariateRealSolver solver1 = new LbisRiddersSolver();
        double[] rootBracket1 = UnivariateRealSolverUtils.bracket(
                f, initial, lowerBound, upperBound, solver1);
        assertEquals("Root bracketing successful with LbisRiddersSolver", 2, rootBracket1.length);
        assertEquals("a", -1.5, rootBracket1[0], 1e-6);
        assertEquals("b", -0.5, rootBracket1[1], 1e-6);

        UnivariateRealSolver solver2 = new BrentSolver();
        double[] rootBracket2 = UnivariateRealSolverUtils.bracket(
                f, initial, lowerBound, upperBound, solver2);
        assertEquals("Root bracketing successful with BrentSolver", 2, rootBracket2.length);
        assertEquals("a", -1.5, rootBracket2[0], 1e-6);
        assertEquals("b", -0.5, rootBracket2[1], 1e-6);

        UnivariateRealSolver solver3 = new PowellBrentSolver();
        double[] rootBracket3 = UnivariateRealSolverUtils.bracket(
                f, initial, lowerBound, upperBound, solver3);
        assertEquals("Root bracketing successful with PowellBrentSolver", 2, rootBracket3.length);
        assertEquals("a", -1.5, rootBracket3[0], 1e-6);
        assertEquals("b", -0.5, rootBracket3[1], 1e-6);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals("Midpoint calculation correct", expected, result, 1e-6);
    }
}