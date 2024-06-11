package org.apache.commons.math.analysis.solvers;

import junit.framework.TestCase;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    /** The instance of the BaseSecantSolver class to be tested. */
    private BaseSecantSolver solver;

    /** A simple UnivariateRealFunction for testing. */
    private UnivariateRealFunction simpleFunction;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS);
        simpleFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
    }

    @Test
    public void testSolveSimpleFunction() {
        double root = solver.solve(simpleFunction, -1.5, 1.5);
        TestCase.assertEquals(0.0, Precision.abs(FastMath.sin(root) - root), 1e-8);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveFunctionOutOfBounds() {
        solver.solve(simpleFunction, 1.5, -1.5);
    }

    @Test
    public void testSolveFunctionInvertedInterval() {
        BaseSecantSolver solverInverted = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS,
                AllowedSolution.LEFT_SIDE);
        double root = solverInverted.solve(simpleFunction, -1.5, 1.5);
        TestCase.assertEquals(0.0, Precision.abs(FastMath.sin(root) - root), 1e-8);
    }

    @Test
    public void testSolveFunctionInvalidAllowedSolution() {
        BaseSecantSolver solverInvalid = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS,
                AllowedSolution.BELOW_SIDE);
        try {
            solverInvalid.solve(simpleFunction, -1.5, 1.5);
            TestCase.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolveFunctionZeroFunctionValue() {
        UnivariateRealFunction zeroFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BaseSecantSolver solverZero = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS,
                AllowedSolution.ANY_SIDE);
        double root = solverZero.solve(zeroFunction, -1.5, 1.5);
        TestCase.assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testSolveFunctionZeroBound() {
        UnivariateRealFunction zeroBoundFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BaseSecantSolver solverZeroBound = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS,
                AllowedSolution.BELOW_SIDE);
        try {
            solverZeroBound.solve(zeroBoundFunction, 0.0, 1.5);
            TestCase.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolveFunctionMaxEvalReached() {
        UnivariateRealFunction maxEvalFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BaseSecantSolver solverMaxEval = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS,
                AllowedSolution.ANY_SIDE);
        try {
            solverMaxEval.solve(maxEvalFunction, -1.5, 1.5, 1);
            TestCase.fail("Expected MathIllegalArgumentException");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("maxEvaluation < 1", e.getMessage());
            // Expected
        }
    }
}