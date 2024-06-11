package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test case 1
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.5;
            }

			@Override
			protected double getB(int n, double x) {
				return 0.5;
			}
        };
        double result1 = cf.getA(0, 1);
        assertEquals(0.5, result1, 0.0);

        // Test case 2
        ContinuedFraction cf2 = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -1;
            }

			@Override
			protected double getB(int n, double x) {
				return 2;
			}
        };
        double result2 = cf2.getA(5, 2);
        assertEquals(-1, result2, 0.0);
    }

    @Test
    public void testGetB() {
        // Test case 1
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.5;
            }

			@Override
			protected double getB(int n, double x) {
				return 0.5;
			}
        };
        double result1 = cf.getB(0, 1);
        assertEquals(0.5, result1, 0.0);

        // Test case 2
        ContinuedFraction cf2 = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -1;
            }

			@Override
			protected double getB(int n, double x) {
				return 2;
			}
        };
        double result2 = cf2.getB(5, 2);
        assertEquals(2, result2, 0.0);
    }

    @Test
    public void testEvaluate() {
        // Test case 1
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.5;
            }

			@Override
			protected double getB(int n, double x) {
				return 0.5;
			}
        };
        double result1 = cf.evaluate(1);
        assertEquals(0.5, result1, 0.0);

        // Test case 2
        ContinuedFraction cf2 = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -1;
            }

			@Override
			protected double getB(int n, double x) {
				return 2;
			}
        };
        double result2 = cf2.evaluate(2);
        assertEquals(-3, result2, 0.0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        // Test case 1
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.5;
            }

			@Override
			protected double getB(int n, double x) {
				return 0.5;
			}
        };
        double result1 = cf.evaluate(1, 0.01);
        assertEquals(0.5, result1, 0.0);

        // Test case 2
        ContinuedFraction cf2 = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -1;
            }

			@Override
			protected double getB(int n, double x) {
				return 2;
			}
        };
        double result2 = cf2.evaluate(2, 0.01);
        assertEquals(-3, result2, 0.0);
    }
    
    @Test
    public void testEvaluateWithMaxIterations() {
        // Test case 1
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.5;
            }

			@Override
			protected double getB(int n, double x) {
				return 0.5;
			}
        };
        double result1 = cf.evaluate(1, 10);
        assertEquals(0.5, result1, 0.0);

        // Test case 2
        ContinuedFraction cf2 = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -1;
            }

			@Override
			protected double getB(int n, double x) {
				return 2;
			}
        };
        double result2 = cf2.evaluate(2, 10);
        assertEquals(-3, result2, 0.0);
    }
    
    @Test
    public void testEvaluateWithAllParams() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.5;
            }

			@Override
			protected double getB(int n, double x) {
				return 0.5;
			}
        };
        double result1 = cf.evaluate(1, 0.01, 10);
        assertEquals(0.5, result1, 0.0);

        ContinuedFraction cf2 = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -1;
            }

			@Override
			protected double getB(int n, double x) {
				return 2;
			}
        };
        double result2 = cf2.evaluate(2, 0.01, 10);
        assertEquals(-3, result2, 0.0);
    }
}
