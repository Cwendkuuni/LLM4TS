package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.apache.commons.math3.util.Precision.*;

public class ContinuedFractionTest {

    @Test
    public void testSquareRootContinuedFraction() {
        ContinuedFraction sqrt = new SquareRootContinuedFraction();
        double epsilon = 1e-10;
        double x = 2.0;

        // Test evaluate methods
        assertEquals(Math.sqrt(x), sqrt.evaluate(x), epsilon);
        assertEquals(Math.sqrt(x), sqrt.evaluate(x, epsilon), epsilon);
        assertEquals(Math.sqrt(x), sqrt.evaluate(x, epsilon, 10), epsilon);

        // Test getA and getB methods
        assertEquals(2.0, sqrt.getA(0, x), epsilon);
        assertEquals(-1.5, sqrt.getA(1, x), epsilon);
        assertEquals(1.0, sqrt.getB(0, x), epsilon);
        assertEquals(0.5, sqrt.getB(1, x), epsilon);

        // Test convergence and maxIterations
        try {
            sqrt.evaluate(x, epsilon, 0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        try {
            sqrt.evaluate(x, epsilon, 1);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }

        try {
            sqrt.evaluate(x, epsilon, 1000001);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testLogContinuedFraction() {
        ContinuedFraction log = new LogContinuedFraction();
        double epsilon = 1e-10;
        double x = 1.5;

        // Test evaluate methods
        assertEquals(Math.log(x), log.evaluate(x), epsilon);
        assertEquals(Math.log(x), log.evaluate(x, epsilon), epsilon);
        assertEquals(Math.log(x), log.evaluate(x, epsilon, 10), epsilon);

        // Test getA and getB methods
        assertEquals(2.0, log.getA(0, x), epsilon);
        assertEquals(-1.0, log.getA(1, x), epsilon);
        assertEquals(1.0 / x, log.getB(0, x), epsilon);
        assertEquals(-1.0 / (x * (x - 1.0)), log.getB(1, x), epsilon);

        // Test convergence and maxIterations
        try {
            log.evaluate(x, epsilon, 0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        try {
            log.evaluate(x, epsilon, 1);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }

        try {
            log.evaluate(x, epsilon, 1000001);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}