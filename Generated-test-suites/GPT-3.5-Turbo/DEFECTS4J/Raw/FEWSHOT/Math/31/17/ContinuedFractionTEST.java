package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {
    private static final double EPSILON = 10e-9;

    @Test
    public void testGetA() {
        // TODO: Implement test cases
    }

    @Test
    public void testGetB() {
        // TODO: Implement test cases
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {

            @Override
            protected double getA(int n, double x) {
                // TODO: Implement method
            }

            @Override
            protected double getB(int n, double x) {
                // TODO: Implement method
            }
        };

        // TODO: Implement test cases
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {

            @Override
            protected double getA(int n, double x) {
                // TODO: Implement method
            }

            @Override
            protected double getB(int n, double x) {
                // TODO: Implement method
            }
        };

        // TODO: Implement test cases
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {

            @Override
            protected double getA(int n, double x) {
                // TODO: Implement method
            }

            @Override
            protected double getB(int n, double x) {
                // TODO: Implement method
            }
        };

        // TODO: Implement test cases
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {

            @Override
            protected double getA(int n, double x) {
                // TODO: Implement method
            }

            @Override
            protected double getB(int n, double x) {
                // TODO: Implement method
            }
        };

        // TODO: Implement test cases
    }
}