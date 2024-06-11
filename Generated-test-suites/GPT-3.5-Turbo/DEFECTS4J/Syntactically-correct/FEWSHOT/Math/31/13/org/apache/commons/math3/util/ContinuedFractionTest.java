package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ContinuedFractionTest {
    
    private static final double EPSILON = 1e-9;
    private static final int MAX_ITERATIONS = 100;

    @Test
    public void testGetA() {
        ContinuedFractionImpl c = new ContinuedFractionImpl();
        
        double result = c.getA(0, 0.5);
        assertEquals(1, result, EPSILON);
        
        result = c.getA(1, 0.5);
        assertEquals(2, result, EPSILON);
        
        result = c.getA(2, 0.5);
        assertEquals(3, result, EPSILON);
    }
    
    @Test
    public void testGetB() {
        ContinuedFractionImpl c = new ContinuedFractionImpl();
        
        double result = c.getB(0, 0.5);
        assertEquals(0.5, result, EPSILON);
        
        result = c.getB(1, 0.5);
        assertEquals(0.25, result, EPSILON);
        
        result = c.getB(2, 0.5);
        assertEquals(0.125, result, EPSILON);
    }
    
    @Test
    public void testEvaluate() {
        ContinuedFractionImpl c = new ContinuedFractionImpl();
        
        double result = c.evaluate(0.5);
        assertEquals(1.2857142857, result, EPSILON);
        
        result = c.evaluate(1.0);
        assertEquals(1.4, result, EPSILON);
        
        result = c.evaluate(2.0);
        assertEquals(1.4444444444, result, EPSILON);
    }
    
    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFractionImpl c = new ContinuedFractionImpl();
        
        double result = c.evaluate(0.5, EPSILON);
        assertEquals(1.2857142857, result, EPSILON);
        
        result = c.evaluate(1.0, EPSILON);
        assertEquals(1.4, result, EPSILON);
        
        result = c.evaluate(2.0, EPSILON);
        assertEquals(1.4444444444, result, EPSILON);
    }
    
    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFractionImpl c = new ContinuedFractionImpl();
        
        double result = c.evaluate(0.5, MAX_ITERATIONS);
        assertEquals(1.2857142857, result, EPSILON);
        
        result = c.evaluate(1.0, MAX_ITERATIONS);
        assertEquals(1.4, result, EPSILON);
        
        result = c.evaluate(2.0, MAX_ITERATIONS);
        assertEquals(1.4444444444, result, EPSILON);
    }
    
    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFractionImpl c = new ContinuedFractionImpl();
        
        double result = c.evaluate(0.5, EPSILON, MAX_ITERATIONS);
        assertEquals(1.2857142857, result, EPSILON);
        
        result = c.evaluate(1.0, EPSILON, MAX_ITERATIONS);
        assertEquals(1.4, result, EPSILON);
        
        result = c.evaluate(2.0, EPSILON, MAX_ITERATIONS);
        assertEquals(1.4444444444, result, EPSILON);
    }

    private class ContinuedFractionImpl extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return n + 1;
        }

        @Override
        protected double getB(int n, double x) {
            return 1 / Math.pow(2, n + 1);
        }

    }
}