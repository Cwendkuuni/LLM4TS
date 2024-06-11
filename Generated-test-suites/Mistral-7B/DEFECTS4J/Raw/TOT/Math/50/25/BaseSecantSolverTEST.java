package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0E-6);
        try {
            double root = solver.solve(50, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, 1.0E-6);
        } catch (NoBracketingException e) {
            fail("Expected a solution, but no bracketing was found.");
        }

        // Test with a function that has no roots
        try {
            solver.solve(50, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x);
                }
            }, -2.0, 2.0, 0.0);
            fail("Expected an error, but no error was thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that has multiple roots
        UnivariateRealFunction fMultipleRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2) * (x - 2.0) * (x - 3.0);
            }
        };
        try {
            solver.solve(50, fMultipleRoots, -3.0, 3.0, 1.5);
            fail("Expected multiple roots, but only one was returned.");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test
    public void testIllinois() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1.0E-6);
        try {
            double root = solver.solve(50, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, 1.0E-6);
        } catch (NoBracketingException e) {
            fail("Expected a solution, but no bracketing was found.");
        }

        // Test with a function that has no roots
        try {
            solver.solve(50, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x);
                }
            }, -2.0, 2.0, 0.0);
            fail("Expected an error, but no error was thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that has multiple roots
        UnivariateRealFunction fMultipleRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2) * (x - 2.0) * (x - 3.0);
            }
        };
        try {
            solver.solve(50, fMultipleRoots, -3.0, 3.0, 1.5);
            fail("Expected multiple roots, but only one was returned.");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test
    public void testPegasus() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1.0E-6);
        try {
            double root = solver.solve(50, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, 1.0E-6);
        } catch (NoBracketingException e) {
            fail("Expected a solution, but no bracketing was found.");
        }

        // Test with a function that has no roots
        try {
            solver.solve(50, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x);
                }
            }, -2.0, 2.0, 0.0);
            fail("Expected an error, but no error was thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that has multiple roots
        UnivariateRealFunction fMultipleRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2) * (x - 2.0) * (x - 3.0);
            }
        };
        try {
            solver.solve(50, fMultipleRoots, -3.0, 3.0, 1.5);
            fail("Expected multiple roots, but only one was returned.");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * (x - 1.0);
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0E-6, AllowedSolution.LEFT_SIDE);
        try {
            double root = solver.solve(50, f, 0.0, 1.0, 0.5);
            assertEquals(0.0, root, 1.0E-6);
        } catch (NoBracketingException e) {
            fail("Expected a solution, but no bracketing was found.");
        }

        solver = new RegulaFalsiSolver(1.0E-6, AllowedSolution.RIGHT_SIDE);
        try {
            root = solver.solve(50, f, 0.0, 1.0, 0.5);
            fail("Expected an error, but no error was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        solver = new RegulaFalsiSolver(1.0E-6, AllowedSolution.BELOW_SIDE);
        try {
            root = solver.solve(50, f, 0.0, 1.0, 0.5);
            fail("Expected an error, but no error was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        solver = new RegulaFalsiSolver(1.0E-6, AllowedSolution.ABOVE_SIDE);
        try {
            root = solver.solve(50, f, 0.0, 1.0, 0.5);
            assertEquals(1.0, root, 1.0E-6);
        } catch (NoBracketingException e) {
            fail("Expected a solution, but no bracketing was found.");
        }
    }

    @Test
    public void testRelativeAndAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0E-6, 1.0E-8);
        try {
            double root = solver.solve(50, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, 1.0E-8);
        } catch (NoBracketingException e) {
            fail("Expected a solution, but no bracketing was found.");
        }
    }
}