package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;

public class BisectionSolverTest {
    
    private double absoluteAccuracy = 1E-6;
    private int maximalIterationCount = 100;

    @Test
    public void testConstructor() {
        // Test with function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), absoluteAccuracy);

        // Test without function
        solver = new BisectionSolver();
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), absoluteAccuracy);
    }
    
    @Test
    public void testSolveWithFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        
        // Test with valid interval
        try {
            double result = solver.solve(f, 0, 3);
            assertEquals(2, result, absoluteAccuracy);
        } catch (MaxIterationsExceededException e) {
            fail();
        } catch (FunctionEvaluationException e) {
            fail();
        }
        
        // Test with invalid interval
        try {
            double result = solver.solve(f, 3, 5);
            fail();
        } catch (MaxIterationsExceededException e) {
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("Function evaluation error", e.getMessage());
        }
        
        // Test with max iterations exceeded
        try {
            solver = new BisectionSolver(f);
            double result = solver.solve(0, 3, 1.5);
            fail();
        } catch (MaxIterationsExceededException e) {
            assertEquals(100, e.getMaximalIterationCount());
        } catch (FunctionEvaluationException e) {
            fail();
        }
    }
    
    @Test
    public void testSolveWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();
        
        // Test with valid interval
        try {
            double result = solver.solve(0, 3);
            fail();
        } catch (MaxIterationsExceededException e) {
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("Function evaluation error", e.getMessage());
        }
        
        // Test with invalid interval
        try {
            double result = solver.solve(3, 5);
            fail();
        } catch (MaxIterationsExceededException e) {
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("Function evaluation error", e.getMessage());
        }
        
        // Test with max iterations exceeded
        try {
            solver = new BisectionSolver();
            double result = solver.solve(0, 3, 1.5);
            fail();
        } catch (MaxIterationsExceededException e) {
            assertEquals(100, e.getMaximalIterationCount());
        } catch (FunctionEvaluationException e) {
            fail();
        }
    }
    
    @Test
    public void testSolveInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        
        try {
            double result = solver.solve(f, 3, 2);
            fail();
        } catch (MaxIterationsExceededException e) {
            fail();
        } catch (FunctionEvaluationException e) {
            assertEquals("Function evaluation error", e.getMessage());
        }
    }
}