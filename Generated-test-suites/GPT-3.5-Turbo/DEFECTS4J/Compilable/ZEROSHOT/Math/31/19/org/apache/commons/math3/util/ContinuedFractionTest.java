package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    // Method to test getA()
    @Test
    public void testGetA() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        assertEquals(1.0, fraction.getA(0, 1.0), 0.0);
        assertEquals(2.0, fraction.getA(1, 1.0), 0.0);
    }

    // Method to test getB()
    @Test
    public void testGetB() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        assertEquals(2.0, fraction.getB(0, 1.0), 0.0);
        assertEquals(4.0, fraction.getB(1, 1.0), 0.0);
    }

    // Method to test evaluate() with default epsilon and maxIterations
    @Test
    public void testEvaluate_DefaultParams() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        assertEquals(0.0, fraction.evaluate(1.0), 0.000001);
    }

    // Method to test evaluate() with specified epsilon
    @Test
    public void testEvaluate_SpecifiedEpsilon() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        assertEquals(0.0, fraction.evaluate(1.0, 0.0001), 0.000001);
    }

    // Method to test evaluate() with specified maxIterations
    @Test
    public void testEvaluate_SpecifiedMaxIterations() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        assertEquals(0.0, fraction.evaluate(1.0, 1), 0.000001);
    }

    // Method to test evaluate() with specified epsilon and maxIterations
    @Test
    public void testEvaluate_SpecifiedEpsilonAndMaxIterations() {
        ContinuedFraction fraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        assertEquals(0.0, fraction.evaluate(1.0, 0.0001, 1), 0.000001);
    }

}