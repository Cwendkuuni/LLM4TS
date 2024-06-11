package org.apache.commons.math.analysis.solvers;


import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    // Method: solve(int, UnivariateRealFunction, double, double, AllowedSolution)
    @Test
    public void testSolve_intUnivariateRealFunctionDoubleDoubleAllowedSolution() {
        BaseSecantSolver solver = new SubBaseSecantSolver(0, 0, 0, null);
        double result = solver.solve(100, null, -1, 1, AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(Double.NaN, result, 0);
    }

    // Method: solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)
    @Test
    public void testSolve_intUnivariateRealFunctionDoubleDoubleDoubleAllowedSolution() {
        BaseSecantSolver solver = new SubBaseSecantSolver(0, 0, 0, null);
        double result = solver.solve(100, null, -1, 1, 0, AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(Double.NaN, result, 0);
    }

    // Method: solve(int, UnivariateRealFunction, double, double, double)
    @Test
    public void testSolve_intUnivariateRealFunctionDoubleDoubleDouble() {
        BaseSecantSolver solver = new SubBaseSecantSolver(0, 0, 0, null);
        double result = solver.solve(100, null, -1, 1, 0);
        Assert.assertEquals(Double.NaN, result, 0);
    }

    // Method: doSolve()
    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new SubBaseSecantSolver(0, 0, 0, null);
        double result = solver.doSolve();
        Assert.assertEquals(Double.NaN, result, 0);
    }
    
    private class SubBaseSecantSolver extends BaseSecantSolver {

        public SubBaseSecantSolver(double relativeAccuracy,
                double absoluteAccuracy, double functionValueAccuracy,
                Method method) {
            super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy,
                    method);
        }
        
        @Override
        protected double computeObjectiveValue(double x) {
            return 0;
        }

        @Override
        protected void verifyBracketing(double lower, double upper)
                throws ConvergenceException {
        }
        
    }
}