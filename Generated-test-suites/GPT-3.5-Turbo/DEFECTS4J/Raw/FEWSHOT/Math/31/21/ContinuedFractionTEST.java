import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

public class ContinuedFractionTest {
    
    private static final double EPSILON = 1e-9;
    private static final int MAX_ITERATIONS = 100;
    
    private class TestFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n < 0) {
                throw new IllegalArgumentException("Invalid argument: n must be positive or zero.");
            }
            return n+1;
        }
        
        @Override
        protected double getB(int n, double x) {
            if (n < 0) {
                throw new IllegalArgumentException("Invalid argument: n must be positive or zero.");
            }
            return n+2;
        }
    }
    
    @Test
    public void testEvaluate() {
        ContinuedFraction fraction = new TestFraction();
        
        assertEquals(1.0, fraction.evaluate(0.0), EPSILON);
        assertEquals(1.5, fraction.evaluate(1.0), EPSILON);
        assertEquals(1.8, fraction.evaluate(2.0), EPSILON);
        assertEquals(2.3333333333333335, fraction.evaluate(3.0), EPSILON);
        assertEquals(2.166666666666667, fraction.evaluate(4.0), EPSILON);
    }
    
    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction fraction = new TestFraction();
        
        double result = fraction.evaluate(0.5, 1e-5);
        assertEquals(1.4615384615384615, result, EPSILON);
        
        result = fraction.evaluate(1.5, 1e-6);
        assertEquals(1.5, result, EPSILON);
        
        result = fraction.evaluate(2.5, 1e-6);
        assertEquals(1.6805555555555556, result, EPSILON);
        
        result = fraction.evaluate(3.5, 1e-8);
        assertEquals(1.8241758241758241, result, EPSILON);
    }
    
    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction fraction = new TestFraction();
        
        double result = fraction.evaluate(0.5, MAX_ITERATIONS);
        assertEquals(1.4615384615384615, result, EPSILON);
        
        result = fraction.evaluate(1.5, MAX_ITERATIONS);
        assertEquals(1.5, result, EPSILON);
        
        result = fraction.evaluate(2.5, MAX_ITERATIONS);
        assertEquals(1.6805555555555556, result, EPSILON);
        
        result = fraction.evaluate(3.5, MAX_ITERATIONS);
        assertEquals(1.8241758241758241, result, EPSILON);
    }
    
    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction fraction = new TestFraction();
        
        double result = fraction.evaluate(0.5, 1e-5, 1000);
        assertEquals(1.4615384615384615, result, EPSILON);
        
        result = fraction.evaluate(1.5, 1e-6, 10000);
        assertEquals(1.5, result, EPSILON);
        
        result = fraction.evaluate(2.5, 1e-6, 10000);
        assertEquals(1.6805555555555556, result, EPSILON);
        
        result = fraction.evaluate(3.5, 1e-8, 100000);
        assertEquals(1.8241758241758241, result, EPSILON);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAThrowsException() {
        ContinuedFraction fraction = new TestFraction();
        
        fraction.getA(-1, 0.0);
        fail("IllegalArgumentException should have been thrown.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetBThrowsException() {
        ContinuedFraction fraction = new TestFraction();
        
        fraction.getB(-1, 0.0);
        fail("IllegalArgumentException should have been thrown.");
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateThrowsConvergenceException() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }
            
            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        
        fraction.evaluate(1.0);
        fail("ConvergenceException should have been thrown.");
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateThrowsMaxCountExceededException() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }
            
            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        
        fraction.evaluate(1.0, Integer.MAX_VALUE);
        fail("MaxCountExceededException should have been thrown.");
    }
    
}