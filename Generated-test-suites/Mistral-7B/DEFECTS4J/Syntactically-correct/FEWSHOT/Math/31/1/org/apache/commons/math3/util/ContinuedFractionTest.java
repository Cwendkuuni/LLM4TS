package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new ContinuedFraction() {};
    }

    @Test
    public void testEvaluateWithDefaultParameters() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0);
        assertEquals(2.44948974278317, result, 1e-6);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 1e-10);
        assertEquals(2.44948974278317, result, 1e-11);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0, Integer.MAX_VALUE);
        assertEquals(2.44948974278317, result, 1e-6);
    }

    @Test
    public void testEvaluateWithMaxIterationsAndEpsilon() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 10, 1e-10);
        assertEquals(2.44948974278317, result, 1e-11);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithConvergenceException() {
        ContinuedFraction continuedFraction = new SqrtContinuedFractionWithSlowConvergence();
        continuedFraction.evaluate(2.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxCountExceededException() {
        ContinuedFraction continuedFraction = new SqrtContinuedFractionWithMaxCountExceeded();
        continuedFraction.evaluate(2.0);
    }

}

class SqrtContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return (x + 2.0) / (x + 2.0 - 2.0 * n);
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0;
    }
}

class SqrtContinuedFractionWithSlowConvergence extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return (x + 2.0) / (x + 2.0 - 2.0 * n);
    }

    @Override
    protected double getB(int n, double x) {
        return Math.pow(x, 2.0) / (x + 2.0 - 2.0 * n);
    }
}

class SqrtContinuedFractionWithMaxCountExceeded extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return Math.pow(x, n);
    }

    @Override
    protected double getB(int n, double x) {
        return Math.pow(x, n - 1);
    }
}