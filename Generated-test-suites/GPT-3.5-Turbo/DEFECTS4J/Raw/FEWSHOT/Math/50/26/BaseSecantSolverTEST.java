package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BaseSecantSolver.Method;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    private static final double EPSILON = 1e-6;
    
    private class TestFunction implements UnivariateRealFunction {
        
        public double value(double x) {
            return x * x - 4;
        }
        
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        TestFunction testFunction = new TestFunction();
        
        // Test when root is between -2 and 2
        double result1 = solver.solve(100, testFunction, -2, 2);
        assertEquals(2.0, result1, EPSILON);
        
        // Test when root is between -4 and -2
        double result2 = solver.solve(100, testFunction, -4, -2);
        assertEquals(-2.0, result2, EPSILON);
        
        // Test when root is between 2 and 4
        double result3 = solver.solve(100, testFunction, 2, 4);
        assertEquals(2.0, result3, EPSILON);
    }

}