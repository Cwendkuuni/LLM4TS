package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * x;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(0.0, cf.getA(0, 0.0), 1e-15);
        assertEquals(5.0, cf.getA(5, 1.0), 1e-15);
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
                return n * x;
            }
        };

        assertEquals(0.0, cf.getB(0, 0.0), 1e-15);
        assertEquals(5.0, cf.getB(5, 1.0), 1e-15);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0 + n;
            }
        };

        assertEquals(1.0, cf.evaluate(0.0), 1e-15);
        assertEquals(0.5, cf.evaluate(-1.0), 1e-15);
        assertEquals(0.25, cf.evaluate(2.0), 1e-15);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0 + n;
            }
        };

        double result = cf.evaluate(0.0, 1e-5);
        assertEquals(1.0, result, 1e-5);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0 + n;
            }
        };

        double result = cf.evaluate(0.0, 1e-9, 5);
        assertEquals(1.0, result, 1e-9);
    }

    @Test
    public void testEvaluateWithMaxIterationsFailure() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0 + n;
            }
        };

        try {
            cf.evaluate(0.0, 1e-9, 1);
            fail("Exception expected.");
        } catch (MaxCountExceededException e) {
            // success
        }
    }

}