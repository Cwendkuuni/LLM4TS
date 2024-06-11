package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {

    private UnivariateRealFunction func;
    private BisectionSolver solver;
    private double absTolerance;
    private int maxIterations;

    @Before
    public void setUp() {
        func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        solver = new BisectionSolver();
        maxIterations = solver.getMaxIterations();
        absTolerance = solver.getAbsoluteAccuracy();
    }

    @Test
    public void testSolveFunctionPositiveRoot() throws Exception {
        double root = solver.solve(func, 0.0, 1.0);
        Assert.assertEquals(1.0, root, Precision.SAFE_EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        solver.setMaxIterations(0);
        solver.solve(func, 0.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmall() throws Exception {
        solver.setAbsoluteAccuracy(0.0);
        solver.solve(func, 0.0, 1.0);
    }

    @Test
    public void testSolveFunctionNegativeRoot() throws Exception {
        double root = solver.solve(func, -1.0, 0.0);
        Assert.assertEquals(-1.0, root, Precision.SAFE_EPSILON);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction funcException = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new FunctionEvaluationException("Exception");
            }
        };
        solver.solve(funcException, 0.0, 1.0);
    }

    @Test
    public void testSolveFunctionZero() throws Exception {
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        }, 0.0, 1.0);
        Assert.assertEquals(0.0, root, absTolerance);
    }

    @Test
    public void testSolveFunctionZeroWithInitialGuess() throws Exception {
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        }, 1.0, 1.5, 1.2);
        Assert.assertEquals(1.175201193643801, root, absTolerance);
    }

    @Test
    public void testSolveFunctionZeroWithMaxIterations() throws Exception {
        solver.setMaxIterations(10);
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        }, 1.0, 1.5);
        Assert.assertEquals(1.175201193643801, root, absTolerance);
    }

    @Test
    public void testSolveFunctionZeroWithAbsTolerance() throws Exception {
        solver.setAbsoluteAccuracy(1E-5);
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        }, 1.0, 1.5);
        Assert.assertEquals(1.175201193643801, root, 1E-5);
    }

    @Test
    public void testSolveFunctionZeroWithInitialGuessAndMaxIterations() throws Exception {
        solver.setMaxIterations(10);
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        }, 1.0, 1.5, 1.2);
        Assert.assertEquals(1.175201193643801, root, absTolerance);
    }

    @Test
    public void testSolveFunctionZeroWithInitialGuessAndAbsTolerance() throws Exception {
        solver.setAbsoluteAccuracy(1E-5);
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        }, 1.0, 1.5, 1.2);
        Assert.assertEquals(1.175201193643801, root, 1E-5);
    }

    @Test
    public void testSolveFunctionZeroWithPrecisionExceededException() throws Exception {
        solver.setAbsoluteAccuracy(0.0);
        try {
            solver.solve(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) / x;
                }
            }, 1.0, 1.5);
            Assert.fail("Expected PrecisionExceededException");
        } catch (PrecisionExceededException e) {
            // expected
        }
    }
}