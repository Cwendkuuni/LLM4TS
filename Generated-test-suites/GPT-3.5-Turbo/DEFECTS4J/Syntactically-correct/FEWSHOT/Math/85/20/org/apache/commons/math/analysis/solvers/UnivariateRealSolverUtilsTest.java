package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1, -3, 2}); // f(x) = x^2 - 3x + 2
        double x = UnivariateRealSolverUtils.solve(f, 0, 3); // expects x = 1
        assertEquals(1, x, 0);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_NullFunction() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = null;
        double x = UnivariateRealSolverUtils.solve(f, 0, 3);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolve_InvalidInterval() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1, -3, 2}); // f(x) = x^2 - 3x + 2
        double x = UnivariateRealSolverUtils.solve(f, 3, 0); // invalid interval
    }
    
    @Test
    public void testSolve_WithAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1, -3, 2}); // f(x) = x^2 - 3x + 2
        double x = UnivariateRealSolverUtils.solve(f, 0, 3, 0.001); // expects x = 1
        assertEquals(1, x, 0.001);
    }
    
    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1, -3, 2}); // f(x) = x^2 - 3x + 2
        double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 3); // expects [1, 3]
        assertEquals(1, result[0], 0);
        assertEquals(3, result[1], 0);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_NullFunction() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = null;
        double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 3);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracket_InvalidInterval() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1, -3, 2}); // f(x) = x^2 - 3x + 2
        double[] result = UnivariateRealSolverUtils.bracket(f, 2, 3, 0); // invalid interval
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracket_MaximumIterationsExceeded() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1, -3, 2}); // f(x) = x^2 - 3x + 2
        double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 5, 3); // expects ConvergenceException
    }
    
    @Test
    public void testMidpoint() {
        double a = 0;
        double b = 2;
        double result = UnivariateRealSolverUtils.midpoint(a, b); // expects 1
        assertEquals(1, result, 0);
    }
    
    @Test
    public void testSetup() {
        try {
            UnivariateRealFunction f = null;
            UnivariateRealSolverUtils.setup(f);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("function is null"));
        }
    }
}