package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class BaseSecantSolverTest {
  
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
      
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_maxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
      
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(5, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(root));
    }
    
    @Test
    public void testSolve_min() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
      
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }
    
    @Test
    public void testSolve_max() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
      
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 2, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }
    
    @Test
    public void testSolve_allowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
      
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, root, 1e-6);
        
        root = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(-2.0, root, 1e-6);
        
        root = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, root, 1e-6);
        
        root = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_nullSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
      
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 3, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertNull(root);
    }

    @Test
    public void testSolve_withStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
      
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 10, 2.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }
  
}