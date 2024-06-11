package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    

    /**
     * Test constructor with default values.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test constructor with absolute accuracy and method.
     */
    @Test
    public void testConstructorWithAbsoluteAccuracyAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(1e-4, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test constructor with relative accuracy, absolute accuracy, and method.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, 1e-6, BaseSecantSolver.Method.PEGASUS);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test constructor with relative accuracy, absolute accuracy, function value
     * accuracy, and method.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, 1e-6, 1e-7, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-7, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test getMin method.
     */
    @Test
    public void testGetMin() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver.getMin());
    }

    /**
     * Test getMax method.
     */
    @Test
    public void testGetMax() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver.getMax());
    }

    /**
     * Test getMethod method.
     */
    @Test
    public void testGetMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test getAllowedSolution method.
     */
    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test setAllowedSolution method.
     */
    @Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test solve method with the default constructor and a simple function.
     */
    @Test
    public void testSolveWithDefaultConstructorAndSimpleFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x*x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -2, 2);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalException e) {
            fail("MaxEvalException should not be thrown in this test.");
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not be thrown in this test.");
        }
    }

    /**
     * Test solve method with absolute accuracy and a simple function.
     */
    @Test
    public void testSolveWithAbsoluteAccuracyAndSimpleFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x*x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS);
        try {
            double root = solver.solve(100, function, -2, 2);
            assertEquals(0.0, root, 1e-4);
        } catch (MaxEvalException e) {
            fail("MaxEvalException should not be thrown in this test.");
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not be thrown in this test.");
        }
    }

    /**
     * Test solve method with relative accuracy and a simple function.
     */
    @Test
    public void testSolveWithRelativeAccuracyAndSimpleFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x*x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.PEGASUS);
        try {
            double root = solver.solve(100, function, -2, 2);
            assertEquals(0.0, root, 0.01);
        } catch (MaxEvalException e) {
            fail("MaxEvalException should not be thrown in this test.");
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not be thrown in this test.");
        }
    }

    /**
     * Test solve method with absolute accuracy, function value accuracy, and a simple function.
     */
    @Test
    public void testSolveWithAbsoluteAccuracyFunctionValueAccuracyAndSimpleFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x*x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, 1e-5, 1e-7, BaseSecantSolver.Method.ILLINOIS);
        try {
            double root = solver.solve(100, function, -2, 2);
            assertEquals(0.0, root, 0.01);
        } catch (MaxEvalException e) {
            fail("MaxEvalException should not be thrown in this test.");
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not be thrown in this test.");
        }
    }

    /**
     * Test solve method with absolute accuracy, a non-bracketing function, and an allowed solution.
     */
    @Test
    public void testSolveWithAbsoluteAccuracyNonBracketingFunctionAndAllowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-3, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        try {
            double root = solver.solve(100, function, -TestConstants.PI/2, TestConstants.PI/2);
            fail("NoBracketingException should be thrown in this test.");
        } catch (NoBracketingException e) {
            // Expected result
        } catch (MaxEvalException e) {
            fail("MaxEvalException should not be thrown in this test.");
        }
    }

    /**
     * Test solve method with relative accuracy, a non-bracketing function, and an allowed solution.
     */
    @Test
    public void testSolveWithRelativeAccuracyNonBracketingFunctionAndAllowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        try {
            double root = solver.solve(100, function, -TestConstants.PI/2, TestConstants.PI/2);
            fail("NoBracketingException should be thrown in this test.");
        } catch (NoBracketingException e) {
            // Expected result
        } catch (MaxEvalException e) {
            fail("MaxEvalException should not be thrown in this test.");
        }
    }

    /**
     * Test solve method with absolute accuracy, a non-bracketing function, and an overallowed solution.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithAbsoluteAccuracyNonBracketingFunctionAndOverallowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-3, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        solver.solve(100, function, -TestConstants.PI/2, TestConstants.PI/2);
    }

    /**
     * Test solve method with relative accuracy, a non-bracketing function, and an overallowed solution.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithRelativeAccuracyNonBracketingFunctionAndOverallowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        solver.solve(100, function, -TestConstants.PI/2, TestConstants.PI/2);
    }

    
}