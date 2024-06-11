//Test getA() and getB() methods
package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ContinuedFractionTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {

            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(0.0, cf.getA(0, 1.0), EPSILON);
        assertEquals(1.0, cf.getA(1, 1.0), EPSILON);
        assertEquals(2.0, cf.getA(2, 1.0), EPSILON);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {

            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.getB(0, 1.0), EPSILON);
        assertEquals(1.0, cf.getB(1, 1.0), EPSILON);
        assertEquals(2.0, cf.getB(2, 1.0), EPSILON);
    }
}