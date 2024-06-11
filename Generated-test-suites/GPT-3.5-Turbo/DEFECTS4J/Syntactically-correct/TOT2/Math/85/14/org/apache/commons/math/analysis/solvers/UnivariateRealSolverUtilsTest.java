package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        try {
            double result = UnivariateRealSolverUtils.solve(function, 1.0, 3.0);
            assertEquals(2.0, result, 1e-12);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }
    
    @Test
    public void testBracket() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(function, 0.0, -10.0, 10.0);
            assertNotNull(bracket);
            assertEquals(-1.0, bracket[0], 1e-12);
            assertEquals(1.0, bracket[1], 1e-12);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }
    
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, midpoint, 1e-12);
    }
}