package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {
	
    private BisectionSolver solver;
    private UnivariateRealFunction function;
    
    @Before
    public void setup() {
        solver = new BisectionSolver();
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 4;
            }
        };
    }
    
    @Test
    public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
    	double result = solver.solve(function, 1, 3);
    	assertEquals(2.0, result, 0.0);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceededException() throws FunctionEvaluationException, MaxIterationsExceededException {
    	solver.solve(function, 0, 1);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws FunctionEvaluationException, MaxIterationsExceededException {
    	UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
    		@Override
    		public double value(double x) throws FunctionEvaluationException {
    			throw new FunctionEvaluationException(x);
    		}
    	};
    	solver.solve(invalidFunction, 1, 3);
    }
}