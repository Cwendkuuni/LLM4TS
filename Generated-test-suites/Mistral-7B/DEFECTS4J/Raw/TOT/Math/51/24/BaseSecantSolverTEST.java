package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test of the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(f);
            final double root = solver.solve(100);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-6);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        }
    }

    /**
     * Test of the constructor with specified accuracies.
     */
    @Test
    public void testConstructorWithAccuracies() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-9, f);
            final double root = solver.solve(100);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-8);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        }
    }

    /**
     * Test of the constructor with relative and absolute accuracies.
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracies() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, f);
            final double root = solver.solve(100);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-5);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        }
    }

    /**
     * Test of the constructor with maximum function value error.
     */
    @Test
    public void testConstructorWithFunctionValueAccuracy() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, 1e-7, f);
            final double root = solver.solve(100);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-5);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        }
    }

    /**
     * Test of the constructor with maximum number of evaluations.
     */
    @Test
    public void testConstructorWithMaxEval() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, f, 50);
            final double root = solver.solve(100);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-5);
        } catch (MaxEvalException e) {
            fail("MaxEvalException should not have been thrown");
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        }
    }

    /**
     * Test of the constructor with unsupported method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithUnsupportedMethod() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, f, BaseSecantSolver.Method.INVALID);
    }

    /**
     * Test of the solve method with default method and default bounds.
     */
    @Test
    public void testSolveWithDefaultMethodAndDefaultBounds() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(f);
            final double root = solver.solve(100);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-6);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        }
    }

    /**
     * Test of the solve method with default method and custom bounds.
     */
    @Test
    public void testSolveWithDefaultMethodAndCustomBounds() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(f);
            final double root = solver.solve(100, -2.0, 2.0);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-6);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not have been thrown");
        }
    }

    /**
     * Test of the solve method with custom method and custom bounds.
     */
    @Test
    public void testSolveWithCustomMethodAndCustomBounds() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS, f);
            final double root = solver.solve(100, -2.0, 2.0);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-5);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not have been thrown");
        }
    }

    /**
     * Test of the solve method with custom method and default bounds.
     */
    @Test
    public void testSolveWithCustomMethodAndDefaultBounds() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        try {
            final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS, f);
            final double root = solver.solve(100);
            assertEquals("Root should be approximately 1.0", 1.0, root, 1e-5);
        } catch (NoBracketingException e) {
            fail("NoBracketingException should not have been thrown");
        }
    }

    /**
     * Test of the solve method with custom method and invalid bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithCustomMethodAndInvalidBounds() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS, f);
        solver.solve(100, 2.0, 1.0);
    }

    /**
     * Test of the solve method with custom method and invalid method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithCustomMethodAndInvalidMethod() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.INVALID, f);
        solver.solve(100);
    }

    /**
     * Test of the solve method with non-bracketing bounds.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithNonBracketingBounds() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS, f);
        solver.solve(100, 2.0, 1.0);
    }

    /**
     * Test of the solve method with function value outside of allowed range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithFunctionValueOutsideAllowedRange() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS, f);
        solver.solve(100, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    }

    /**
     * Test of the solve method with a function that is not real-valued.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithComplexFunction() {
        final UnivariateRealFunction f = new ComplexPolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS, f);
        solver.solve(100);
    }

    /**
     * Test of the solve method with a function that is not differentiable at the root.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNonDifferentiableFunction() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0, 0.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS, f);
        solver.solve(100);
    }
}

class ComplexPolynomialFunction implements UnivariateRealFunction {

    private final PolynomialFunction realPart;

    ComplexPolynomialFunction(double[] coefficients) {
        realPart = new PolynomialFunction(coefficients);
    }

    @Override
    public double value(double x) {
        final double real = realPart.value(x);
        final double imaginary = Math.sqrt(Math.pow(Math.abs(realPart.value(x + 1e-24)), 2.0)
                - Math.pow(real, 2.0));
        return real + imaginary * FunctionUtils.SIXTEEN_PI / 4.0;
    }
}