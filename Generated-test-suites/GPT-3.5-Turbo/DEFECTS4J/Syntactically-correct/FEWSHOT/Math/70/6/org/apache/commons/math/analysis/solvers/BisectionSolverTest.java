package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BisectionSolverTest {
    
    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(f, 1, 3);
        
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(10);
        
        double result = solver.solve(f, 1, 3);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                if (x == 2) {
                    throw new FunctionEvaluationException();
                }
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(f, 1, 3);
    }
}