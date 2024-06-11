package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.BisectionSolver;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionReachedException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    private UnivariateRealFunction function;
    private BisectionSolver solver;

    @Before
    public void setUp() {
        function = new PolynomialFunction(2.0, new double[]{-1.0, 1.0, -1.0});
        solver = new BisectionSolver();
    }

    /**
     * Test that the solver returns the correct root for a simple polynomial function.
     */
    @Test
    public void testSolverSimplePolynomial() {
        double root = solver.solve(function, -1.0, 1.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test that the solver throws an exception when the function is not monotonic.
     */
    @Test
    public void testSolverNotMonotonicFunction() {
        UnivariateRealFunction functionNotMonotonic = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        assertThrows(MaxIterationsExceededException.class, () -> {
            solver.solve(functionNotMonotonic, -1.0, 1.0);
        });
    }

    /**
     * Test that the solver throws an exception when the function is not defined at the end of the interval.
     */
    @Test
    public void testSolverFunctionNotDefinedAtMax() {
        UnivariateRealFunction functionNotDefinedAtMax = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 3.0) {
                    throw new NumberIsTooSmallException("x is too small: " + x);
                }
                return Math.sin(x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(functionNotDefinedAtMax, -1.0, 4.0);
        });
    }

    /**
     * Test that the solver throws an exception when the function is not defined at the start of the interval.
     */
    @Test
    public void testSolverFunctionNotDefinedAtMin() {
        UnivariateRealFunction functionNotDefinedAtMin = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < -2.0) {
                    throw new NumberIsTooSmallException("x is too small: " + x);
                }
                return Math.sin(x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(functionNotDefinedAtMin, -3.0, 1.0);
        });
    }

    /**
     * Test that the solver throws an exception when the function is not continuous.
     */
    @Test
    public void testSolverFunctionNotContinuous() {
        UnivariateRealFunction functionNotContinuous = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    return 1.0 / x;
                } else {
                    throw new NumberIsTooSmallException("x is too small: " + x);
                }
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(functionNotContinuous, -1.0, 1.0);
        });
    }

    /**
     * Test that the solver throws an exception when the absolute accuracy is too small.
     */
    @Test
    public void testSolverAbsAccuracyTooSmall() {
        BisectionSolver solverWithSmallAccuracy = new BisectionSolver(100, 1e-15);
        assertThrows(IllegalArgumentException.class, () -> {
            solverWithSmallAccuracy.solve(function, -1.0, 1.0);
        });
    }

    /**
     * Test that the solver throws an exception when the maximum number of iterations is too small.
     */
    @Test
    public void testSolverMaxIterationsTooSmall() {
        BisectionSolver solverWithSmallIterations = new BisectionSolver(10, 1e-6);
        assertThrows(MaxIterationsExceededException.class, () -> {
            solverWithSmallIterations.solve(function, -1.0, 1.0);
        });
    }

    /**
     * Test that the solver returns the correct root for a simple polynomial function with a custom initial value.
     */
    @Test
    public void testSolverSimplePolynomialCustomInitial() {
        double root = solver.solve(function, -2.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test that the solver returns the correct root for a simple polynomial function with a custom maximum number of iterations.
     */
    @Test
    public void testSolverSimplePolynomialCustomMaxIterations() {
        BisectionSolver solverWithCustomMaxIterations = new BisectionSolver(20, 1e-6);
        double root = solverWithCustomMaxIterations.solve(function, -1.0, 1.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test that the solver returns the correct root for a simple polynomial function with a custom absolute accuracy.
     */
    @Test
    public void testSolverSimplePolynomialCustomAbsAccuracy() {
        BisectionSolver solverWithCustomAbsAccuracy = new BisectionSolver(100, 1e-12);
        double root = solverWithCustomAbsAccuracy.solve(function, -1.0, 1.0);
        assertEquals(0.0, root, 1e-12);
    }

    /**
     * Test that the solver returns the correct root for a complex function.
     */
    @Test
    public void testSolverComplexFunction() {
        UnivariateRealFunction functionComplex = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log1p(Math.exp(x) - 1.0);
            }
        };
        double root = solver.solve(functionComplex, 0.0, 1.0);
        assertEquals(0.56714325, root, 1e-6);
    }

    /**
     * Test that the solver returns the correct root for a complex function with a custom initial value.
     */
    @Test
    public void testSolverComplexFunctionCustomInitial() {
        UnivariateRealFunction functionComplex = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log1p(Math.exp(x) - 1.0);
            }
        };
        double root = solver.solve(functionComplex, -1.0, 1.0, 0.1);
        assertEquals(0.56714325, root, 1e-6);
    }

    /**
     * Test that the solver throws an exception when the function is not defined in the interval.
     */
    @Test
    public void testSolverFunctionNotDefinedInInterval() {
        UnivariateRealFunction functionNotDefinedInInterval = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0.0) {
                    throw new FunctionEvaluationException("Function not defined for x < 0.0");
                }
                return Math.sin(Math.PI / x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(functionNotDefinedInInterval, -1.0, 0.0);
        });
    }

    /**
     * Test that the solver throws an exception when the function evaluates to NaN.
     */
    @Test
    public void testSolverFunctionEvaluatesToNaN() {
        UnivariateRealFunction functionNaN = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.pow(x, 2.0) - 1.0) / (x - 1.0);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(functionNaN, 1.0, 2.0);
        });
    }

    /**
     * Test that the solver returns the correct root for a complex function with a custom maximum number of iterations.
     */
    @Test
    public void testSolverComplexFunctionCustomMaxIterations() {
        UnivariateRealFunction functionComplex = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log1p(Math.exp(x) - 1.0);
            }
        };
        BisectionSolver solverWithCustomMaxIterations = new BisectionSolver(20, 1e-6);
        double root = solverWithCustomMaxIterations.solve(functionComplex, 0.0, 1.0);
        assertEquals(0.56714325, root, 1e-6);
    }

    /**
     * Test that the solver returns the correct root for a complex function with a custom absolute accuracy.
     */
    @Test
    public void testSolverComplexFunctionCustomAbsAccuracy() {
        UnivariateRealFunction functionComplex = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log1p(Math.exp(x) - 1.0);
            }
        };
        BisectionSolver solverWithCustomAbsAccuracy = new BisectionSolver(100, 1e-12);
        double root = solverWithCustomAbsAccuracy.solve(functionComplex, 0.0, 1.0);
        assertEquals(0.56714325, root, 1e-12);
    }

    /**
     * Test that the solver returns the correct root for a complex function with a custom initial value and a custom maximum number of iterations.
     */
    @Test
    public void testSolverComplexFunctionCustomInitialAndMaxIterations() {
        UnivariateRealFunction functionComplex = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log1p(Math.exp(x) - 1.0);
            }
        };
        BisectionSolver solverWithCustomInitialAndMaxIterations = new BisectionSolver(20, 1e-6, 0.1);
        double root = solverWithCustomInitialAndMaxIterations.solve(functionComplex, -1.0, 1.0);
        assertEquals(0.56714325, root, 1e-6);
    }

    /**
     * Test that the solver returns the correct root for a complex function with a custom initial value and a custom absolute accuracy.
     */
    @Test
    public void testSolverComplexFunctionCustomInitialAndAbsAccuracy() {
        UnivariateRealFunction functionComplex = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log1p(Math.exp(x) - 1.0);
            }
        };
        BisectionSolver solverWithCustomInitialAndAbsAccuracy = new BisectionSolver(100, 1e-12, 0.1);
        double root = solverWithCustomInitialAndAbsAccuracy.solve(functionComplex, -1.0, 1.0);
        assertEquals(0.56714325, root, 1e-12);
    }

    /**
     * Test that the solver returns the correct root for a complex function with a custom initial value and a custom maximum number of iterations and a custom absolute accuracy.
     */
    @Test
    public void testSolverComplexFunctionCustomInitialMaxIterationsAndAbsAccuracy() {
        UnivariateRealFunction functionComplex = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log1p(Math.exp(x) - 1.0);
            }
        };
        BisectionSolver solverWithCustomInitialMaxIterationsAndAbsAccuracy = new BisectionSolver(20, 1e-12, 0.1);
        double root = solverWithCustomInitialMaxIterationsAndAbsAccuracy.solve(functionComplex, -1.0, 1.0);
        assertEquals(0.56714325, root, 1e-12);
    }

    /**
     * Test that the solver returns the correct root for a complex function with a custom initial value, a custom maximum number of iterations, and a custom absolute accuracy and a custom function.
     */
    @Test
    public void testSolverComplexFunctionCustomFunction() {
        UnivariateRealFunction functionComplex = x -> Math.log1p(Math.exp(x) - 1.0);
        BisectionSolver solverWithCustomInitialMaxIterationsAndAbsAccuracy = new BisectionSolver(20, 1e-12, 0.1);
        double root = solverWithCustomInitialMaxIterationsAndAbsAccuracy.solve(functionComplex, -1.0, 1.0);
        assertEquals(0.56714325, root, 1e-12);
    }
}