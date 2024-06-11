package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BaseSecantSolverTest {

    double absoluteAccuracy = 1e-6;
    double relativeAccuracy = 1e-6;
    double functionValueAccuracy = 1e-6;
    BaseSecantSolver.Method method;
    BaseSecantSolver solver;

    @Test
    public void testSolve() {
    	UnivariateRealFunction f = new UnivariateRealFunction() {
    		@Override
    		public double value(double x) {
    			return Math.exp(x) - 1;
    		}
    	};
    	
    	double min = 1;
    	double max = 2;
    	
    	//Test allowedSolution = ANY_SIDE
    	double startValue = 1.5;
    	BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
    	solver = new BaseSecantSolver(absoluteAccuracy, method) {
    		protected double computeObjectiveValue(double x) {
    			return f.value(x);
    		}
    	};
    	assertEquals(1, solver.solve(100, f, min, max, startValue, allowedSolution), absoluteAccuracy);
    	
    	//Test allowedSolution = LEFT_SIDE
    	allowedSolution = BaseSecantSolver.AllowedSolution.LEFT_SIDE;
    	solver = new BaseSecantSolver(absoluteAccuracy, method) {
    		protected double computeObjectiveValue(double x) {
    			return f.value(x);
    		}
    	};
    	assertEquals(1, solver.solve(100, f, min, max, startValue, allowedSolution), absoluteAccuracy);
    	
    	//Test allowedSolution = RIGHT_SIDE
    	allowedSolution = BaseSecantSolver.AllowedSolution.RIGHT_SIDE;
    	solver = new BaseSecantSolver(absoluteAccuracy, method) {
    		protected double computeObjectiveValue(double x) {
    			return f.value(x);
    		}
    	};
    	assertEquals(1, solver.solve(100, f, min, max, startValue, allowedSolution), absoluteAccuracy);
    	
    	//Test allowedSolution = BELOW_SIDE
    	allowedSolution = BaseSecantSolver.AllowedSolution.BELOW_SIDE;
    	solver = new BaseSecantSolver(absoluteAccuracy, method) {
    		protected double computeObjectiveValue(double x) {
    			return f.value(x);
    		}
    	};
    	assertEquals(1, solver.solve(100, f, min, max, startValue, allowedSolution), absoluteAccuracy);
    	
    	//Test allowedSolution = ABOVE_SIDE
    	allowedSolution = BaseSecantSolver.AllowedSolution.ABOVE_SIDE;
    	solver = new BaseSecantSolver(absoluteAccuracy, method) {
    		protected double computeObjectiveValue(double x) {
    			return f.value(x);
    		}
    	};
    	assertEquals(1, solver.solve(100, f, min, max, startValue, allowedSolution), absoluteAccuracy);
    }
    
    public void testDoSolve() {
    	UnivariateRealFunction f = new UnivariateRealFunction() {
    		@Override
    		public double value(double x) {
    			return Math.exp(x) - 1;
    		}
    	};
    	
    	double min = 0;
    	double max = 1;
    	solver = new BaseSecantSolver(absoluteAccuracy, method) {
    		protected double computeObjectiveValue(double x) {
    			return f.value(x);
    		}
    	};
    	
    	//Test when function value at x1 is 0
    	double x0 = 0;
    	double x1 = 1;
    	double f0 = f.value(x0);
    	double f1 = f.value(x1);
    	double expected = 0;
    	assertEquals(expected, solver.doSolve(), absoluteAccuracy);
    	
    	//Test when function value at x1 is not 0 and function value is smaller than ftol
    	x0 = 0;
    	x1 = 0.5;
    	f0 = f.value(x0);
    	f1 = f.value(x1);
    	expected = 0;
    	assertEquals(expected, solver.doSolve(), absoluteAccuracy);
    	
    	//Test when function value at x1 is not 0 and function value is larger than ftol
    	x0 = 0;
    	x1 = 3;
    	f0 = f.value(x0);
    	f1 = f.value(x1);
    	expected = 0;
    	assertEquals(expected, solver.doSolve(), absoluteAccuracy);
    }
}