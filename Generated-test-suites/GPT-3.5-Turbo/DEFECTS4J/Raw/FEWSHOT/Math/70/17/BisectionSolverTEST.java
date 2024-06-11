package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-6;
    private static final int MAX_ITERATIONS = 100;

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction f = new TestFunction();
        BisectionSolver solver = new BisectionSolver(f);
        
        assertNotNull(solver);
        assertEquals(MAX_ITERATIONS, solver.getMaximalIterationCount());
        assertEquals(EPSILON, solver.getAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
        
        assertNotNull(solver);
        assertEquals(MAX_ITERATIONS, solver.getMaximalIterationCount());
        assertEquals(EPSILON, solver.getAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testSolveWithInitialValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double min = -1.0;
        double max = 1.0;
        double initial = 0.5;
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(f, min, max, initial);
        
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testSolveWithoutInitialValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double min = -1.0;
        double max = 1.0;
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(f, min, max);
        
        assertEquals(0.0, result, EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double min = -1.0;
        double max = 1.0;
        BisectionSolver solver = new BisectionSolver();
        
        solver.setMaximalIterationCount(0);
        solver.solve(f, min, max);
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x;
        }
    }
}