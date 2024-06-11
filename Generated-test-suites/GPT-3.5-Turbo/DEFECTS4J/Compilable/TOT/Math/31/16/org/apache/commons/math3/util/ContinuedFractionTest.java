package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;

    // Test cases for getA method
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * x; // Simple linear function
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0; // Constant value
            }
        };

        // Test typical use case
        assertEquals(0.0, cf.getA(0, 0.0), EPSILON);

        // Test edge cases
        assertEquals(0.0, cf.getA(Integer.MIN_VALUE, Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(Double.NaN, cf.getA(Integer.MAX_VALUE, Double.NEGATIVE_INFINITY), EPSILON);
    }

    // Test cases for getB method
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0; // Constant value
            }

            @Override
            protected double getB(int n, double x) {
                return n * x; // Simple linear function
            }
        };

        // Test typical use case
        assertEquals(0.0, cf.getB(0, 0.0), EPSILON);

        // Test edge cases
        assertEquals(Double.POSITIVE_INFINITY, cf.getB(Integer.MIN_VALUE, Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(Double.NEGATIVE_INFINITY, cf.getB(Integer.MAX_VALUE, Double.NEGATIVE_INFINITY), EPSILON);
    }

    // Test cases for evaluate method
    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + x;
            }
        };

        // Test typical use case
        assertEquals(0.5, cf.evaluate(0.5), EPSILON);

        // Test edge cases
        assertEquals(0.0, cf.evaluate(0.0), EPSILON);
        assertEquals(1.0, cf.evaluate(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(Double.NaN, cf.evaluate(Double.NaN), EPSILON);
    }

}