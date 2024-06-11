package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
            @Override
            protected void verifyBracketing(double min, double max) {
                if (computeObjectiveValue(min) * computeObjectiveValue(max) >= 0) {
                    throw new MathInternalError("Function values at endpoints do not have different signs.");
                }
            }
        };
        
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3);
        
        assertEquals(2.0, result, 1e-6);
    }
        
    
}