package org.apache.commons.math3.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0/n;
            }
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        
        double result = cf.getA(5, 2.0);
        assertEquals(0.2, result, 0.0001);
    }
    
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 0.0;
            }
            protected double getB(int n, double x) {
                return 1.0/(n+1);
            }
        };
        
        double result = cf.getB(5, 2.0);
        assertEquals(0.16667, result, 0.0001);
    }
    
    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return x;
                }
            }
            protected double getB(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return 1.0;
                }
            }
        };
        
        double result = cf.evaluate(2.0);
        assertEquals(0.66667, result, 0.0001);
    }
    
    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return x;
                }
            }
            protected double getB(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return 1.0;
                }
            }
        };
        
        double result = cf.evaluate(2.0, 0.001);
        assertEquals(0.66667, result, 0.0001);
    }
    
    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return x;
                }
            }
            protected double getB(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return 1.0;
                }
            }
        };
        
        double result = cf.evaluate(2.0, 100);
        assertEquals(0.66667, result, 0.0001);
    }
    
    @Test
    public void testEvaluateWithAllParameters() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return x;
                }
            }
            protected double getB(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return 1.0;
                }
            }
        };
        
        double result = cf.evaluate(2.0, 0.001, 100);
        assertEquals(0.66667, result, 0.0001);
    }
}