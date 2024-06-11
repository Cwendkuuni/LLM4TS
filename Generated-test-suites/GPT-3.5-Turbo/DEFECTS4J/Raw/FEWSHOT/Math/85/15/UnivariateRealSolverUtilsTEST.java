package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 1;
            }
        };
        
        double x0 = -2.0;
        double x1 = 2.0;
        
        double expected = 1.0;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSolve_NullFunction() throws ConvergenceException, FunctionEvaluationException {
        double x0 = -2.0;
        double x1 = 2.0;
        
        UnivariateRealSolverUtils.solve(null, x0, x1);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 1;
            }
        };
        
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        
        double[] expected = {-1.0, 1.0};
        double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        
        assertNotNull(actual);
        assertEquals(expected[0], actual[0], EPSILON);
        assertEquals(expected[1], actual[1], EPSILON);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBracket_NullFunction() throws ConvergenceException, FunctionEvaluationException {
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        
        UnivariateRealSolverUtils.bracket(null, initial, lowerBound, upperBound);
    }
    
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        
        double expected = 2.0;
        double actual = UnivariateRealSolverUtils.midpoint(a, b);
        
        assertEquals(expected, actual, EPSILON);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

}