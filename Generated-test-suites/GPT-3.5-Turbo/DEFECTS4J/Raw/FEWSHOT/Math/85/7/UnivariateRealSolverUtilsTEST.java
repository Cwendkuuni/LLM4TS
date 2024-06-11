package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, 0, 3);
        assertEquals(2, result, 0);
    }
    
    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, 0, 3, 0.01);
        assertEquals(2, result, 0.01);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        
        UnivariateRealSolverUtils.solve(f, 0, 3);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -5, 5);
        assertNotNull(result);
        assertEquals(-2, result[0], 0);
        assertEquals(2, result[1], 0);
    }
    
    @Test
    public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -5, 5, 1000);
        assertNotNull(result);
        assertEquals(-2, result[0], 0);
        assertEquals(2, result[1], 0);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        
        UnivariateRealSolverUtils.bracket(f, 0, -5, 5);
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 4);
        assertEquals(2, result, 0);
    }
    
    @Test
    public void testSetup() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        UnivariateRealSolverUtils.setup(f);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testSetupWithNullFunction() {
        UnivariateRealFunction f = null;
        
        UnivariateRealSolverUtils.setup(f);
    }
    
    @Test
    public void testLazyHolder() {
        UnivariateRealSolverFactory factory1 = UnivariateRealSolverUtils.LazyHolder.FACTORY;
        UnivariateRealSolverFactory factory2 = UnivariateRealSolverUtils.LazyHolder.FACTORY;
        
        assertNotNull(factory1);
        assertNotNull(factory2);
        assertTrue(factory1 == factory2);
    }
}