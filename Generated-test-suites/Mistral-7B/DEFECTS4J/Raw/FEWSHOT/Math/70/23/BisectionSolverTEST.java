package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Test;

public class BisectionSolverTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructorWithFunctionDeprecated() {
        new BisectionSolver(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        });
    }

    @Test
    public void testEmptyConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getRelativeAccuracy(), 1E-8);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithNullFunction() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(null, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidMinMax() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testSolveSimpleFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, -2.5, 2.5);
        assertEquals(-1, result, 1E-6);
    }

    @Test
    public void testSolveFunctionWithThreeRoots() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        double result = solver.solve(f, -2, 2);
        assertEquals(-1, result, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        solver.solve(f, 1, 2, 1.5);
    }

    @Test
    public void testSolveWithDifferentMinMax() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, -3, 1);
        assertEquals(-1, result, 1E-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithSmallInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, 0, 1E-12);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceededAndSmallInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        solver.solve(f, 1, 1E-12, 1.5);
    }

    @Test
    public void testSolveWithMaxIterationsExceededAndLargeInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double result = solver.solve(f, 1, 3, 1.5);
        assertEquals(1.2312438566583983, result, 1E-6);
    }

    @Test
    public void testSolveWithRelativeAccuracy() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, -2.5, 2.5, 1E-4);
        assertEquals(-1, result, 1E-7);
    }

    @Test
    public void testSolveWithMaxIterationsAndRelativeAccuracy() {
        BisectionSolver solver = new BisectionSolver(50);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, -2.5, 2.5, 1E-4);
        assertEquals(-1, result, 5E-5);
    }

    @Test
    public void testVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.verifyInterval(-2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalWithInvalidMin() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.verifyInterval(Double.NEGATIVE_INFINITY, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalWithInvalidMax() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.verifyInterval(-2, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMidpoint() {
        double min = 1.1;
        double max = 1.2;
        double expected = (min + max) / 2;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1E-8);
    }

    @Test
    public void testMidpointWithNegativeMin() {
        double min = -1.1;
        double max = 1.2;
        double expected = (min + max) / 2;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1E-8);
    }

    @Test
    public void testMidpointWithZeroMin() {
        double min = 0.0;
        double max = 1.2;
        double expected = (min + max) / 2;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1E-8);
    }

    @Test
    public void testMidpointWithMaxGreaterThanMin() {
        double min = 1.1;
        double max = 2.2;
        double expected = (min + max) / 2;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1E-8);
    }

    @Test
    public void testMidpointWithMaxLessThanMin() {
        double min = 2.2;
        double max = 1.1;
        double expected = (min + max) / 2;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1E-8);
    }

    @Test
    public void testMidpointWithNegativeMax() {
        double min = 1.1;
        double max = -1.2;
        double expected = (min + max) / 2;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1E-8);
    }

    @Test
    public void testSolveWithZeroFunctionValueAtMidpoint() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, -1, 1);
        assertEquals(-1, result, 1E-6);
    }

    @Test
    public void testSolveWithZeroFunctionValueAtMin() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, 0, 1);
        assertEquals(-1, result, 1E-6);
    }

    @Test
    public void testSolveWithZeroFunctionValueAtMax() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double result = solver.solve(f, 1, 0);
        assertEquals(-1, result, 1E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidFunction() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(() -> {
            throw new UnsupportedOperationException("Function evaluation is not supported.");
        }, 0, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolveWithAlreadySetResult() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, 0, 1);
        solver.setResult(1);
        try {
            solver.solve(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return 0;
                }
            }, 0, 1);
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}