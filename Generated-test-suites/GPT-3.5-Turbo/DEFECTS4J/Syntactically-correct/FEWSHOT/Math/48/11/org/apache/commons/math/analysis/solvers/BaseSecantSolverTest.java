package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BaseSecantSolverTest {
    
    class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 4;
        }
    }
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {};
        double result = solver.solve(100, new TestFunction(), 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(FastMath.abs(result - 2) < 1e-6);
    }
    
    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {};
        try {
            double result = solver.solve(1, new TestFunction(), 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        } catch (ConvergenceException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {};
        double result1 = solver.solve(100, new TestFunction(), 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double result2 = solver.solve(100, new TestFunction(), 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double result3 = solver.solve(100, new TestFunction(), 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertTrue(FastMath.abs(result1 - 2) < 1e-6);
        assertTrue(FastMath.abs(result2 - 2) < 1e-6);
        assertEquals(result3, 0, 1e-6);
    }
    
    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {};
        double result = solver.solve(100, new TestFunction(), 0, 10, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(FastMath.abs(result - 2) < 1e-6);
    }
    
    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {};
        double result = solver.doSolve();
        assertTrue(FastMath.abs(result) < 1e-6);
    }
    
    @Test(expected = MathInternalError.class)
    public void testDoSolveWithInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, null) {};
        double result = solver.doSolve();
    }
    
    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {};
        try {
            solver.verifyBracketing(0, 10);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            assertTrue(false);
        }
        
        try {
            solver.verifyBracketing(10, 0);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testComputeObjectiveValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {};
        double result = solver.computeObjectiveValue(2);
        assertEquals(result, 0, 1e-6);
    }
    
}