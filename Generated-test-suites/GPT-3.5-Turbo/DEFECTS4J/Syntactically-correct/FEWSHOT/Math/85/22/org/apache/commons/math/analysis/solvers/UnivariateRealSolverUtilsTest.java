package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    private UnivariateRealFunction testFunction;
    private double[] bracketResult;
    private double result;
    
    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        bracketResult = null;
        result = 0.0;
    }
    
    @Test
    public void testSolve() {
        try {
            result = UnivariateRealSolverUtils.solve(testFunction, 0.0, 3.0);
            assertEquals(2.0, result, 0.0001);
            
            result = UnivariateRealSolverUtils.solve(testFunction, 0.0, 3.0, 0.0001);
            assertEquals(2.0, result, 0.0001);
            
            bracketResult = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.5, 0.6);
            result = UnivariateRealSolverUtils.solve(testFunction, bracketResult[0], bracketResult[1]);
            assertEquals(2.0, result, 0.0001);
            
            bracketResult = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.5, 0.6, 100);
            result = UnivariateRealSolverUtils.solve(testFunction, bracketResult[0], bracketResult[1]);
            assertEquals(2.0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testBracket() {
        try {
            bracketResult = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.5, 0.6);
            assertEquals(0.5, bracketResult[0], 0.0001);
            assertEquals(0.6, bracketResult[1], 0.0001);
            
            bracketResult = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.5, 0.6, 100);
            assertEquals(0.5, bracketResult[0], 0.0001);
            assertEquals(0.6, bracketResult[1], 0.0001);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithNullFunction() {
        result = UnivariateRealSolverUtils.solve(null, 0.0, 3.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithInvalidInterval() {
        result = UnivariateRealSolverUtils.solve(testFunction, 3.0, 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithNegativeAccuracy() {
        result = UnivariateRealSolverUtils.solve(testFunction, 0.0, 3.0, -0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithNullFunction() {
        bracketResult = UnivariateRealSolverUtils.bracket(null, 0.0, 0.5, 0.6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithInvalidInterval() {
        bracketResult = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.6, 0.5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithZeroIterations() {
        bracketResult = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.5, 0.6, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithNegativeIterations() {
        bracketResult = UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.5, 0.6, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMidpoint_WithNaN() {
        result = UnivariateRealSolverUtils.midpoint(Double.NaN, 3.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMidpoint_WithInfinity() {
        result = UnivariateRealSolverUtils.midpoint(Double.POSITIVE_INFINITY, 3.0);
    }
}