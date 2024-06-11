
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    private static final double EPSILON = 1e-6;
    
    private BaseSecantSolver solver;
    
    @Before
    public void setUp() {
        solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
    }
    
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        
        double result = solver.solve(100, f, 0, 10);
        assertTrue(FastMath.abs(FastMath.sin(result) - result) <= EPSILON);
    }
    
    @Test
    public void testSolveWithNoRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        
        try {
            solver.solve(100, f, -10, 10);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected behavior
        }
    }
    
    @Test
    public void testSolveWithZeroBracketingInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        try {
            solver.solve(100, f, 0, 0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected behavior
        }
    }
    
    @Test
    public void testSolveWithInvalidAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        
        try {
            solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected behavior
        }
    }
    
    @Test
    public void testSolveWithCustomAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        
        solver.setAbsoluteAccuracy(1e-9);
        double result = solver.solve(100, f, 0, 10);
        assertTrue(FastMath.abs(FastMath.sin(result) - result) <= 1e-9);
    }
    
    @Test
    public void testSolveWithMaxIterationsReached() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        
        try {
            solver.solve(10, f, 0, 10);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected behavior
        }
    }
    
    @Test
    public void testDefaultValues() {
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getRelativeAccuracy(), 0);
    }
}
