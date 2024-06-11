package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        Assert.assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, baseSecantSolver.getAbsoluteAccuracy(), Precision.SAFE_EPSILON);
        Assert.assertEquals(BaseSecantSolver.Method.REGULA_FALSI, baseSecantSolver.getMethod());
    }

    /**
     * Test constructor with specified arguments.
     */
    @Test
    public void testConstructor() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
        Assert.assertEquals(relativeAccuracy, baseSecantSolver.getRelativeAccuracy(), Precision.SAFE_EPSILON);
        Assert.assertEquals(absoluteAccuracy, baseSecantSolver.getAbsoluteAccuracy(), Precision.SAFE_EPSILON);
        Assert.assertEquals(BaseSecantSolver.Method.ILLINOIS, baseSecantSolver.getMethod());
    }

    /**
     * Test constructor with function value accuracy.
     */
    @Test
    public void testConstructorWithFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-5;
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        Assert.assertEquals(relativeAccuracy, baseSecantSolver.getRelativeAccuracy(), Precision.SAFE_EPSILON);
        Assert.assertEquals(absoluteAccuracy, baseSecantSolver.getAbsoluteAccuracy(), Precision.SAFE_EPSILON);
        Assert.assertEquals(functionValueAccuracy, baseSecantSolver.getFunctionValueAccuracy(), Precision.SAFE_EPSILON);
        Assert.assertEquals(BaseSecantSolver.Method.PEGASUS, baseSecantSolver.getMethod());
    }

    /**
     * Test solving with a simple function.
     */
    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = baseSecantSolver.solve(100, testFunction, -5, 5);
            Assert.assertEquals(0.0, root, Precision.SAFE_EPSILON);
        } catch (MaxEvalException e) {
            Assert.fail("Test failed: Maximum number of evaluations reached.");
        }
    }

    /**
     * Test solving with a function with roots at the bounds.
     */
    @Test
    public void testSolveFunctionWithRootsAtBounds() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = baseSecantSolver.solve(100, testFunction, -1, 1);
            Assert.assertEquals(-1.0, root, Precision.SAFE_EPSILON);
        } catch (MaxEvalException e) {
            Assert.fail("Test failed: Maximum number of evaluations reached.");
        }
    }

    /**
     * Test solving with a function with roots between the bounds.
     */
    @Test
    public void testSolveFunctionWithRootsBetweenBounds() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        try {
            double root = baseSecantSolver.solve(100, testFunction, -6, 6);
            Assert.assertFalse("Test failed: Root not found.", root == 0.0);
            Assert.assertTrue("Test failed: Root is within the bounds.", Math.abs(testFunction.value(root)) <= Precision.SAFE_EPSILON);
        } catch (MaxEvalException e) {
            Assert.fail("Test failed: Maximum number of evaluations reached.");
        }
    }

    /**
     * Test solving with a function with no real roots.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveFunctionWithNoRealRoots() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }
        };
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        try {
            baseSecantSolver.solve(100, testFunction, -5, 5);
        } catch (MaxEvalException e) {
            Assert.fail("Test failed: Maximum number of evaluations reached.");
        }
    }

    /**
     * Test solving with a function with singularity at a bound.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveFunctionWithSingularityAtBound() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new ArithmeticException("Division by zero.");
                }
                return Math.log(Math.abs(x)) / x;
            }
        };
        BaseSecantSolver baseSecantSolver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        try {
            baseSecantSolver.solve(100, testFunction, -1, 0);
        } catch (MaxEvalException e) {
            Assert.fail("Test failed: Maximum number of evaluations reached.");
        }
    }
}