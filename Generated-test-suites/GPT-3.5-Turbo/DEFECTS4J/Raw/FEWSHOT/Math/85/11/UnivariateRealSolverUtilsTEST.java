package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    private UnivariateRealFunction f;
    
    @Before
    public void setup() {
        f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x; // replace with your actual function
            }
        };
    }
    
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double solution = UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
        assertNotNull(solution);
        
        // check if solution is close enough to be considered correct
        double epsilon = 0.0001;
        assertTrue(Math.abs(f.value(solution)) < epsilon);
    }
    
    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double solution = UnivariateRealSolverUtils.solve(f, 0.0, 1.0, 0.0001);
        assertNotNull(solution);
        
        // check if solution is close enough to be considered correct
        double epsilon = 0.0001;
        assertTrue(Math.abs(f.value(solution)) < epsilon);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction nullFunction = null;
        UnivariateRealSolverUtils.solve(nullFunction, 0.0, 1.0);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] bracket = UnivariateRealSolverUtils.bracket(f, 0.0, -1.0, 1.0);
        assertNotNull(bracket);
        assertEquals(-1.0, bracket[0], 0.0001);
        assertEquals(1.0, bracket[1], 0.0001);
    }
    
    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        double[] bracket = UnivariateRealSolverUtils.bracket(f, 0.0, -1.0, 1.0, 100);
        assertNotNull(bracket);
        assertEquals(-1.0, bracket[0], 0.0001);
        assertEquals(1.0, bracket[1], 0.0001);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction nullFunction = null;
        UnivariateRealSolverUtils.bracket(nullFunction, 0.0, -1.0, 1.0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracketWithInvalidBounds() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, -1.0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracketWithNegativeMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(f, 0.0, -1.0, 1.0, -1);
    }
    
    @Test
    public void testMidpoint() {
        double a = 0.0;
        double b = 2.0;
        
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.0, midpoint, 0.0001);
    }
}