package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BisectionSolverTest {
    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(f, 1, 4);
        assertEquals(2.0, result, EPSILON);
        
        result = solver.solve(f, -4, -1);
        assertEquals(-2.0, result, EPSILON);
        
        result = solver.solve(f, -4, 4);
        assertEquals(2.0, result, EPSILON);
        
        result = solver.solve(f, -4, 1);
        assertEquals(-2.0, result, EPSILON);
        
        result = solver.solve(f, 4, 7);
        assertEquals(Double.NaN, result, EPSILON);
    }

    @Test
    public void testSolveMaxIterationsExceededException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        try {
            solver.solve(f, -10, 10);
        } catch (MaxIterationsExceededException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        try {
            solver.solve(f, -10, 10);
        } catch (FunctionEvaluationException e) {
            assertNotNull(e);
        }
    }
}