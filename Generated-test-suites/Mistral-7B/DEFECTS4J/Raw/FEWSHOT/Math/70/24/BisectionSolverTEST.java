package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolators.LinearInterpolator;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolver() {
        BisectionSolver solver = new BisectionSolver();

        // Test with a simple function
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double root = solver.solve(func, -10, 10);
        assertEquals(Math.PI / 4, root, 0.001);

        // Test with a function with two roots
        UnivariateFunction funcTwoRoots = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.cos(x);
            }
        };
        try {
            root = solver.solve(funcTwoRoots, -5, 5);
            fail("Expected MaxIterationsExceededException but got a solution");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test with a function that is not continuous
        UnivariateFunction funcDiscontinuous = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.sqrt(x);
                } else {
                    throw new UnsupportedOperationException("Function not defined for negative x");
                }
            }
        };
        try {
            root = solver.solve(funcDiscontinuous, -1, 0);
            fail("Expected UnsupportedOperationException but got a solution");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a function that does not bracket the root
        UnivariateFunction funcNoBracket = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        try {
            root = solver.solve(funcNoBracket, 1, 1);
            fail("Expected FunctionEvaluationException but got a solution");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test with a function that brackets the root but does not converge
        UnivariateFunction funcDivergent = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E4;
            }
        };
        try {
            root = solver.solve(funcDivergent, -5, 5);
            fail("Expected MaxIterationsExceededException but got a solution");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test with a function that converges quickly
        UnivariateFunction funcQuickConverge = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E1;
            }
        };
        root = solver.solve(funcQuickConverge, -5, 5);
        assertEquals(E.E1, root, 1e-10);

        // Test with a function that converges slowly
        UnivariateFunction funcSlowConverge = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E10;
            }
        };
        root = solver.solve(funcSlowConverge, -5, 5);
        assertEquals(E.E10, root, 1e-10);

        // Test with a function that has a very large root
        UnivariateFunction funcLargeRoot = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E100;
            }
        };
        try {
            root = solver.solve(funcLargeRoot, -500, 500);
            fail("Expected NumberIsTooSmallException but got a solution");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test with a function that has a very small root
        UnivariateFunction funcSmallRoot = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E14;
            }
        };
        try {
            root = solver.solve(funcSmallRoot, -500, 500);
            fail("Expected NumberIsTooSmallException but got a solution");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithInitialGuess() {
        BisectionSolver solver = new BisectionSolver();

        // Test with a simple function
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double root = solver.solve(func, -10, 10, Math.PI / 4);
        assertEquals(Math.PI / 4, root, 0.001);

        // Test with a function that converges quickly
        UnivariateFunction funcQuickConverge = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E1;
            }
        };
        root = solver.solve(funcQuickConverge, -5, 5, 1);
        assertEquals(E.E1, root, 1e-10);

        // Test with a function that converges slowly
        UnivariateFunction funcSlowConverge = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E10;
            }
        };
        root = solver.solve(funcSlowConverge, -5, 5, 1.5);
        assertEquals(E.E10, root, 1e-10);

        // Test with a function that has a very large root
        UnivariateFunction funcLargeRoot = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E100;
            }
        };
        try {
            root = solver.solve(funcLargeRoot, -500, 500, 100);
            fail("Expected NumberIsTooSmallException but got a solution");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test with a function that has a very small root
        UnivariateFunction funcSmallRoot = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - E.E14;
            }
        };
        try {
            root = solver.solve(funcSmallRoot, -500, 500, 0.00001);
            fail("Expected NumberIsTooSmallException but got a solution");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithCustomFunctionEvaluator() {
        BisectionSolver solver = new BisectionSolver();

        // Test with a simple function
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        UnivariateFunction evaluator = new UnivariateFunction() {
            private final UnivariateFunction func;

            public Evaluator(UnivariateFunction func) {
                this.func = func;
            }

            @Override
            public double value(double x) {
                if (Double.isNaN(x)) {
                    throw new IllegalArgumentException("Input cannot be NaN");
                }
                return func.value(x);
            }
        };

        try {
            double result = solver.solve(evaluator, -10, 10).getValue();
            assertEquals(Math.PI / 4, result, 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();

        // Test with an interval that does not bracket the root
        double min = 5.0;
        double max = 4.0;
        solver.verifyInterval(min, max);
        fail("Expected FunctionEvaluationException but got no exception");

        // Test with an interval that brackets the root
        min = 5.0;
        max = 6.0;
        solver.verifyInterval(min, max);

        // Test with an interval that is very close to bracketing the root
        min = 5.0;
        max = 6.00001;
        solver.verifyInterval(min, max);
        fail("Expected FunctionEvaluationException but got no exception");

        // Test with an interval that is exactly bracketing the root
        min = 5.0;
        max = 5.0;
        solver.verifyInterval(min, max);
        fail("Expected FunctionEvaluationException but got no exception");

        // Test with an interval that is larger than the domain of the function
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x);
            }
        };
        min = 1.0;
        max = -1.0;
        solver.verifyInterval(min, max);
        fail("Expected FunctionEvaluationException but got no exception");
    }
}