package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BisectionSolverTest {
    
    private final double EPSILON = 1e-10;
    
    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }
    
    @Test
    public void testSolve_singleRoot() throws FunctionEvaluationException {
        UnivariateRealFunction f = (double x) -> x - 1;
        BisectionSolver solver = new BisectionSolver();
        
        double root = solver.solve(f, 0, 2);
        
        assertEquals(1, root, EPSILON);
    }
    
    @Test
    public void testSolve_multipleRoots() throws FunctionEvaluationException {
        UnivariateRealFunction f = (double x) -> (x - 1) * (x + 2);
        BisectionSolver solver = new BisectionSolver();
        
        double root = solver.solve(f, 0, 3);
        
        assertEquals(1, root, EPSILON);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_maxIterationsExceeded() throws FunctionEvaluationException {
        UnivariateRealFunction f = (double x) -> x;
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(5);
        
        solver.solve(f, 0, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_invalidInterval() throws FunctionEvaluationException {
        UnivariateRealFunction f = (double x) -> x;
        BisectionSolver solver = new BisectionSolver();
        
        solver.solve(f, 1, 0);
    }
    
    @Test
    public void testSolve_withInitial() throws FunctionEvaluationException {
        UnivariateRealFunction f = (double x) -> (x - 1) * (x + 2);
        BisectionSolver solver = new BisectionSolver();
        
        double root = solver.solve(f, 0, 3, 0.1);
        
        assertEquals(1, root, EPSILON);
    }
    
    @Test
    public void testSolve_withInitial_noRoot() throws FunctionEvaluationException {
        UnivariateRealFunction f = (double x) -> x;
        BisectionSolver solver = new BisectionSolver();
        
        double root = solver.solve(f, 0, 1, 2);
        
        assertEquals(Double.NaN, root, EPSILON);
    }
}