package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

public class BaseSecantSolverTest {
    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                if (arg0 == 42) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        };
        
        double result = solver.solve(100, null, 41, 43, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(42, result, 0.0);
    }
    
    @Test(expected = MathInternalError.class)
    public void testSolve_UnknownMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS) {
            protected double computeObjectiveValue(double arg0) {
                return 0;
            }
        };
        
        solver.solve(100, null, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }
    
    @Test
    public void testSolve_AnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return arg0;
            }
        };
        
        double result = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0, result, 0.0);
    }
    
    @Test
    public void testSolve_LeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return arg0;
            }
        };
        
        double result = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-10, result, 0.0);
    }
    
    @Test
    public void testSolve_RightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return arg0;
            }
        };
        
        double result = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(10, result, 0.0);
    }
    
    @Test
    public void testSolve_BelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return arg0;
            }
        };
        
        double result = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(-10, result, 0.0);
    }
    
    @Test
    public void testSolve_AboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return arg0;
            }
        };
        
        double result = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(10, result, 0.0);
    }
    
    @Test
    public void testSolve_MultipleIterations() {
        final double[] values = {1, 2, 3, 4, 5};
        
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            private int count = 0;

            protected double computeObjectiveValue(double arg0) {
                return values[count++];
            }
        };
        
        double result = solver.solve(100, null, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1, result, 0.0);
    }
    
    @Test
    public void testSolve_MaximumEvaluations() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return 1;
            }
        };
        
        double result = solver.solve(0, null, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Double.NaN, result, 0.0);
    }
    
    @Test
    public void testSolve_NoSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return 1;
            }
        };
        
        double result = solver.solve(100, null, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(Double.NaN, result, 0.0);
    }
    
    @Test
    public void testSolve_BracketingInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return arg0;
            }
        };
        
        double result = solver.solve(100, null, 10, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(10, result, 0.0);
    }
    
    @Test
    public void testSolve_SmallInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double arg0) {
                return arg0;
            }
        };
        
        double result = solver.solve(100, null, 0, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0, result, 0.0);
    }
}